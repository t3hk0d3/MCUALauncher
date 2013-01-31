package ru.tehkode.mualauncher.updater;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author t3hk0d3
 */
public class LauncherVersionTest {
    
    public LauncherVersionTest() {
    }

    @Test
    public void testLocalVersion() {
        
        LauncherVersion version = LauncherVersion.getLocalVersion();
        
        assertNotNull(version);        
        assertNull(version.getSource()); // local version source should be always null
    }
}
