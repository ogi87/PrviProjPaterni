/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validator;

/**
 *
 * @author student2
 */
public class NumberValidator implements MyValidator{

    @Override
    public void validate(Object value) throws Exception {
        try {
            Integer.parseInt(value.toString());
        } catch (Exception e) {
            throw new Exception("Vrednost nije broj");
        }

    }
    
}
