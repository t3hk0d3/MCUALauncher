package ru.tehkode.mualauncher;

import ru.tehkode.mualauncher.gui.LauncherWindow;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ru.tehkode.mualauncher.utils.Logger;
import ru.tehkode.mualauncher.utils.Resources;

/**
 * Hello world!
 *
 */
public class Launcher {

    public static void main(String[] args) {
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        //If shaped windows aren't supported, exit.
        if (!gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT)) {
            System.err.println("Shaped windows are not supported");
        }

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //
        }
        
        Resources.getInstance();

        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    JFrame w = new LauncherWindow();
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
