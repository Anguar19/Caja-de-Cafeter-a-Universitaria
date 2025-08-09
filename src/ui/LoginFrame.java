/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import servicio.AuthService;
import servicio.LogService;
import utilidades.Validador;

public class LoginFrame extends javax.swing.JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSalir;

    public LoginFrame() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Cafetería Universitaria - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitulo = new JLabel("SISTEMA DE CAFETERÍA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Usuario:"), gbc);

        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(txtUsername, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(txtPassword, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnLogin = new JButton("Iniciar Sesión");
        btnSalir = new JButton("Salir");

        btnLogin.addActionListener(e -> login());
        btnSalir.addActionListener(e -> System.exit(0));

        getRootPane().setDefaultButton(btnLogin);

        btnPanel.add(btnLogin);
        btnPanel.add(btnSalir);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(btnPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        setSize(400, 300);
        setResizable(false);
    }

    private void login() {
        try {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (!Validador.esUsuarioValido(username)) {
                JOptionPane.showMessageDialog(this,
                        "El usuario debe tener al menos 3 caracteres",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Validador.esPasswordValida(password)) {
                JOptionPane.showMessageDialog(this,
                        "La contraseña debe tener al menos 6 caracteres",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (AuthService.getInstance().login(username, password)) {
                JOptionPane.showMessageDialog(this,
                        "Bienvenido " + username,
                        "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);

                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                this.dispose();
            }

        } catch (Exception e) {
            LogService.getInstance().logError("Error en login", e);
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error de Login", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }

    public static void main(String args[]) {
        /* Código para Look and Feel y mostrar ventana */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}

