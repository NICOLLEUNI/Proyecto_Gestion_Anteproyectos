/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.service;

/**
 *
 * @author User
 */
public class UserService {
    public void saveUser(){}
    public void aunthenticateUser(){}
    public void hashPassword(){}
    public void validatePassword(){}
    public void validateEmail(){}
    private boolean isEmailUnicauca(String email) {
    return email != null && email.toLowerCase().endsWith("@unicauca.edu.co");
}

    private boolean isStrongPassword(String pwd) {
    // ≥6, al menos un dígito, un especial, una mayúscula
    return pwd != null && pwd.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{6,}$");
}
}
