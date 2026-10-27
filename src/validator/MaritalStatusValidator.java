/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validator;

import java.util.Arrays;
import java.util.List;

/**
 * @author student2
 */
public class MaritalStatusValidator implements MyValidator {

    private static final List<String> VALID_STATUSES = Arrays.asList(
            "neoženjen / neudata", "neozenjen / neudata", "neoženjen", "neudata", "neozenjen",
            "oženjen / udata", "ozenjen / udata", "oženjen", "udata", "ozenjen",
            "razveden / razvedena", "razveden", "razvedena"
            
    );

    @Override
    public void validate(Object value) throws Exception {
        if (value == null || value.toString().trim().isEmpty()) {
            throw new Exception("Bračni status je obavezan");
        }

        String text = value.toString().trim().replaceAll("\\s+", " ").toLowerCase();

        boolean isValid = false;
        for (String validStatus : VALID_STATUSES) {
            if (text.equalsIgnoreCase(validStatus)) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new Exception("Bracni status mora biti: Neozenjen/Neudata, Ozenjen/Udata, Razveden/Razvedena ");
        }
    }
}
