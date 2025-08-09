/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;
import dominio.Venta;
import dominio.DetalleVenta;
import infrastructura.VentaRepository;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class VentaService {
    private static VentaService instance;
    private final VentaRepository repository;
    
    private VentaService() {
        this.repository = VentaRepository.getInstance();
    }
    
    public static VentaService getInstance() {
        if (instance == null) {
            instance = new VentaService();
        }
        return instance;
    }
    
    public Venta procesarVenta(Venta venta) throws Exception {
        try {
            // Validar venta
            if (venta.getDetalles().isEmpty()) {
                throw new Exception("La venta debe tener al menos un producto");
            }
            
            // Calcular totales
            venta.calcularTotales();
            
            // Guardar venta
            venta = repository.save(venta);
            
            LogService.getInstance().logInfo("Venta procesada. ID: " + venta.getId() + 
                                            ", Total: $" + venta.getTotal());
            
            return venta;
            
        } catch (Exception e) {
            LogService.getInstance().logError("Error al procesar venta", e);
            throw e;
        }
    }
    
    public List<Venta> obtenerVentasDelDia() throws Exception {
        try {
            List<Venta> ventas = repository.findByDate(LocalDate.now());
            LogService.getInstance().logInfo("Ventas del día consultadas: " + ventas.size());
            return ventas;
        } catch (Exception e) {
            LogService.getInstance().logError("Error al obtener ventas del día", e);
            throw new Exception("Error al obtener las ventas del día: " + e.getMessage());
        }
    }
    
    public double calcularTotalVentasDelDia() throws Exception {
        List<Venta> ventas = obtenerVentasDelDia();
        double total = 0;
        
        // Usando for tradicional
        for (int i = 0; i < ventas.size(); i++) {
            total += ventas.get(i).getTotal();
        }
        
        return total;
    }
    
    public List<DetalleVenta> obtenerProductosVendidosHoy() throws Exception {
        try {
            List<Venta> ventasHoy = obtenerVentasDelDia();
            List<DetalleVenta> todosLosDetalles = new java.util.ArrayList<>();
            
            // Usando while
            int index = 0;
            while (index < ventasHoy.size()) {
                todosLosDetalles.addAll(ventasHoy.get(index).getDetalles());
                index++;
            }
            
            return todosLosDetalles;
        } catch (Exception e) {
            LogService.getInstance().logError("Error al obtener productos vendidos", e);
            throw e;
        }
    }
}
