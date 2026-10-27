# Projekat: Forma za unos podataka o osobi (Softverski Paterni)

Ovaj projekat implementira proširenu formu za unos podataka o osobi u Java Swing okruženju, sa višekratno iskoristivim UI komponentama (panelima) i primenom projektnih uzora (**Strategy**, **Polymorphic UI Component**).

---

## 1. Implementirane komponente i arhitektura

Svi ulazni paneli nasleđuju `javax.swing.JPanel` i implementiraju zajednički interfejs `component.FormInputPanel`:
- `Object getValue() throws Exception` – vraća validiranu vrednost ili ispisuje grešku na panelu i baca izuzetak.
- `void clearError()` – uklanja poruku o grešci sa panela.
- `void reset()` – vraća panel u početno stanje.

### Struktura paketa:
```
src/
├── component/
│   ├── FormInputPanel.java            <-- Zajednički interfejs za sve ulazne panele
│   ├── InputTextPanel.java / .form    <-- Unos teksta u JTextField (Ime, Prezime, Godine)
│   ├── InputGenderPanel.java / .form  <-- Unos pola preko JRadioButton (Muški / Ženski)
│   ├── InputDatePanel.java / .form    <-- Unos datuma rođenja u JTextField (dd.MM.yyyy)
│   ├── InputMaritalStatusPanel.java   <-- Unos bračnog statusa preko JComboBox
│   │   └── .form
│   └── InputComboBoxPanel.java / .form<-- Generička komponenta za JComboBox unos
├── validator/
│   ├── MyValidator.java               <-- Interfejs strategije validacije
│   ├── NumberValidator.java           <-- Validacija brojeva (za godine)
│   └── DateValidator.java             <-- Striktna validacija datuma (dd.MM.yyyy)
├── model/
│   └── Person.java                    <-- Domenski model osobe
├── form/
│   ├── PersonForm.java / .form        <-- Glavna forma sa svim poljima i akcijama
│   └── (NetBeans GUI Builder podrška)
└── main/
    └── Main.java                      <-- Pokretač forme
```

---

## 2. Zahtevane komponente iz zadatka

1. **Panel za Pol (`InputGenderPanel`)**:
   - Koristi radio dugmad (`rbMale` – "Muški", `rbFemale` – "Ženski") grupisana u `ButtonGroup` tako da je moguć odabir samo jedne opcije.
   - Ako korisnik ne odabere pol, ispisuje grešku `"Morate odabrati pol"`.

2. **Panel za Datum rođenja (`InputDatePanel`)**:
   - Vrednost prihvata u `JTextField` (`inputText`).
   - Prikazuje format hint `(dd.MM.yyyy)`.
   - Koristi `DateValidator` koji proverava:
     - Da polje nije prazno
     - Da je format striktno `dd.MM.yyyy`
     - Da je datum kalendarski ispravan (onemogućava npr. 31.02)
     - Da datum nije u budućnosti i da godina nije pre 1900.
   - Vraća instancu `java.time.LocalDate`.

3. **Panel za Bračni status (`InputMaritalStatusPanel`)**:
   - Vrednost prihvata u `JComboBox` (`cbMaritalStatus`).
   - Sadrži opcije:
     - `--- Izaberite bračni status ---` (placeholder)
     - `Neoženjen / Neudata`
     - `Oženjen / Udata`
     - `Razveden / Razvedena`
     - `Udovac / Udovica`
   - Validacija zahteva da korisnik odabere konkretnu opciju (a ne placeholder).
   - Dostupan je i generički `InputComboBoxPanel` za bilo koji drugi ComboBox unos.

---

## 3. Glavna forma (`PersonForm`)

- **Kompletna validacija odjednom:** Prilikom klika na dugme **"Sačuvaj"**, validiraju se sva polja paralelno tako da korisnik vidi sve greške istovremeno na formi ispod odgovarajućih polja.
- **Logička provera (Cross-field validation):** Forma poredi uneti broj godina sa izračunatim godinama iz datuma rođenja (`Period.between(birthDate, LocalDate.now()).getYears()`). U slučaju neslaganja većeg od 1 godine, prikazuje se dijalog sa pitanjem da li korisnik sigurno želi da sačuva takve podatke.
- **Dugme "Poništi" (`btnReset`):** Polimorfno resetuje sva polja i briše greške jednim klikom.
- **Domenski model:** Podaci se pakuju u instancu klase `Person`.

---

## 4. Lokacija projekta i pokretanje

Projekat je uspešno iskompajliran i nalazi se na sledećim lokacijama:
- **Radni projekat:** `C:\Users\student2\Desktop\pt\PrviProjekatPaterni`
- **Gotova ZIP arhiva:** `C:\Users\student2\Desktop\pt\PrviProjekatPaterni_Zavrseno.zip`

### Otvaranje u NetBeans-u:
1. U NetBeans-u izaberite: **File** -> **Open Project**.
2. Navigirajte do: `C:\Users\student2\Desktop\pt\PrviProjekatPaterni` i kliknite **Open Project**.
3. Pritisnite **F6** (ili desni klik na projekat -> **Run**) za pokretanje!
