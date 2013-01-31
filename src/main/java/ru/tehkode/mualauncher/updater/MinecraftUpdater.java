package ru.tehkode.mualauncher.updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.CharBuffer;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;

import ru.tehkode.mualauncher.gui.DownloadWindow;
import ru.tehkode.mualauncher.net.DownloadAdapter;
import ru.tehkode.mualauncher.net.Downloader;
import ru.tehkode.mualauncher.utils.Logger;

/**
 *
 * @author t3hk0d3
 */
public class MinecraftUpdater {

    private final static String UPDATE = "update.zip";
    private final static String VERSION = "version";
    private final File basePath;
    private final String baseURL;
    private final boolean forceUpdate;
    private int currentVersion = 0;
    private DownloadWindow progressWindow;

    public MinecraftUpdater(File basePath, String baseURL, boolean forceUpdate) {
        this.basePath = basePath;
        this.baseURL = baseURL;
        this.forceUpdate = forceUpdate;
        this.progressWindow = new DownloadWindow();
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

            File tempZip = File.createTempFile("update_mcua", ".zip");

            progressWindow.setVisible(true);

            Logger.info("Downloading...");
            
            DownloadAdapter downloader = Downloader.create(updateURL);
            
            downloader.setDownloadListener(progressWindow);
            
            downloader.downloadToFile(tempZip);
            
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
            
            IOUtils.copy(zip.getInputStream(entry), new FileOutputStream(destFile));

            this.progressWindow.setUnpackingProgress(++processed);
        }
    }

}
