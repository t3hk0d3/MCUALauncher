package ru.tehkode.mualauncher;

import ru.tehkode.mualauncher.gui.LauncherWindow;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ru.tehkode.mualauncher.updater.LauncherVersion;
import ru.tehkode.mualauncher.utils.Logger;
import ru.tehkode.mualauncher.utils.PlatformUtils;
import ru.tehkode.mualauncher.utils.Resources;

public class Launcher {

    public static void main(String[] args) throws Exception {
        System.setProperty("http.agent", "MUALauncher/1.0");

        final File currentPath = PlatformUtils.getApplicationPath("mualauncher");
        final LauncherOptions options = new LauncherOptions(new File(currentPath, "launcher.options"));

        // initalize logger output        
        Logger.initialize(new File(currentPath, "launcher.log"));

        Resources.initialize(options);

        Logger.info("Loading MUALauncher version '%s'", LauncherVersion.getLocalVersion().toString());


        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        //If shaped windows aren't supported, exit.
        if (!gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT)) {
            Logger.error("Shaped windows are not supported");
        }

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //
        }

        // check new version
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                } catch (Throwable e) {
                }
            }
        });

        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    JFrame w = new LauncherWindow(currentPath, options);
                    //w.setSize(300, 300);
                    //w.setLocation(500, 100);

                    w.setVisible(true);
                } catch (Throwable e) {
                    Logger.error("Laucher Error: (%s) %s", e.getClass().getSimpleName(), e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    }
}
