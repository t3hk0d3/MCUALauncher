package ru.tehkode.mualauncher;

import java.applet.Applet;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import ru.tehkode.mualauncher.minecraft.MinecraftAppletWrapper;
import ru.tehkode.mualauncher.minecraft.MinecraftFrame;
import ru.tehkode.mualauncher.minecraft.MinecraftProperties;
import ru.tehkode.mualauncher.utils.Logger;
import ru.tehkode.mualauncher.utils.Resources;

/**
 *
 * @author t3hk0d3
 */
public class Minecraft {

    public static void main(String[] args) {
        MinecraftProperties properties = new MinecraftProperties(args);

        File basePath = properties.getBasePath();

        // Needed for localization options
        final LauncherOptions options = new LauncherOptions(new File(basePath, "launcher.options"));

        try {
            Resources.initialize(options);

            setupEnvironment(basePath);

            Class<?> minecraftClass = Class.forName("net.minecraft.client.Minecraft");
            injectNewBasepath(minecraftClass, new File(basePath, "minecraft"));

            try {
                launchApplet(properties);
            } catch (Throwable e) {
                Logger.warning("Failed to start applet, fallback to regular launcher!");
                e.printStackTrace();
                Method minecraftEntryPoint = minecraftClass.getMethod("main", String[].class);
                minecraftEntryPoint.invoke(null, (Object) new String[]{properties.getLogin(), properties.getSessionId()});
            }

        } catch (Throwable e) {
            Logger.error("Something awful happens - %s", e.getMessage());
            e.printStackTrace();
        }
    }

    private static void launchApplet(MinecraftProperties properties) throws Exception {
        Class<?> minecraftAppletClass = Class.forName("net.minecraft.client.MinecraftApplet");

        Applet minecraftApplet = (Applet) minecraftAppletClass.newInstance();

        final MinecraftFrame launcherFrame = new MinecraftFrame();
        final MinecraftAppletWrapper wrapper = new MinecraftAppletWrapper(minecraftApplet, properties);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    launcherFrame.setVisible(true);
                    launcherFrame.start(wrapper);
                } catch (Throwable e) {
                    Logger.error("Launch error - ", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private static void setupEnvironment(File basePath) {
        // Setup properties
        String nativeLibrariesPath = FileUtils.getFile(basePath, "minecraft", "bin", "natives").toString();

        Logger.info("Native libraries located in '%s'", nativeLibrariesPath);

        System.setProperty("java.library.path", nativeLibrariesPath);
        System.setProperty("org.lwjgl.librarypath", nativeLibrariesPath);
        System.setProperty("net.java.games.input.librarypath", nativeLibrariesPath);

        System.setProperty("minecraft.applet.WrapperClass", MinecraftAppletWrapper.class.getCanonicalName());

        System.setProperty("user.home", basePath.getAbsolutePath());
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
