package ru.tehkode.mualauncher;

import java.io.File;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import ru.tehkode.mualauncher.session.UserSession;

/**
 *
 * @author t3hk0d3
 */
public class MinecraftLauncherTest {
    
    private MinecraftLauncher instance;
    
    @Before
    public void setup() {
        LauncherOptions options = new LauncherOptions(null);
        
        instance = new MinecraftLauncher(new File("Minecraft"), options);
        
    }

    /**
     * Test of launchMinecraft method, of class MinecraftLauncher.
     */
    @Test
    public void testLaunchMinecraft() {
        
    }

    /**
     * Test of getJavaBinary method, of class MinecraftLauncher.
     */
    @Test
    public void testGetJavaBinary() {
        System.out.println(instance.getJavaBinary());
        
    }


}
