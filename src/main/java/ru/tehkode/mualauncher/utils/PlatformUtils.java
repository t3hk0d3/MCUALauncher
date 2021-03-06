package ru.tehkode.mualauncher.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import ru.tehkode.mualauncher.Minecraft;

/**
 *
 * @author t3hk0d3
 */
public class PlatformUtils {

    public enum Platform {

        WINDOWS, MACOSX, LINUX, OTHER
    };
    public final static Platform currentPlatform;

    static {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            currentPlatform = Platform.WINDOWS;
        } else if (os.contains("mac")) {
            currentPlatform = Platform.MACOSX;
        } else if (os.contains("nux")) {
            currentPlatform = Platform.LINUX;
        } else {
            currentPlatform = Platform.OTHER;
        }
    }

    public static File getApplicationPath(String appName) {
        switch (currentPlatform) {
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

    public static void openWebpage(String uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI(uri));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static File getJavaBinary() {
        File javaBinaryPath = new File(System.getProperty("java.home"), "bin");
        
        String[] possibleJavaBinaries = new String[] { "javaw.exe", "java.exe", "java" };
        
        for(String binaryName : possibleJavaBinaries) {
            File binaryFile = new File(javaBinaryPath, binaryName);
            
            if(binaryFile.exists() && binaryFile.canExecute()) {
                return binaryFile;
            }
        }
        
        throw new RuntimeException("Can't find java binary!");              
    }
    
    public static Process launchJavaApplication(String classPath, String className, String... args) throws IOException {        
        return launchJavaApplication(classPath, null, className, Arrays.asList(args));
    }
    
    public static Process launchJavaApplication(String classPath, String jvmOptions, String className, List<String> args) throws IOException {
        List<String> arguments = new ArrayList<String>();
        
        arguments.add(getJavaBinary().getAbsolutePath());
        
        if(jvmOptions != null && !jvmOptions.isEmpty()) {
            arguments.addAll(Arrays.asList(jvmOptions.split("\\s+")));
        }
        
        arguments.add("-cp");
        arguments.add(classPath);
        
        arguments.add(className);
        
        arguments.addAll(args);
            
        ProcessBuilder builder = new ProcessBuilder(arguments);
                
        return builder.start();        
    }
    
}
