/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import servicio.AuthService;
import servicio.LogService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JMenuBar menuBar;
    private JLabel lblUsuario;
    private JLabel lblFecha;
    
    public MainFrame() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Cafetería Universitaria - Sistema Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Crear menú
        createMenuBar();
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel de bienvenida
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(Color.WHITE);
        
        JLabel lblBienvenida = new JLabel("SISTEMA DE CAFETERÍA UNIVERSITARIA");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 24));
        lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitulo = new JLabel("Gestión de Ventas y Productos");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        welcomePanel.add(Box.createVerticalStrut(100));
        welcomePanel.add(lblBienvenida);
        welcomePanel.add(Box.createVerticalStrut(20));
        welcomePanel.add(lblSubtitulo);
        
        mainPanel.add(welcomePanel, BorderLayout.CENTER);
        
        // Panel de estado
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        
        lblUsuario = new JLabel("Usuario: " + 
            AuthService.getInstance().getUsuarioActual().getUsername());
        lblFecha = new JLabel("Fecha: " + 
            new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
        
        statusPanel.add(lblUsuario, BorderLayout.WEST);
        statusPanel.add(lblFecha, BorderLayout.EAST);
        
        add(mainPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        
        setSize(800, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    private void createMenuBar() {
        menuBar = new JMenuBar();
        
        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemNuevaVenta = new JMenuItem("Nueva Venta");
        JMenuItem itemSalir = new JMenuItem("Salir");
        
        itemNuevaVenta.addActionListener(e -> abrirVentaFrame());
        itemSalir.addActionListener(e -> salir());
        
        menuArchivo.add(itemNuevaVenta);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        
        // Menú Productos
        JMenu menuProductos = new JMenu("Productos");
        JMenuItem itemGestionProductos = new JMenuItem("Gestión de Productos");
        JMenuItem itemListaProductos = new JMenuItem("Lista de Productos");
        
        itemGestionProductos.addActionListener(e -> abrirGestionProductos());
        itemListaProductos.addActionListener(e -> mostrarListaProductos());
        
        menuProductos.add(itemGestionProductos);
        menuProductos.add(itemListaProductos);
        
        // Menú Ventas
        JMenu menuVentas = new JMenu("Ventas");
        JMenuItem itemHistorialVentas = new JMenuItem("Historial de Ventas");
        JMenuItem itemVentasDelDia = new JMenuItem("Ventas del Día");
        
        itemHistorialVentas.addActionListener(e -> abrirHistorialVentas());
        itemVentasDelDia.addActionListener(e -> mostrarVentasDelDia());
        
        menuVentas.add(itemHistorialVentas);
        menuVentas.add(itemVentasDelDia);
        
        // Menú Herramientas
        JMenu menuHerramientas = new JMenu("Herramientas");
        JMenuItem itemCalculadora = new JMenuItem("Calculadora");
        
        itemCalculadora.addActionListener(e -> abrirCalculadora());
        
        menuHerramientas.add(itemCalculadora);
        
        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca de...");
        
        itemAcerca.addActionListener(e -> mostrarAcercaDe());
        
        menuAyuda.add(itemAcerca);
        
        // Agregar menús a la barra
        menuBar.add(menuArchivo);
        menuBar.add(menuProductos);
        menuBar.add(menuVentas);
        menuBar.add(menuHerramientas);
        menuBar.add(menuAyuda);
        
        setJMenuBar(menuBar);
    }
    
    private void abrirVentaFrame() {
        VentaFrame ventaFrame = new VentaFrame();
        ventaFrame.setVisible(true);
    }
    
    private void abrirGestionProductos() {
        ProductoDialog dialog = new ProductoDialog(this);
        dialog.setVisible(true);
    }
    
    private void mostrarListaProductos() {
        try {
            java.util.List< dominio.Producto> productos = 
                 servicio.ProductoService.getInstance().obtenerTodosLosProductos();
            
            StringBuilder sb = new StringBuilder("LISTA DE PRODUCTOS ACTIVOS\n");
            sb.append("========================================\n");
            
            // Usando do-while
            if (!productos.isEmpty()) {
                int i = 0;
                do {
                     dominio.Producto p = productos.get(i);
                    sb.append(String.format("%d. %s - $%.2f\n", 
                        i+1, p.getNombre(), p.getPrecioUnitario()));
                    i++;
                } while (i < productos.size());
            }
            
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, 
                "Lista de Productos", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            LogService.getInstance().logError("Error al mostrar productos", e);
            JOptionPane.showMessageDialog(this, 
                "Error al cargar productos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirHistorialVentas() {
        HistorialVentasFrame historial = new HistorialVentasFrame();
        historial.setVisible(true);
    }
    
    private void mostrarVentasDelDia() {
        try {
            java.util.List< dominio.Venta> ventas = 
                 servicio.VentaService.getInstance().obtenerVentasDelDia();
            
            double totalDia =  servicio.VentaService.getInstance()
                .calcularTotalVentasDelDia();
            
            String mensaje = String.format("Ventas del día: %d\nTotal vendido: $%.2f", 
                ventas.size(), totalDia);
            
            JOptionPane.showMessageDialog(this, mensaje, 
                "Ventas del Día", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            LogService.getInstance().logError("Error al mostrar ventas del día", e);
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirCalculadora() {
        CalculadoraDialog calc = new CalculadoraDialog(this);
        calc.setVisible(true);
    }
    
    private void mostrarAcercaDe() {
        String mensaje = "Sistema de Cafetería Universitaria\n" +
                        "Versión 1.0.0\n" +
                        "Desarrollado para la gestión de ventas\n" +
                        "© 2025 Universidad";
        
        JOptionPane.showMessageDialog(this, mensaje, 
            "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void salir() {
        int option = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea salir?", 
            "Confirmar Salida", 
            JOptionPane.YES_NO_OPTION);
            
        if (option == JOptionPane.YES_OPTION) {
            AuthService.getInstance().logout();
            System.exit(0);
        }
    }
}