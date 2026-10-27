/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package component;

/**
 *
 * 
 * @author student2
 */
public interface FormInputPanel {
    
    Object getValue() throws Exception;
 
    void clearError();
    
    void reset();

    default void setLabel(String label) {}

    default String getLabel() {
        return "";
    }

    default java.awt.Component getComponent() {
        if (this instanceof java.awt.Component) {
            return (java.awt.Component) this;
        }
        return null;
    }
}
