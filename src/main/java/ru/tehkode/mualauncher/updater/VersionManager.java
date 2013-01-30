package ru.tehkode.mualauncher.updater;

import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import ru.tehkode.mualauncher.gui.DownaloadWindow;
import ru.tehkode.mualauncher.utils.Logger;

/**
 *
 * @author t3hk0d3
 */
public class VersionManager {

    private final static String UPDATE = "update.zip";
    private final static String VERSION = "version";
    private final File basePath;
    private final String baseURL;
    private final boolean forceUpdate;
    private int currentVersion = 0;
    private DownaloadWindow progressWindow;

    public VersionManager(File basePath, String baseURL, boolean forceUpdate) {
        this.basePath = basePath;
        this.baseURL = baseURL;
        this.forceUpdate = forceUpdate;
        this.progressWindow = new DownaloadWindow();
    }

    public void checkForUpdates() throws Exception {
        if (!this.basePath.isDirectory()) {
            this.basePath.mkdirs();
        }

        this.loadVersionCache();

        int targetVersion = this.currentVersion;

        try {
            URL versionURL = new URL(baseURL + VERSION);

            targetVersion = loadVersion(versionURL.openStream());
        } catch (Exception e) {
            Logger.warning("Couldn't load remote version - " + e.getMessage());
            e.printStackTrace();
        }

        if (forceUpdate) {
            Logger.info("Force update");
        }

        if (forceUpdate || targetVersion > currentVersion) {
            Logger.info("Downloading new version - " + targetVersion + " / " + currentVersion);
            this.downloadUpdate();
        } else {
            Logger.info("Already lastest version available (local version: %d, remote version: %d)", currentVersion, targetVersion);
        }
    }

    private void loadVersionCache() {
        File versionFile = new File(basePath, VERSION);

        if (versionFile.exists()) {
            //Buffered
            try {
                this.currentVersion = loadVersion(new FileInputStream(versionFile));
            } catch (Exception e) {
                Logger.warning("Couldn't read version file - " + e.getMessage());
            }
        }
    }

    private int loadVersion(InputStream is) throws IOException {
        CharBuffer buffer = CharBuffer.allocate(10);

        try {
            (new InputStreamReader(is)).read(buffer);
            buffer.flip();

            return Integer.parseInt(buffer.toString());
        } finally {
            is.close();
        }
    }

    private void downloadUpdate() throws Exception {
        try {
            URL updateURL = new URL(baseURL + UPDATE);

            Logger.info("Downloading " + updateURL);

            URLConnection connection = updateURL.openConnection();

            File tempZip = File.createTempFile("update_mcua", ".zip");

            connection.connect();

            progressWindow.setTotal(connection.getContentLengthLong());

            progressWindow.setVisible(true);

            Logger.info("Downloading...");
            this.copyStreams(connection.getInputStream(), new FileOutputStream(tempZip), connection.getContentLengthLong());

            Logger.info("Unpacking");

            unpackZip(tempZip);

            Logger.info("Removing temporary file");

            tempZip.delete();

            progressWindow.setVisible(false);

        } catch (Exception e) {
            Logger.error("Failed to download update - " + e.getMessage());

            throw e;
        }
    }

    private void unpackZip(File zipFile) throws Exception {
        ZipFile zip = new ZipFile(zipFile);

        Enumeration<? extends ZipEntry> enumerable = zip.entries();

        Logger.info("Unpacking update...");

        this.progressWindow.setTotal(zip.size());

        long processed = 0;

        while (enumerable.hasMoreElements()) {
            ZipEntry entry = enumerable.nextElement();

            File destFile = new File(basePath, entry.getName());

            if (entry.isDirectory()) {
                destFile.mkdirs();

                continue;
            }

            Logger.info("Unpacking %s into %s", entry.getName(), destFile.toString());

            destFile.createNewFile();

            this.copyStreams(zip.getInputStream(entry), new FileOutputStream(destFile), 0);

            this.progressWindow.setUnpackingProgress(++processed);
        }
    }

    private void copyStreams(InputStream is, OutputStream os, long total) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(10240);
        ReadableByteChannel rc = null;
        WritableByteChannel wc = null;

        try {
            rc = Channels.newChannel(is);
            wc = Channels.newChannel(os);

            int read;
            long readed = 0;

            int delta = 0;
            long start = System.currentTimeMillis();

            while ((read = rc.read(buffer)) > 0) {
                buffer.flip();
                wc.write(buffer);
                buffer.clear();
                readed += read;
                
                if (delta < 102400) { // update on delta overflow only
                    delta += read;
                } else if (total > 0) {
                    progressWindow.setDownloadProgress(readed, (int)(readed / (System.currentTimeMillis() - start))*1000);
                    delta = 0;
                }
            }

        } finally {
            if (is != null) {
                is.close();
            }

            if (os != null) {
                os.close();
            }

            if (rc != null) {
                rc.close();
            }

            if (wc != null) {
                wc.close();
            }
        }
    }
}
