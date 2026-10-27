# Projekat: Dinamička forma za unos podataka o osobi (Softverski Paterni)

Ovaj projekat implementira modularnu desktop formu za unos podataka o osobi u Java Swing okruženju, razvijenu u okviru predmeta **Softverski Paterni / Projektovanje softvera**.

Arhitektura projekta se oslanja na projektne uzore (**Strategy**, **Polymorphic UI Component**) i dinamičku zamenu komponenti korisničkog interfejsa (*Pluggable UI Components*), što omogućava lako proširivanje, višekratnu iskoristivost komponenti i promenu načina unosa podataka u jednoj liniji koda.

---

## 1. Primenjeni projektni uzori i arhitektura

### 1.1. Polimorfne UI komponente (*Polymorphic UI Components*)
Sve ulazne komponente nasleđuju `javax.swing.JPanel` i implementiraju zajednički interfejs `component.FormInputPanel`:
- `Object getValue() throws Exception` – vraća unetu i validiranu vrednost odgovarajućeg tipa (`String`, `Integer`, `LocalDate`), a u slučaju greške ispisuje poruku na samoj komponenti (crvenim tekstom) i baca izuzetak.
- `void clearError()` – uklanja ispisane poruke o grešci.
- `void reset()` – vraća komponentu u početno stanje (čisti unos i briše greške).
- `void setLabel(String label)` i `String getLabel()` – postavljanje i čitanje teksta labele komponente.
- `Component getComponent()` – pomoćna metoda koja vraća AWT/Swing komponentu za ugradnju u kontejnere.

Zahvaljujući ovom interfejsu, glavna forma `PersonForm` manipuliše svim ulaznim elementima na uniforman, polimorfan način (npr. u metodi `btnResetActionPerformed` gde se jednim pozivom prolazi kroz listu svih komponenti i poziva `panel.reset()`).

### 1.2. Uzor Strategija (*Strategy Pattern*)
Validacija unetih podataka odvojena je od same UI komponente kroz interfejs strategije:
```java
public interface MyValidator {
    void validate(Object value) throws Exception;
}
```
Konkretne strategije validacije:
- **`NumberValidator`** – proverava da li je uneta vrednost validan ceo broj (`Integer.parseInt`), koristi se za validaciju polja za godine.
- **`GenderValidator`** – proverava tekstualni unos pola (dozvoljene vrednosti: `Muški`, `Ženski`, `Muski`, `Zenski`, `M`, `Ž`, `Z`).
- **`MaritalStatusValidator`** – proverava tekstualni unos bračnog statusa (dozvoljene vrednosti: `Neoženjen / Neudata`, `Oženjen / Udata`, `Razveden / Razvedena` itd.).

Paneli (`InputTextPanel`, `InputDatePanel`) imaju referencu na `MyValidator` (`setMyValidator(MyValidator)`), što omogućava dinamičku promenu pravila validacije u vreme izvršavanja bez potrebe za menjanjem koda samih UI komponenti.

### 1.3. Dinamička zamena komponenti (*Pluggable UI Components*)
U klasi `PersonForm` kontejneri za unos pola (`pnlGender`) i bračnog statusa (`pnlMaritalStatus`) nisu fiksno vezani za određenu Swing kontrolu, već koriste generičku metodu:
```java
public FormInputPanel setInputComponent(JPanel container, FormInputPanel panel) {
    if (container != null && panel != null) {
        container.removeAll();
        container.setLayout(new java.awt.BorderLayout());
        container.add(panel.getComponent(), java.awt.BorderLayout.CENTER);
        container.revalidate();
        container.repaint();
    }
    return panel;
}
```
Ovo omogućava promenu načina unosa u samo jednoj liniji koda. Na primer:
- **Pol preko tekstualnog polja sa validatorom:**
  ```java
  gender = setInputComponent(pnlGender, new InputTextPanel("Pol:", new GenderValidator()));
  ```
- **Pol preko padajuće liste (ComboBox):**
  ```java
  gender = setInputComponent(pnlGender, new InputComboBoxPanel("Pol:"));
  ```
Isto važi i za bračni status, gde se može koristiti `InputTextPanel` sa `MaritalStatusValidator`-om ili `InputComboBoxPanel`.

---

## 2. Struktura projekta

