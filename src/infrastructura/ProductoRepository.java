/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructura;
import dominio.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class ProductoRepository {
    private static ProductoRepository instance;
    
    private ProductoRepository() {}
    
    public static ProductoRepository getInstance() {
        if (instance == null) {
            instance = new ProductoRepository();
        }
        return instance;
    }
    
    public List<Producto> findAll() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTOS WHERE activo = true ORDER BY nombre";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Producto producto = mapResultSetToProducto(rs);
                productos.add(producto);
            }
        }
        return productos;
    }
    
    public Producto findById(int id) throws SQLException {
        String sql = "SELECT * FROM PRODUCTOS WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProducto(rs);
            }
        }
        return null;
    }
    
    public Producto save(Producto producto) throws SQLException {
        String sql = "INSERT INTO PRODUCTOS (nombre, precio_unitario, activo, creado) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecioUnitario());
            pstmt.setBoolean(3, producto.isActivo());
            pstmt.setTimestamp(4, Timestamp.valueOf(producto.getCreado()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        producto.setId(generatedKeys.getInt(1));
                    }
                }
            }
        }
        return producto;
    }
    
    public void update(Producto producto) throws SQLException {
        String sql = "UPDATE PRODUCTOS SET nombre = ?, precio_unitario = ?, activo = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setDouble(2, producto.getPrecioUnitario());
            pstmt.setBoolean(3, producto.isActivo());
            pstmt.setInt(4, producto.getId());
            
            pstmt.executeUpdate();
        }
    }
    
    public void toggleActive(int id) throws SQLException {
        String sql = "UPDATE PRODUCTOS SET activo = NOT activo WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    
    private Producto mapResultSetToProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setPrecioUnitario(rs.getDouble("precio_unitario"));
        producto.setActivo(rs.getBoolean("activo"));
        producto.setCreado(rs.getTimestamp("creado").toLocalDateTime());
        return producto;
    }
}
