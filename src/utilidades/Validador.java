/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class Validador {
    public static boolean esTextoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
    
    public static boolean esNumeroPositivo(String texto) {
        try {
            double num = Double.parseDouble(texto);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean esEnteroPositivo(String texto) {
        try {
            int num = Integer.parseInt(texto);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean esUsuarioValido(String username) {
        return esTextoValido(username) && username.length() >= 3;
    }
    
    public static boolean esPasswordValida(String password) {
        return esTextoValido(password) && password.length() >= 6;
    }
}
