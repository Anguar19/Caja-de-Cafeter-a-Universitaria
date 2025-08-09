/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;
import dominio.Usuario;
import infrastructura.UsuarioRepository;
import utilidades.HashUtil;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class AuthService {
    private static AuthService instance;
    private Usuario usuarioActual;
    private int intentosFallidos = 0;
    private static final int MAX_INTENTOS = 3;
    
    private AuthService() {}
    
    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }
    
    public boolean login(String username, String password) throws Exception {
        try {
            if (intentosFallidos >= MAX_INTENTOS) {
                LogService.getInstance().logWarning("Máximo de intentos alcanzado para: " + username);
                throw new Exception("Máximo de intentos alcanzado. La cuenta ha sido bloqueada temporalmente.");
            }
            
            Usuario usuario = UsuarioRepository.getInstance().findByUsername(username);
            
            if (usuario == null) {
                intentosFallidos++;
                LogService.getInstance().logWarning("Usuario no encontrado: " + username);
                throw new Exception("Usuario o contraseña incorrectos");
            }
            
            if (!HashUtil.verifyPassword(password, usuario.getPassword())) {
                intentosFallidos++;
                LogService.getInstance().logWarning("Contraseña incorrecta para: " + username);
                throw new Exception("Usuario o contraseña incorrectos");
            }
            
            usuarioActual = usuario;
            intentosFallidos = 0;
            LogService.getInstance().logInfo("Login exitoso: " + username);
            return true;
            
        } catch (Exception e) {
            LogService.getInstance().logError("Error en login", e);
            throw e;
        }
    }
    
    public void logout() {
        if (usuarioActual != null) {
            LogService.getInstance().logInfo("Logout: " + usuarioActual.getUsername());
            usuarioActual = null;
        }
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public void resetIntentos() {
        intentosFallidos = 0;
    }
}
