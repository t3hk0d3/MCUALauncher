/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.mualauncher.utils;

/**
 *
 * @author t3hk0d3
 */
public class Logger {
    
    private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("MCLauncher");
    
    public static void info(String message, Object... values) {
        if(values != null) {
            message = String.format(message, values);
        }
        
        logger.info(message);
    }
    
    public static void warning(String message, Object... values) {
        if(values != null) {
            message = String.format(message, values);
        }
        
        logger.warning(message);
    }
    
    public static void error(String message, Object... values) {
        if(values != null) {
            message = String.format(message, values);
        }
        
        logger.severe(message);
    }
    
    
    
    
}
