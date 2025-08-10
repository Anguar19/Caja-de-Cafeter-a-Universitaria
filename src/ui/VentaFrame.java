/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
// ==================== VentaFrame.java ====================
package ui;

import dominio.*;
import servicio.*;
import utilidades.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.util.List;

public class VentaFrame extends JFrame {
    private JComboBox<Producto> cmbProductos;
    private JSpinner spnCantidad;
    private JButton btnAgregar;
    private JButton btnEliminar;
    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;
    private JTextField txtDescuento;
    private JLabel lblSubtotal;
    private JLabel lblIVA;
    private JLabel lblIVI;
    private JLabel lblDescuento;
    private JLabel lblTotal;
    private JButton btnProcesarVenta;
    private JButton btnCancelar;
    
    private Venta ventaActual;
    
    public VentaFrame() {
        ventaActual = new Venta();
        ventaActual.setUserId(AuthService.getInstance().getUsuarioActual().getId());
        initComponents();
        cargarProductos();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Nueva Venta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel superior - Selección de productos
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Agregar Productos"));
        
        panelSuperior.add(new JLabel("Producto:"));
        cmbProductos = new JComboBox<>();
        cmbProductos.setPreferredSize(new Dimension(250, 25));
        panelSuperior.add(cmbProductos);
        
        panelSuperior.add(new JLabel("Cantidad:"));
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spnCantidad.setPreferredSize(new Dimension(60, 25));
        panelSuperior.add(spnCantidad);
        
        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarProducto());
        panelSuperior.add(btnAgregar);
        
        // Panel central - Tabla de detalles
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Detalle de Venta"));
        
        String[] columnas = {"Producto", "Cantidad", "Precio Unit.", "Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaDetalles = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaDetalles);
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        
        JPanel panelBotonesTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEliminar = new JButton("Eliminar Seleccionado");
        btnEliminar.addActionListener(e -> eliminarDetalle());
        panelBotonesTabla.add(btnEliminar);
        panelCentral.add(panelBotonesTabla, BorderLayout.SOUTH);
        
        // Panel inferior - Totales
        JPanel panelInferior = new JPanel(new BorderLayout());
        
        JPanel panelTotales = new JPanel(new GridBagLayout());
        panelTotales.setBorder(BorderFactory.createTitledBorder("Resumen"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.EAST;
        
        // Subtotal
        gbc.gridx = 0; gbc.gridy = 0;
        panelTotales.add(new JLabel("Subtotal:"), gbc);
        gbc.gridx = 1;
        lblSubtotal = new JLabel("$0.00");
        panelTotales.add(lblSubtotal, gbc);
        
        // IVA
        gbc.gridx = 0; gbc.gridy = 1;
        panelTotales.add(new JLabel("IVA (7%):"), gbc);
        gbc.gridx = 1;
        lblIVA = new JLabel("$0.00");
        panelTotales.add(lblIVA, gbc);
        
        // IVI
        gbc.gridx = 0; gbc.gridy = 2;
        panelTotales.add(new JLabel("IVI (13%):"), gbc);
        gbc.gridx = 1;
        lblIVI = new JLabel("$0.00");
        panelTotales.add(lblIVI, gbc);
        
        // Descuento
        gbc.gridx = 0; gbc.gridy = 3;
        panelTotales.add(new JLabel("Descuento:"), gbc);
        gbc.gridx = 1;
        JPanel panelDescuento = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        txtDescuento = new JTextField("0", 8);
        txtDescuento.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizarTotales();
            }
        });
        panelDescuento.add(txtDescuento);
        lblDescuento = new JLabel(" ($0.00)");
        panelDescuento.add(lblDescuento);
        panelTotales.add(panelDescuento, gbc);
        
        // Total
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblTotalLabel = new JLabel("TOTAL:");
        lblTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panelTotales.add(lblTotalLabel, gbc);
        gbc.gridx = 1;
        lblTotal = new JLabel("$0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        panelTotales.add(lblTotal, gbc);
        
        panelInferior.add(panelTotales, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnProcesarVenta = new JButton("Procesar Venta");
        btnCancelar = new JButton("Cancelar");
        
        btnProcesarVenta.addActionListener(e -> procesarVenta());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnProcesarVenta);
        panelBotones.add(btnCancelar);
        
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        
        // Agregar paneles al frame
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
        
        setSize(700, 600);
    }
    
