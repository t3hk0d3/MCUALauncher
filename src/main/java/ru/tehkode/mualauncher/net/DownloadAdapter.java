package ru.tehkode.mualauncher.net;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.List;

/**
 *
 * @author t3hk0d3
 */
public interface DownloadAdapter {
    
    public String getUrl();
    
    public URL getURL();
    
    public void setRequestHeader(String header, String value);
    
    public String getResponseHeader(String header);
    
    public void download(OutputStream stream) throws IOException;
    
    public void download(Writer writer) throws IOException;
    
    public void setDownloadListener(DownloadListener window);
    
    public void downloadToFile(File destination) throws IOException;
    
    public String downloadToString() throws IOException;
    
    public List<String> downloadToLines() throws IOException;
    
}
