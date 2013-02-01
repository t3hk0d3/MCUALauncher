package ru.tehkode.mualauncher.updater;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import ru.tehkode.mualauncher.Launcher;
import ru.tehkode.mualauncher.gui.DownloadWindow;
import ru.tehkode.mualauncher.net.DownloadAdapter;
import ru.tehkode.mualauncher.net.Downloader;
import ru.tehkode.mualauncher.utils.Logger;
import ru.tehkode.mualauncher.utils.PlatformUtils;
import ru.tehkode.mualauncher.utils.Resources;

/**
 *
 * @author t3hk0d3
 */
public class LauncherUpdater {

    private final String removeVersionURL;

    public LauncherUpdater(String versionUrl) {
        removeVersionURL = versionUrl;
    }

    public void updateLauncher() {
        Logger.info("Checking lastest version");

        LauncherVersion localVersion = LauncherVersion.getLocalVersion();
        Logger.info("Local version is %s", localVersion);

        LauncherVersion remoteVersion = fetchRemoteVersion();
        Logger.info("Remote version is %s", remoteVersion);

        if (localVersion.compareTo(remoteVersion) >= 0) { // already lastest version
            Logger.info("Already lastest version (%s/%s", localVersion, remoteVersion);
            return;
        }

        Logger.info("Performing update to %s", remoteVersion);

        try {
            File localBinary = getLocalFile();
            String format = getExtension(localBinary);

            Logger.info("Local file: %s", localBinary.getAbsolutePath());

            Logger.info("Downloading update");

            File tempUpdater = downloadUpdate(remoteVersion, format);

            Logger.info("Downloaded update to %s", tempUpdater.getAbsolutePath());

            Logger.info("Launching self-updater");
            String updater = tempUpdater.getAbsolutePath();

            PlatformUtils.launchJavaApplication(updater, LauncherUpdater.class.getCanonicalName(), updater, localBinary.getAbsolutePath());

            Logger.info("Now exiting to unlock file");

            System.exit(0);

            Logger.info("If you are watching this you are pretty much a pirate now!");
        } catch (IOException e) {
            Logger.error("Error during launcher update: ", e.getMessage());
        }
    }

    public File downloadUpdate(LauncherVersion remoteVersion, String format) throws IOException {
        if (remoteVersion.getSource() == null) { // malformed file, or local version
            throw new RuntimeException("Invalid remote version: source is null");
        }

        // check current format


        String remoteURL = remoteVersion.getSource() + format;

        String fileName = "mualauncher_" + remoteVersion.getVersion() + "_" + remoteVersion.getBuild() + "-";

        File temporaryFile = File.createTempFile(fileName, format);

        DownloadAdapter downloader = Downloader.create(remoteURL);
        
        DownloadWindow window = new DownloadWindow(Resources.string("selfupdate_title"));

        downloader.setDownloadListener(window);
        
        downloader.downloadToFile(temporaryFile);
        
        window.dispose(); // not needed anymore

        return temporaryFile;
    }

    public LauncherVersion fetchRemoteVersion() {
        try {
            String versionJson = Downloader.create(removeVersionURL).downloadToString();

            return LauncherVersion.fromJson(versionJson);
        } catch (Throwable e) {
            Logger.warning("Unable to fetch remote version: %s", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private File getLocalFile() {
        File localBinary = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsoluteFile();

        if (localBinary.isDirectory()) {
            throw new RuntimeException("Local version run from a folder!");
        }

        return localBinary;
    }

    private String getExtension(File file) {
        if (file.getName().endsWith(".exe")) {
            return ".exe";
        } else {
            return ".jar";
        }
    }

    public static void main(String args[]) throws Exception {
        if (args.length < 2) {
            System.out.println("Invalid usage!");
            return;
        }

        final File currentPath = PlatformUtils.getApplicationPath("mualauncher");
        Logger.initialize(new File(currentPath, "updater.log"));

        File tempFile = new File(args[0]);
        File launcherFile = new File(args[1]);

        try {
            Logger.info("Removing old version");

            if (!launcherFile.delete()) {
                throw new IOException("Unable to remove old file " + launcherFile);
            }

            Logger.info("Copying new launcher from %s", tempFile.getAbsolutePath());
            FileUtils.copyFile(tempFile, launcherFile);
        } catch (IOException e) {
            Logger.info("Autoupdate failed: %s ", e.getMessage());
        }

        // Start launcher
        Logger.info("Starting launcher %s", launcherFile.getAbsolutePath());

        PlatformUtils.launchJavaApplication(launcherFile.getAbsolutePath(), Launcher.class.getCanonicalName(), "updated");

        Logger.info("Update done!");

        System.exit(0);

    }
}