    private void cargarProductos() {
        try {
            List<Producto> productos = ProductoService.getInstance().obtenerTodosLosProductos();
            for (Producto p : productos) {
                if (p.isActivo()) {
                    cmbProductos.addItem(p);
                }
            }
        } catch (Exception e) {
            LogService.getInstance().logError("Error al cargar productos", e);
            JOptionPane.showMessageDialog(this, 
                "Error al cargar productos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void agregarProducto() {
        Producto productoSeleccionado = (Producto) cmbProductos.getSelectedItem();
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un producto", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int cantidad = (int) spnCantidad.getValue();
        
        // Verificar si el producto ya está en la lista
        boolean encontrado = false;
        for (DetalleVenta detalle : ventaActual.getDetalles()) {
            if (detalle.getProductoId() == productoSeleccionado.getId()) {
                detalle.setCantidad(detalle.getCantidad() + cantidad);
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) {
            DetalleVenta nuevoDetalle = new DetalleVenta(
                productoSeleccionado.getId(),
                productoSeleccionado.getNombre(),
                cantidad,
                productoSeleccionado.getPrecioUnitario()
            );
            ventaActual.getDetalles().add(nuevoDetalle);
        }
        
        actualizarTabla();
        actualizarTotales();
        
        // Reset spinner
        spnCantidad.setValue(1);
    }
    
    private void eliminarDetalle() {
        int filaSeleccionada = tablaDetalles.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un producto para eliminar", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ventaActual.getDetalles().remove(filaSeleccionada);
        actualizarTabla();
        actualizarTotales();
    }
    
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (DetalleVenta detalle : ventaActual.getDetalles()) {
            Object[] fila = {
                detalle.getProductoNombre(),
                detalle.getCantidad(),
                FormatoUtil.formatearMoneda(detalle.getPrecioUnitario()),
                FormatoUtil.formatearMoneda(detalle.getTotalLinea())
            };
            modeloTabla.addRow(fila);
        }
    }
    
    private void actualizarTotales() {
        try {
            double descuento = 0;
            String descuentoStr = txtDescuento.getText().trim();
            if (!descuentoStr.isEmpty() && Validador.esNumeroPositivo(descuentoStr)) {
                descuento = Double.parseDouble(descuentoStr);
            }
            
            ventaActual.setDescuento(descuento);
            ventaActual.calcularTotales();
            
            lblSubtotal.setText(FormatoUtil.formatearMoneda(ventaActual.getSubtotal()));
            lblIVA.setText(FormatoUtil.formatearMoneda(ventaActual.getImpuestoIVA()));
            lblIVI.setText(FormatoUtil.formatearMoneda(ventaActual.getImpuestoIVI()));
            lblDescuento.setText(" (" + FormatoUtil.formatearMoneda(ventaActual.getDescuento()) + ")");
            lblTotal.setText(FormatoUtil.formatearMoneda(ventaActual.getTotal()));
            
        } catch (Exception e) {
            LogService.getInstance().logError("Error al actualizar totales", e);
        }
    }
    
    private void procesarVenta() {
        try {
            if (ventaActual.getDetalles().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Debe agregar al menos un producto", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Confirmar venta por " + FormatoUtil.formatearMoneda(ventaActual.getTotal()) + "?",
                "Confirmar Venta",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                Venta ventaProcesada = VentaService.getInstance().procesarVenta(ventaActual);
                
                // Generar ticket
                String ticket = FormatoUtil.generarTicket(ventaProcesada);
                
                // Mostrar ticket
                JTextArea textArea = new JTextArea(ticket);
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 400));
                
                int opcion = JOptionPane.showConfirmDialog(this,
                    scrollPane,
                    "Venta Procesada - ¿Guardar ticket?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
                
                if (opcion == JOptionPane.YES_OPTION) {
                    guardarTicket(ticket, ventaProcesada.getId());
                }
                
                dispose();
            }
            
        } catch (Exception e) {
            LogService.getInstance().logError("Error al procesar venta", e);
            JOptionPane.showMessageDialog(this, 
                "Error al procesar venta: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void guardarTicket(String ticket, int ventaId) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("ticket_" + ventaId + ".txt"));
            
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                    writer.write(ticket);
                }
                JOptionPane.showMessageDialog(this, 
                    "Ticket guardado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            LogService.getInstance().logError("Error al guardar ticket", e);
            JOptionPane.showMessageDialog(this, 
                "Error al guardar ticket: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}