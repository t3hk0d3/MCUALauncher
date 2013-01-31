package ru.tehkode.mualauncher.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.io.output.WriterOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author t3hk0d3
 */
public class DownloaderTest {

    private static final String TEST_URL = "http://test.host/";
    private ConnectionFactory factory;
    private URLConnection connection;
    private URL testURL;

    public DownloaderTest() throws Exception {
        this.testURL = new URL(TEST_URL);

        factory = mock(ConnectionFactory.class);
        connection = mock(URLConnection.class);

        stub(factory.openConnection(testURL)).toReturn(connection);

        Downloader.defaultConnectionFactory = factory;
    }

    @Test
    public void testCreatingWithURL() throws Exception {
        DownloadAdapter result = Downloader.create(testURL);

        assertEquals(testURL, result.getURL());
    }

    @Test
    public void testCreatingWithString() throws Exception {
        DownloadAdapter result = Downloader.create(TEST_URL);

        assertEquals(testURL, result.getURL());
    }

    @Test
    public void testDownloadString() throws Exception {
        String expected = "string result";
        stub(connection.getInputStream()).toReturn(new ByteArrayInputStream(expected.getBytes()));

        String result = Downloader.create(testURL).downloadToString();

        assertEquals(expected, result);
    }

    @Test
    public void testDownloadToWriter() throws Exception {

        String expected = "string result";

        stub(connection.getInputStream()).toReturn(new ByteArrayInputStream(expected.getBytes()));

        StringWriter writer = new StringWriter();

        Downloader.create(testURL).download(writer);

        assertEquals(expected, writer.toString());
    }

    @Test
    public void testDownloadLines() throws Exception {

        String expected = "first line\r\nsecond line\nthird line";

        stub(connection.getInputStream()).toReturn(new ByteArrayInputStream(expected.getBytes()));

        List<String> lines = Downloader.create(testURL).downloadLines();
        
        // @todo aggregate somehow this to single assert
        assertEquals("first line", lines.get(0));
        assertEquals("second line", lines.get(1));
        assertEquals("third line", lines.get(2));
    }

    @Test
    public void testDownloadToOutputStream() throws Exception {
        String expected = "string result";

        stub(connection.getInputStream()).toReturn(new ByteArrayInputStream(expected.getBytes()));

        StringWriter writer = new StringWriter();

        Downloader.create(testURL).download(new WriterOutputStream(writer));

        assertEquals(expected, writer.toString());
    }

    @Test
    public void testDownloadListener() throws Exception {
        DownloadAdapter downloader = Downloader.create(testURL);

        // we need relatively big size to actually fire downloadProgress event
        final Long streamSize = 200000L; // 200 KiB

        InputStream testInputStream = new InputStream() {
            private long bytesRemains = streamSize;
            private Random random = new Random();

            @Override
            public int read() throws IOException {
                if (bytesRemains > 0) {
                    bytesRemains--;
                    return random.nextInt();
                }

                return -1; // EOF
            }
        };

        stub(connection.getContentLengthLong()).toReturn(streamSize);
        stub(connection.getInputStream()).toReturn(testInputStream);

        DownloadListener listener = mock(DownloadListener.class);

        downloader.setDownloadListener(listener);

        downloader.download(new NullOutputStream()); // output to cat arse

        verify(listener).onDownloadStarted(streamSize);
        verify(listener).onDownloadProgress(anyLong(), anyLong());
        verify(listener).onDownloadFinished(streamSize);

    }
}
