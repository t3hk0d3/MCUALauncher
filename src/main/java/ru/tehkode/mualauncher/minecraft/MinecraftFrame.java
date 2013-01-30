package ru.tehkode.mualauncher.minecraft;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import ru.tehkode.mualauncher.utils.Resources;

/**
 *
 * @author t3hk0d3
 */
public class MinecraftFrame extends JFrame {

    private final static Logger logger = Logger.getLogger(MinecraftFrame.class.getCanonicalName());
    private JPanel wrapper;
    private Applet applet;

    public MinecraftFrame() {
        super("Minecraft");

        try {
            this.setIconImage(Resources.image("window_icon"));
        } catch (Throwable e) {
            logger.warning("Failed to setup window icon");
        }

        wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(854, 480));
        wrapper.setLayout(new BorderLayout());
        add(wrapper, BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logger.info("Closing minecraft!");
                
                if (applet != null) {                    
                    applet.stop();
                    applet.destroy();

                    logger.info("Closed minecraft!");
                }
                
                System.out.println("SYSTEM EXIT");
                
                System.exit(0);
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    public void start(final Applet applet) {
        logger.log(Level.INFO, "Starting Minecraft using applet {0}", applet.getClass().getCanonicalName());

        this.applet = applet;

        applet.init();
        wrapper.add(applet, BorderLayout.CENTER);
        validate();
        applet.start();
    }
}
