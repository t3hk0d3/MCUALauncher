package ru.tehkode.mualauncher.net;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author t3hk0d3
 */
public interface ConnectionFactory {
    
    public URLConnection openConnection(URL url) throws IOException;
    
}
