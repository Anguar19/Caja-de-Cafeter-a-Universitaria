/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
// ==================== CalculadoraDialog.java ====================
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculadoraDialog extends JDialog implements ActionListener {
    private JTextField txtDisplay;
    private double num1 = 0;
    private double num2 = 0;
    private String operacion = "";
    private boolean nuevaOperacion = true;
    
    public CalculadoraDialog(Frame parent) {
        super(parent, "Calculadora", true);
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Display
        txtDisplay = new JTextField("0");
        txtDisplay.setEditable(false);
        txtDisplay.setHorizontalAlignment(JTextField.RIGHT);
        txtDisplay.setFont(new Font("Arial", Font.BOLD, 20));
        txtDisplay.setPreferredSize(new Dimension(250, 50));
        add(txtDisplay, BorderLayout.NORTH);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(6, 4, 5, 5));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Botones
        String[] botones = {
            "C", "CE", "^", "%",
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "MOD", "", "", ""
        };
        
        for (String texto : botones) {
            if (!texto.isEmpty()) {
                JButton btn = new JButton(texto);
                btn.setFont(new Font("Arial", Font.BOLD, 14));
                btn.addActionListener(this);
                
                // Colores especiales
                if (texto.matches("[0-9.]")) {
                    btn.setBackground(Color.WHITE);
                } else if (texto.equals("=")) {
                    btn.setBackground(new Color(100, 200, 100));
                } else if (texto.matches("[+\\-*/^%]|MOD")) {
                    btn.setBackground(new Color(200, 200, 200));
                } else {
                    btn.setBackground(new Color(255, 200, 200));
                }
                
                panelBotones.add(btn);
            } else {
                panelBotones.add(new JLabel(""));
            }
        }
        
        add(panelBotones, BorderLayout.CENTER);
        
        setSize(300, 400);
        setResizable(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        
        try {
            switch (comando) {
                case "C":
                    limpiarTodo();
                    break;
                    
                case "CE":
                    txtDisplay.setText("0");
                    nuevaOperacion = true;
                    break;
                    
                case "=":
                    calcular();
                    break;
                    
                case "+":
                case "-":
                case "*":
                case "/":
                case "^":
                case "%":
                case "MOD":
                    setOperacion(comando);
                    break;
                    
                case ".":
                    agregarPunto();
                    break;
                    
                default: // Números
                    agregarNumero(comando);
                    break;
            }
        } catch (Exception ex) {
            txtDisplay.setText("Error");
            nuevaOperacion = true;
        }
    }
    
    private void limpiarTodo() {
        txtDisplay.setText("0");
        num1 = 0;
        num2 = 0;
        operacion = "";
        nuevaOperacion = true;
    }
    
    private void agregarNumero(String numero) {
        if (nuevaOperacion) {
            txtDisplay.setText(numero);
            nuevaOperacion = false;
        } else {
            String actual = txtDisplay.getText();
            if (actual.equals("0")) {
                txtDisplay.setText(numero);
            } else {
                txtDisplay.setText(actual + numero);
            }
        }
    }
    
    private void agregarPunto() {
        String actual = txtDisplay.getText();
        if (!actual.contains(".")) {
            txtDisplay.setText(actual + ".");
            nuevaOperacion = false;
        }
    }
    
    private void setOperacion(String op) {
        if (!operacion.isEmpty()) {
            calcular();
        }
        
        num1 = Double.parseDouble(txtDisplay.getText());
        operacion = op;
        nuevaOperacion = true;
    }
    
    private void calcular() {
        if (operacion.isEmpty()) {
            return;
        }
        
        num2 = Double.parseDouble(txtDisplay.getText());
        double resultado = 0;
        
        // Usar switch para las operaciones
        switch (operacion) {
            case "+":
                resultado = num1 + num2;
                break;
            case "-":
                resultado = num1 - num2;
                break;
            case "*":
                resultado = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    JOptionPane.showMessageDialog(this, 
                        "No se puede dividir entre cero", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    limpiarTodo();
                    return;
                }
                resultado = num1 / num2;
                break;
            case "^":
                resultado = Math.pow(num1, num2);
                break;
            case "%":
                resultado = (num1 * num2) / 100;
                break;
            case "MOD":
                resultado = num1 % num2;
                break;
        }
        
        // Formatear resultado
        if (resultado == (int) resultado) {
            txtDisplay.setText(String.valueOf((int) resultado));
        } else {
            txtDisplay.setText(String.format("%.2f", resultado));
        }
        
        num1 = resultado;
        operacion = "";
        nuevaOperacion = true;
    }
}

