/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import dominio.Venta;
import dominio.DetalleVenta;
import servicio.VentaService;
import servicio.LogService;
import utilidades.FormatoUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class HistorialVentasFrame extends JFrame {
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;
    private JTextArea txtDetalles;
    private JButton btnRefrescar;
    private JButton btnVerDetalle;
    private JLabel lblTotalDia;
    
    public HistorialVentasFrame() {
        initComponents();
        cargarVentas();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Historial de Ventas del Día");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Ventas del Día"));
        
        JLabel lblFecha = new JLabel("Fecha: " + LocalDate.now());
        lblFecha.setFont(new Font("Arial", Font.BOLD, 14));
        panelSuperior.add(lblFecha);
        
        panelSuperior.add(Box.createHorizontalStrut(30));
        
        lblTotalDia = new JLabel("Total del día: $0.00");
        lblTotalDia.setFont(new Font("Arial", Font.BOLD, 14));
        panelSuperior.add(lblTotalDia);
        
        // Panel central con split
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        // Tabla de ventas
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Lista de Ventas"));
        
        String[] columnas = {"ID", "Hora", "Subtotal", "IVA", "IVI", "Descuento", "Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaVentas = new JTable(modeloTabla);
        tablaVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalles();
            }
        });
        
        JScrollPane scrollTabla = new JScrollPane(tablaVentas);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVerDetalle = new JButton("Ver Detalle");
        btnRefrescar = new JButton("Refrescar");
        
        btnVerDetalle.addActionListener(e -> mostrarDetalles());
        btnRefrescar.addActionListener(e -> cargarVentas());
        
        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnRefrescar);
        panelTabla.add(panelBotones, BorderLayout.SOUTH);
        
        // Area de detalles
        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalle de Venta"));
        
        txtDetalles = new JTextArea(8, 40);
        txtDetalles.setEditable(false);
        txtDetalles.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollDetalles = new JScrollPane(txtDetalles);
        panelDetalles.add(scrollDetalles, BorderLayout.CENTER);
        
        splitPane.setTopComponent(panelTabla);
        splitPane.setBottomComponent(panelDetalles);
        splitPane.setDividerLocation(300);
        
        // Agregar componentes
        add(panelSuperior, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        
        setSize(800, 600);
    }
    
    private void cargarVentas() {
        try {
            modeloTabla.setRowCount(0);
            List<Venta> ventas = VentaService.getInstance().obtenerVentasDelDia();
            
            double totalDia = 0;
            
            // Usando for mejorado
            for (Venta venta : ventas) {
                Object[] fila = {
                    venta.getId(),
                    venta.getFechaHora().toLocalTime().toString(),
                    FormatoUtil.formatearMoneda(venta.getSubtotal()),
                    FormatoUtil.formatearMoneda(venta.getImpuestoIVA()),
                    FormatoUtil.formatearMoneda(venta.getImpuestoIVI()),
                    FormatoUtil.formatearMoneda(venta.getDescuento()),
                    FormatoUtil.formatearMoneda(venta.getTotal())
                };
                modeloTabla.addRow(fila);
                totalDia += venta.getTotal();
            }
            
            lblTotalDia.setText("Total del día: " + FormatoUtil.formatearMoneda(totalDia));
            
            if (ventas.isEmpty()) {
                txtDetalles.setText("No hay ventas registradas el día de hoy.");
            }
            
        } catch (Exception e) {
            LogService.getInstance().logError("Error al cargar ventas", e);
            JOptionPane.showMessageDialog(this, 
                "Error al cargar ventas: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarDetalles() {
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada < 0) {
            txtDetalles.setText("Seleccione una venta para ver los detalles.");
            return;
        }
        
        try {
            int ventaId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            List<Venta> ventas = VentaService.getInstance().obtenerVentasDelDia();
            
            Venta ventaSeleccionada = null;
            
            // Usando while-do
            int i = 0;
            do {
                if (ventas.get(i).getId() == ventaId) {
                    ventaSeleccionada = ventas.get(i);
                    break;
                }
                i++;
            } while (i < ventas.size());
            
            if (ventaSeleccionada != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("DETALLE DE VENTA #").append(ventaId).append("\n");
                sb.append("=====================================\n");
                sb.append("Fecha: ").append(FormatoUtil.formatearFecha(ventaSeleccionada.getFechaHora())).append("\n");
                sb.append("-------------------------------------\n");
                sb.append("PRODUCTOS:\n");
                
                for (DetalleVenta detalle : ventaSeleccionada.getDetalles()) {
                    sb.append(String.format("- %s x %d = %s\n",
                        detalle.getProductoNombre(),
                        detalle.getCantidad(),
                        FormatoUtil.formatearMoneda(detalle.getTotalLinea())));
                }
                
                sb.append("-------------------------------------\n");
                sb.append("Subtotal: ").append(FormatoUtil.formatearMoneda(ventaSeleccionada.getSubtotal())).append("\n");
                sb.append("IVA (7%): ").append(FormatoUtil.formatearMoneda(ventaSeleccionada.getImpuestoIVA())).append("\n");
                sb.append("IVI (13%): ").append(FormatoUtil.formatearMoneda(ventaSeleccionada.getImpuestoIVI())).append("\n");
                
                if (ventaSeleccionada.getDescuento() > 0) {
                    sb.append("Descuento: ").append(FormatoUtil.formatearMoneda(ventaSeleccionada.getDescuento())).append("\n");
                }
                
                sb.append("=====================================\n");
                sb.append("TOTAL: ").append(FormatoUtil.formatearMoneda(ventaSeleccionada.getTotal())).append("\n");
                
                txtDetalles.setText(sb.toString());
            }
            
        } catch (Exception e) {
            LogService.getInstance().logError("Error al mostrar detalles", e);
            txtDetalles.setText("Error al cargar los detalles de la venta.");
        }
    }
}