```
src/
├── component/
│   ├── FormInputPanel.java             <-- Zajednički interfejs za sve ulazne panele
│   ├── InputTextPanel.java / .form     <-- Panel za unos teksta sa podrškom za MyValidator
│   ├── InputDatePanel.java / .form     <-- Panel za unos datuma (dd.MM.uuuu) sa parsiranjem u LocalDate
│   └── InputComboBoxPanel.java / .form <-- Generički panel za unos putem JComboBox padajuće liste
├── validator/
│   ├── MyValidator.java                <-- Interfejs strategije validacije
│   ├── NumberValidator.java            <-- Strategija za validaciju brojeva (godine)
│   ├── GenderValidator.java            <-- Strategija za tekstualnu validaciju pola
│   └── MaritalStatusValidator.java     <-- Strategija za tekstualnu validaciju bračnog statusa
├── model/
│   └── Person.java                     <-- Domenski model koji predstavlja podatke o osobi
├── form/
│   ├── PersonForm.java / .form         <-- Glavna Swing forma sa kontrolama i obradom akcija
│   └── (NetBeans GUI Builder podrška)
└── main/
    └── Main.java                       <-- Ulazna tačka aplikacije (kreira i prikazuje PersonForm)
```

---

## 3. Implementirane komponente i funkcionalnosti

### 3.1. `InputTextPanel`
- Sadrži `JLabel` za naziv polja, `JTextField` za unos i `JLabel` za ispis greške u crvenoj boji.
- Osnovna validacija proverava da polje nije prazno.
- Ukoliko je postavljen `MyValidator`, poziva se njegova metoda `validate(...)`.
- Konstruktor `InputTextPanel(String label)` poseduje internu logiku koja automatski prepoznaje oznake `"pol"` i `"bračni"` i dodeljuje odgovarajući validator.

### 3.2. `InputDatePanel`
- Sadrži `JLabel` za naziv polja, `JTextField` za unos datuma i `JLabel` za grešku.
- Proverava da polje nije prazno (`"Polje 'Datum rodjenja' je obavezno"`).
- Opciono podržava strategiju validacije (`MyValidator`).
- Parsira uneti datum u `java.time.LocalDate` koristeći format `dd.MM.uuuu` (automatski toleriše tačku na kraju unosa).

### 3.3. `InputComboBoxPanel`
- Generička komponenta sa padajućom listom (`JComboBox<Object>`).
- Konstruktor prepoznaje nazive `"pol"` ili `"bračni"` i automatski inicijalizuje stavke zajedno sa placeholder stavkom (npr. `"--- Odaberi pol ---"`, `"--- Odaberite bracni status ---"`), a omogućava i prosleđivanje proizvoljnog niza ili liste objekata.
- `getValue()` proverava da li je korisnik izabrao validnu stavku; ukoliko nije ništa izabrano ili je ostao selektovan placeholder, ispisuje grešku i baca izuzetak.

### 3.4. Glavna forma (`PersonForm`)
- **Unos polja:** Ime, Prezime, Godine, Pol, Datum rođenja i Bračni status.
- **Validacija pri čuvanju (`btnSave`):**
  - Klikom na dugme **"Sačuvaj"** prolazi se kroz sva polja pozivom `getValue()`.
  - Greške se prikazuju direktno na odgovarajućim panelima ispod polja i prikupljaju u zbirnu poruku.
  - Ako postoji bar jedno neispravno polje, prikazuje se modalni dijalog (`JOptionPane.WARNING_MESSAGE`) sa listom svih grešaka.
  - Ako su svi unosi validni, kreira se instanca `model.Person` i prikazuje se potvrdni dijalog (`JOptionPane.INFORMATION_MESSAGE`) sa formatiranim podacima.
- **Resetovanje forme (`btnReset`):**
  - Klikom na dugme **"Poništi"** polimorfno se poziva `reset()` na svim panelima iz liste `getInputPanels()`, što čisti sva polja i briše poruke o greškama.

### 3.5. Domenski model (`Person`)
- Enkapsulira podatke: `firstName`, `lastName`, `age`, `gender`, `birthDate`, `maritalStatus`.
- Implementira `toString()` koji lepo formatira podatke i datum (`dd.MM.yyyy`) za prikaz.

---

## 4. Pokretanje projekta

### Zahtevi:
- **Java SE Development Kit (JDK):** verzija 17 ili novija
- **NetBeans IDE** (ili bilo koji drugi IDE sa podrškom za NetBeans Ant projekte)

### Pokretanje iz NetBeans IDE:
1. Otvorite NetBeans i izaberite **File** -> **Open Project**.
2. Odaberite korenski direktorijum ovog projekta (`PrviProjekatPaterni`).
3. Pritisnite **F6** ili kliknite na zelenu ikonicu **Run Project**.

### Pokretanje preko komandne linije (JAR):
U korenskom direktorijumu projekta pokrenite:
```bash
java -jar dist/PrviProjekatPaterni.jar
```

Ili kompajlirajte i pokrenite preko Apache Ant-a:
```bash
ant run
```
