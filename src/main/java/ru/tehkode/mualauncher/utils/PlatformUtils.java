package ru.tehkode.mualauncher.utils;

import java.io.File;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author t3hk0d3
 */
public class PlatformUtils {
    
    public enum Platform { WINDOWS, MACOSX, LINUX, OTHER };
    
    public final static Platform currentPlatform;
    
    static {
        String os = System.getProperty("os.name").toLowerCase();
        
        if(os.contains("win")) {
            currentPlatform = Platform.WINDOWS;
        } else if (os.contains("mac")) {
            currentPlatform = Platform.MACOSX;
        } else if (os.contains("nux")) {
            currentPlatform = Platform.LINUX;
        } else  {
            currentPlatform = Platform.OTHER;
        }
    }
    
    public static File getApplicationPath(String appName) {
        switch(currentPlatform) {
            case WINDOWS:
                return new File(System.getenv("APPDATA"), appName);
            case MACOSX:
                return FileUtils.getFile(System.getProperty("user.home"), "Library", "Application Support", appName);
            case LINUX:
                return new File(System.getProperty("user.home"), "." + appName);
            default:
                return new File(appName).getAbsoluteFile(); // relative to current directory
        }        
    }
    
}