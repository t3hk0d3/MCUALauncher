package ru.tehkode.mualauncher.tools;

import ru.tehkode.mualauncher.updater.VersionManager;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author t3hk0d3
 */
public class VersionManagerTest {
    private File tempDirectory;
    
    private VersionManager instance;

    @Before
    public void setup() throws Exception {
        tempDirectory = File.createTempFile("test_versionmanager", "");
       
       tempDirectory.delete();
       
       tempDirectory.mkdir();
              
       instance = new VersionManager(tempDirectory.getAbsoluteFile(), "http://minecraft.com.ua/dl/mods/", true);        
    }
    
    @After
    public void teardown() throws Exception {
        FileUtils.deleteDirectory(tempDirectory);        
    }
 
    @Test
    public void testVersionCheck() throws Exception {
        // skip this test
        /**
        instance.checkForUpdates();
        
        Assert.assertTrue((new File(tempDirectory, "version")).exists());
        **/
    }
}
