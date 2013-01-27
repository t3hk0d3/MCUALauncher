/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.mualauncher.utils;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import ru.tehkode.mualauncher.Launcher;

/**
 *
 * @author t3hk0d3
 */
public class Resources {
    
    public static Image getImage(String name) throws IOException {
        return ImageIO.read(Launcher.class.getResourceAsStream("/" + name));
    }
    
}
