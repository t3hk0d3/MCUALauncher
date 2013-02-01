package ru.tehkode.mualauncher.updater;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.URLConnection;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import ru.tehkode.mualauncher.net.ConnectionFactory;
import ru.tehkode.mualauncher.net.Downloader;

/**
 *
 * @author t3hk0d3
 */
public class LauncherUpdaterTest {

    private static final String VERSION_URL = "http://test.host/";
    private static final String UPDATE_URL = "http://test.host/source";
    
    private LauncherVersion remoteVersion = new LauncherVersion("1.2.3", 10, UPDATE_URL);
    
    public LauncherUpdaterTest() throws Exception {
        URL versionURL = new URL(VERSION_URL);

        ConnectionFactory factory = mock(ConnectionFactory.class);       
        
        URLConnection versionConnection = mock(URLConnection.class);

        stub(factory.openConnection(versionURL)).toReturn(versionConnection);
        stub(versionConnection.getInputStream()).toReturn(new ByteArrayInputStream(remoteVersion.toJson().getBytes()));

        Downloader.defaultConnectionFactory = factory;
    }

    @Test
    public void testRemoveVersionFetch() throws Exception {        
        LauncherUpdater updater = new LauncherUpdater(VERSION_URL);

        assertEquals(remoteVersion, updater.fetchRemoteVersion());
    }
    
    @Test
    public void testUpdateDownload() throws Exception {        
        LauncherUpdater updater = new LauncherUpdater(VERSION_URL);

        assertEquals(remoteVersion, updater.fetchRemoteVersion());
    }
    
    
}
