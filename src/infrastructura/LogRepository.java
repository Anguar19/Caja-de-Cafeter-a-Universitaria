/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructura;
import java.sql.*;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class LogRepository {
    private static LogRepository instance;
    
    private LogRepository() {}
    
    public static LogRepository getInstance() {
        if (instance == null) {
            instance = new LogRepository();
        }
        return instance;
    }
    
    public void saveLog(String nivel, String evento, String detalle, String stacktrace) {
        String sql = "INSERT INTO LOGS (fecha_hora, nivel, evento, detalle, stacktrace) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(2, nivel);
            pstmt.setString(3, evento);
            pstmt.setString(4, detalle);
            pstmt.setString(5, stacktrace);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // En caso de error al guardar log
        }
    }
}
