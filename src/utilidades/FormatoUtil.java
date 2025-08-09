/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class FormatoUtil {
    private static final DecimalFormat df = new DecimalFormat("#,##0.00");
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public static String formatearMoneda(double valor) {
        return "$" + df.format(valor);
    }
    
    public static String formatearFecha(LocalDateTime fecha) {
        return fecha.format(dtf);
    }
    
    public static String generarTicket(dominio.Venta venta) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("       CAFETERÍA UNIVERSITARIA         \n");
        sb.append("========================================\n");
        sb.append("Fecha: ").append(formatearFecha(venta.getFechaHora())).append("\n");
        sb.append("Factura #: ").append(String.format("%06d", venta.getId())).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("DETALLE:\n");
        
        for (dominio.DetalleVenta detalle : venta.getDetalles()) {
            sb.append(String.format("%-20s %3d x %8s = %8s\n",
                detalle.getProductoNombre(),
                detalle.getCantidad(),
                formatearMoneda(detalle.getPrecioUnitario()),
                formatearMoneda(detalle.getTotalLinea())));
        }
        
        sb.append("----------------------------------------\n");
        sb.append(String.format("Subtotal:           %20s\n", formatearMoneda(venta.getSubtotal())));
        sb.append(String.format("IVA (7%%):           %20s\n", formatearMoneda(venta.getImpuestoIVA())));
        sb.append(String.format("IVI (13%%):          %20s\n", formatearMoneda(venta.getImpuestoIVI())));
        if (venta.getDescuento() > 0) {
            sb.append(String.format("Descuento:          %20s\n", formatearMoneda(venta.getDescuento())));
        }
        sb.append("========================================\n");
        sb.append(String.format("TOTAL:              %20s\n", formatearMoneda(venta.getTotal())));
        sb.append("========================================\n");
        sb.append("         ¡Gracias por su compra!       \n");
        
        return sb.toString();
    }
}
