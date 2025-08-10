/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import ui.LoginFrame;
import infrastructura.DatabaseConnection;
import servicio.LogService;
import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LogService.getInstance().logError("Error al configurar Look and Feel", e);
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseConnection.getInstance().testConnection();
                LogService.getInstance().logInfo("Aplicación iniciada correctamente");
                new LoginFrame().setVisible(true);
            } catch (Exception e) {
                LogService.getInstance().logError("Error al iniciar aplicación", e);
                JOptionPane.showMessageDialog(null, 
                    "Error al conectar con la base de datos", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}