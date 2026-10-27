/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validator;

/**
 * Validator za proveru tekstualnog unosa pola.
 * @author student2
 */
public class GenderValidator implements MyValidator {

    @Override
    public void validate(Object value) throws Exception {
        if (value == null || value.toString().trim().isEmpty()) {
            throw new Exception("Pol je obavezan");
        }

        String text = value.toString().trim();
        if (!text.equalsIgnoreCase("Muški") && !text.equalsIgnoreCase("Muski")
                && !text.equalsIgnoreCase("Ženski") && !text.equalsIgnoreCase("Zenski")
                && !text.equalsIgnoreCase("M") && !text.equalsIgnoreCase("Ž") && !text.equalsIgnoreCase("Z")) {
            throw new Exception("Pol mora biti 'Muški' ili 'Ženski' (M/Ž)");
        }
    }
}
