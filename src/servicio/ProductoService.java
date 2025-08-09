/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;
import dominio.Producto;
import infrastructura.ProductoRepository;
import java.util.List;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class ProductoService {
    private static ProductoService instance;
    private final ProductoRepository repository;
    
    private ProductoService() {
        this.repository = ProductoRepository.getInstance();
    }
    
    public static ProductoService getInstance() {
        if (instance == null) {
            instance = new ProductoService();
        }
        return instance;
    }
    
    public List<Producto> obtenerTodosLosProductos() throws Exception {
        try {
            List<Producto> productos = repository.findAll();
            LogService.getInstance().logInfo("Productos consultados: " + productos.size());
            return productos;
        } catch (Exception e) {
            LogService.getInstance().logError("Error al obtener productos", e);
            throw new Exception("Error al obtener los productos: " + e.getMessage());
        }
    }
    
    public Producto obtenerProductoPorId(int id) throws Exception {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            LogService.getInstance().logError("Error al obtener producto por ID: " + id, e);
            throw new Exception("Error al obtener el producto: " + e.getMessage());
        }
    }
    
    public Producto crearProducto(String nombre, double precio) throws Exception {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new Exception("El nombre del producto es requerido");
            }
            
            if (precio <= 0) {
                throw new Exception("El precio debe ser mayor a cero");
            }
            
            Producto producto = new Producto(nombre, precio);
            producto = repository.save(producto);
            LogService.getInstance().logInfo("Producto creado: " + nombre);
            return producto;
            
        } catch (Exception e) {
            LogService.getInstance().logError("Error al crear producto", e);
            throw e;
        }
    }
    
    public void actualizarProducto(Producto producto) throws Exception {
        try {
            repository.update(producto);
            LogService.getInstance().logInfo("Producto actualizado: " + producto.getNombre());
        } catch (Exception e) {
            LogService.getInstance().logError("Error al actualizar producto", e);
            throw new Exception("Error al actualizar el producto: " + e.getMessage());
        }
    }
    
    public void toggleActivoProducto(int id) throws Exception {
        try {
            repository.toggleActive(id);
            LogService.getInstance().logInfo("Estado de producto cambiado. ID: " + id);
        } catch (Exception e) {
            LogService.getInstance().logError("Error al cambiar estado del producto", e);
            throw new Exception("Error al cambiar el estado del producto: " + e.getMessage());
        }
    }
}
