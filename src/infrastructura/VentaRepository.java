/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructura;
import dominio.Venta;
import dominio.DetalleVenta;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class VentaRepository {
    private static VentaRepository instance;
    
    private VentaRepository() {}
    
    public static VentaRepository getInstance() {
        if (instance == null) {
            instance = new VentaRepository();
        }
        return instance;
    }
    
    public Venta save(Venta venta) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            // Guardar venta
            String sqlVenta = "INSERT INTO VENTAS (user_id, fecha_hora, subtotal, impuestoIVA, impuestoIVI, descuento, total) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, venta.getUserId());
                pstmt.setTimestamp(2, Timestamp.valueOf(venta.getFechaHora()));
                pstmt.setDouble(3, venta.getSubtotal());
                pstmt.setDouble(4, venta.getImpuestoIVA());
                pstmt.setDouble(5, venta.getImpuestoIVI());
                pstmt.setDouble(6, venta.getDescuento());
                pstmt.setDouble(7, venta.getTotal());
                
                int affectedRows = pstmt.executeUpdate();
                
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            venta.setId(generatedKeys.getInt(1));
                        }
                    }
                }
            }
            
            // Guardar detalles
            String sqlDetalle = "INSERT INTO DETALLES_VENTA (venta_id, product_id, cantidad, precio_unit, total_linea) " +
                               "VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDetalle)) {
                for (DetalleVenta detalle : venta.getDetalles()) {
                    pstmt.setInt(1, venta.getId());
                    pstmt.setInt(2, detalle.getProductoId());
                    pstmt.setInt(3, detalle.getCantidad());
                    pstmt.setDouble(4, detalle.getPrecioUnitario());
                    pstmt.setDouble(5, detalle.getTotalLinea());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            
            conn.commit();
            return venta;
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    public List<Venta> findByDate(LocalDate fecha) throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT v.*, u.username FROM VENTAS v " +
                    "JOIN USUARIOS u ON v.user_id = u.id " +
                    "WHERE DATE(v.fecha_hora) = ? " +
                    "ORDER BY v.fecha_hora DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(fecha));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id"));
                venta.setUserId(rs.getInt("user_id"));
                venta.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                venta.setSubtotal(rs.getDouble("subtotal"));
                venta.setImpuestoIVA(rs.getDouble("impuestoIVA"));
                venta.setImpuestoIVI(rs.getDouble("impuestoIVI"));
                venta.setDescuento(rs.getDouble("descuento"));
                venta.setTotal(rs.getDouble("total"));
                
                // Cargar detalles
                venta.setDetalles(findDetallesByVentaId(venta.getId()));
                ventas.add(venta);
            }
        }
        return ventas;
    }
    
    public List<DetalleVenta> findDetallesByVentaId(int ventaId) throws SQLException {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT d.*, p.nombre FROM DETALLES_VENTA d " +
                    "JOIN PRODUCTOS p ON d.product_id = p.id " +
                    "WHERE d.venta_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, ventaId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setId(rs.getInt("id"));
                detalle.setVentaId(rs.getInt("venta_id"));
                detalle.setProductoId(rs.getInt("product_id"));
                detalle.setProductoNombre(rs.getString("nombre"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setPrecioUnitario(rs.getDouble("precio_unit"));
                detalle.setTotalLinea(rs.getDouble("total_linea"));
                detalles.add(detalle);
            }
        }
        return detalles;
    }
}
