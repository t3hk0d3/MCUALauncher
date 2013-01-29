package ru.tehkode.mualauncher.minecraft;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
        
        pack();
        setLocationRelativeTo(null);
    }
    
    public void start(Applet applet) {
        logger.log(Level.INFO, "Starting Minecraft using applet {0}", applet.getClass().getCanonicalName());
        
        applet.init();
        wrapper.add(applet, BorderLayout.CENTER);
        validate();
        applet.start();
    }
}
