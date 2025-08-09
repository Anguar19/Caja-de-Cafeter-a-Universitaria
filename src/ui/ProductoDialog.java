/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import dominio.Producto;
import servicio.ProductoService;
import servicio.LogService;
import utilidades.Validador;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductoDialog extends JDialog {
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnActivarDesactivar;
    private JButton btnRefrescar;
    
    public ProductoDialog(Frame parent) {
        super(parent, "Gestión de Productos", true);
        initComponents();
        cargarProductos();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel superior - Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Nuevo Producto"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        
        txtNombre = new JTextField(20);
        gbc.gridx = 1;
        panelFormulario.add(txtNombre, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(new JLabel("Precio:"), gbc);
        
        txtPrecio = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(txtPrecio, gbc);
        
        btnAgregar = new JButton("Agregar Producto");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelFormulario.add(btnAgregar, gbc);
        
        btnAgregar.addActionListener(e -> agregarProducto());
        
        // Panel central - Tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Lista de Productos"));
        
        String[] columnas = {"ID", "Nombre", "Precio", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnEditar = new JButton("Editar");
        btnActivarDesactivar = new JButton("Activar/Desactivar");
        btnRefrescar = new JButton("Refrescar");
        
        btnEditar.addActionListener(e -> editarProducto());
        btnActivarDesactivar.addActionListener(e -> toggleEstadoProducto());
        btnRefrescar.addActionListener(e -> cargarProductos());
        
        panelBotones.add(btnEditar);
        panelBotones.add(btnActivarDesactivar);
        panelBotones.add(btnRefrescar);
        
        panelTabla.add(panelBotones, BorderLayout.SOUTH);
        
        // Agregar paneles al dialog
        add(panelFormulario, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
        
        setSize(600, 500);
    }
    
    private void cargarProductos() {
        try {
            modeloTabla.setRowCount(0);
            List<Producto> productos = ProductoService.getInstance().obtenerTodosLosProductos();
            
            for (Producto p : productos) {
                Object[] fila = {
                    p.getId(),
                    p.getNombre(),
                    String.format("$%.2f", p.getPrecioUnitario()),
                    p.isActivo() ? "Activo" : "Inactivo"
                };
                modeloTabla.addRow(fila);
            }
        } catch (Exception e) {
            LogService.getInstance().logError("Error al cargar productos", e);
            JOptionPane.showMessageDialog(this, 
                "Error al cargar productos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void agregarProducto() {
        try {
            String nombre = txtNombre.getText();
            String precioStr = txtPrecio.getText();
            
            if (!Validador.esTextoValido(nombre)) {
                JOptionPane.showMessageDialog(this, 
                    "El nombre del producto es requerido", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!Validador.esNumeroPositivo(precioStr)) {
                JOptionPane.showMessageDialog(this, 
                    "El precio debe ser un número positivo", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double precio = Double.parseDouble(precioStr);
            ProductoService.getInstance().crearProducto(nombre, precio);
            
            JOptionPane.showMessageDialog(this, 
                "Producto agregado exitosamente", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            txtNombre.setText("");
            txtPrecio.setText("");
            cargarProductos();
            
        } catch (Exception e) {
            LogService.getInstance().logError("Error al agregar producto", e);
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarProducto() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un producto para editar", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombreActual = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            String precioActual = ((String) modeloTabla.getValueAt(filaSeleccionada, 2)).replace("$", "");
            
            JTextField txtNuevoNombre = new JTextField(nombreActual);
            JTextField txtNuevoPrecio = new JTextField(precioActual);
            
            Object[] campos = {
                "Nombre:", txtNuevoNombre,
                "Precio:", txtNuevoPrecio
            };
            
            int resultado = JOptionPane.showConfirmDialog(this, campos, 
                "Editar Producto", JOptionPane.OK_CANCEL_OPTION);
            
            if (resultado == JOptionPane.OK_OPTION) {
                Producto producto = ProductoService.getInstance().obtenerProductoPorId(id);
                producto.setNombre(txtNuevoNombre.getText());
                producto.setPrecioUnitario(Double.parseDouble(txtNuevoPrecio.getText()));
                
                ProductoService.getInstance().actualizarProducto(producto);
                cargarProductos();
                
                JOptionPane.showMessageDialog(this, 
                    "Producto actualizado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            LogService.getInstance().logError("Error al editar producto", e);
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void toggleEstadoProducto() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un producto", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            ProductoService.getInstance().toggleActivoProducto(id);
            cargarProductos();
            
            JOptionPane.showMessageDialog(this, 
                "Estado del producto actualizado", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            LogService.getInstance().logError("Error al cambiar estado del producto", e);
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}