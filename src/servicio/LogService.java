/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;
import infrastructura.LogRepository;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class LogService {
     private static LogService instance;
    private final Logger logger;
    private final LogRepository repository;
    
    private LogService() {
        this.logger = Logger.getLogger("CafeteriaApp");
        this.repository = LogRepository.getInstance();
    }
    
    public static LogService getInstance() {
        if (instance == null) {
            instance = new LogService();
        }
        return instance;
    }
    
    public void logInfo(String mensaje) {
        logger.log(Level.INFO, mensaje);
        repository.saveLog("INFO", mensaje, null, null);
    }
    
    public void logWarning(String mensaje) {
        logger.log(Level.WARNING, mensaje);
        repository.saveLog("WARNING", mensaje, null, null);
    }
    
    public void logError(String mensaje, Exception e) {
        logger.log(Level.SEVERE, mensaje, e);
        
        String stackTrace = null;
        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            stackTrace = sw.toString();
        }
        
        repository.saveLog("ERROR", mensaje, e != null ? e.getMessage() : null, stackTrace);
    }
}
