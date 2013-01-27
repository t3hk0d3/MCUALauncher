package ru.tehkode.mualauncher;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.commons.io.FileUtils;
import ru.tehkode.mualauncher.utils.Logger;

/**
 *
 * @author t3hk0d3
 */
public class Minecraft {

    public static void main(String[] args) {

        File basePath = new File(args[0]);
        String login = args[1], sessionId = args[2];

        try {
            Class<?> minecraftClass = Class.forName("net.minecraft.client.Minecraft");

            // Setup properties
            String nativeLibrariesPath = FileUtils.getFile(basePath, "minecraft", "bin", "natives").toString();
            
            Logger.info("Native libraries located in '%s'", nativeLibrariesPath);
            
            System.setProperty("java.library.path", nativeLibrariesPath);
            System.setProperty("org.lwjgl.librarypath", nativeLibrariesPath);
            System.setProperty("net.java.games.input.librarypath", nativeLibrariesPath);
            
            System.setProperty("user.home", basePath.getAbsolutePath());

            injectNewBasepath(minecraftClass, new File(basePath, "minecraft"));
            
            String[] minecraftArgs = new String[] { login, sessionId };
            
            Method minecraftEntryPoint = minecraftClass.getMethod("main", String[].class);

            minecraftEntryPoint.invoke(null, (Object)minecraftArgs);
        } catch (Throwable e) {
            Logger.error("Something awful happens - %s", e.getMessage());
            e.printStackTrace();
        }
    }

    private static void injectNewBasepath(Class<?> minecraftClass, File basePath) {
        for (Field field : minecraftClass.getDeclaredFields()) {
            try {
                if (field.getType() != File.class || (field.getModifiers() & (Modifier.PRIVATE | Modifier.STATIC)) == 0) {
                    continue;
                }

                field.setAccessible(true);
                field.set(null, basePath);
                
                Logger.info("Patched minecraft path field '%s'", field.getName());
            } catch (IllegalAccessException e) {
                Logger.warning("IllegalAccessException - %s", e.getMessage());
            }
        }

    }
}
