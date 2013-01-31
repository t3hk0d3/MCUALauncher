package ru.tehkode.mualauncher.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.input.AutoCloseInputStream;
import org.apache.commons.io.output.WriterOutputStream;

/**
 *
 * @author t3hk0d3
 */
public class Downloader implements DownloadAdapter {

    public static Class<?> adapterClass = Downloader.class;
    
    public static ConnectionFactory defaultConnectionFactory = new DefaultURLConnectionFactory();

    public static DownloadAdapter create(String url) {
        try {
            return create(new URL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DownloadAdapter create(URL url) {
        try {
            return (DownloadAdapter) adapterClass.getDeclaredConstructor(URL.class).newInstance(url);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    private final URL url;
    private DownloadListener listener = null;
    private URLConnection connection = null;
    private Map<String, String> params = new HashMap<String, String>();
    
    private ConnectionFactory connectionFactory = defaultConnectionFactory;

    protected Downloader(URL url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return this.url.toString();
    }

    @Override
    public URL getURL() {
        return this.url;
    }

    @Override
    public void setDownloadListener(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    public void setRequestHeader(String header, String value) {
        this.params.put(header, value);
    }

    @Override
    public String getResponseHeader(String header) {
        if (this.connection != null) {
            this.connection.getRequestProperty(header);
        }

        return null;
    }

    @Override
    public void download(OutputStream stream) throws IOException {
        URLConnection conn = this.openConnection();

        long total = conn.getContentLengthLong();
        
        InputStream is = new AutoCloseInputStream(conn.getInputStream());


        ByteBuffer buffer = ByteBuffer.allocate(10240);
        ReadableByteChannel rc = null;
        WritableByteChannel wc = null;

        try {
            rc = Channels.newChannel(is);
            wc = Channels.newChannel(stream);

            int read;
            long readed = 0;

            int delta = 0;
            
            if(listener != null) {
                listener.onDownloadStarted(total);
            }
            
            while ((read = rc.read(buffer)) > 0) {
                buffer.flip();
                wc.write(buffer);
                buffer.clear();
                readed += read;

                if (delta < 102400) { // update on delta overflow only
                    delta += read;
                } else if (total > 0) {
                    if(listener != null) {
                        listener.onDownloadProgress(readed, total);
                    }
                    
                    delta = 0;
                }
            }
            
            if(listener != null) {
                listener.onDownloadFinished(readed);
            }

        } finally {
            if (rc != null) {
                rc.close();
            }

            if (wc != null) {
                wc.close();
            }
        }
    }

    @Override
    public void download(Writer writer) throws IOException {
        this.download(new WriterOutputStream(writer));
    }

    @Override
    public void downloadToFile(File destination) throws IOException {
        this.download(new FileOutputStream(destination));
    }

    @Override
    public String downloadToString() throws IOException {
        StringWriter writer = new StringWriter();
        
        this.download(writer);
        
        return writer.toString();
    }

    @Override
    public List<String> downloadLines() throws IOException {
        return new ArrayList<String>(Arrays.asList(this.downloadToString().split("\\r?\\n")));
    }

    public URLConnection openConnection() throws IOException {        
        if (this.connection == null) {
            this.connection = this.connectionFactory.openConnection(url);
            
            for (Map.Entry<String, String> entry : params.entrySet()) {
                this.connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        return this.connection;
    }
    
    protected static class DefaultURLConnectionFactory implements ConnectionFactory {

        @Override
        public URLConnection openConnection(URL url) throws IOException {
            return url.openConnection();
        }

    }
}
