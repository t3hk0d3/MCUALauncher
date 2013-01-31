package ru.tehkode.mualauncher.updater;

import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.net.URL;
import ru.tehkode.mualauncher.utils.Logger;

/**
 *
 * @author t3hk0d3
 */
public class LauncherVersion implements Comparable<LauncherVersion> {

    private static LauncherVersion localVersion = null;

    public static LauncherVersion getLocalVersion() {
        if (localVersion == null) {
            localVersion = readLocalVersion();
        }

        return localVersion;
    }
    private String version;
    private int build;
    private String source;

    public LauncherVersion() {
    }

    public LauncherVersion(String version, int build, String source) {
        this.version = version;
        this.build = build;
        this.source = source;
    }

    public String getVersion() {
        return version;
    }

    public int getBuild() {
        return build;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return String.format("%s (build: %d)", version, build);
    }

    private static LauncherVersion readLocalVersion() {
        try {
            Gson gson = new Gson();

            InputStreamReader reader = new InputStreamReader(LauncherVersion.class.getResourceAsStream("/version.json"));

            return gson.fromJson(reader, LauncherVersion.class);
        } catch (Throwable e) {
            Logger.error("Failed to read local version: (%s) %s ", e.getClass().getSimpleName(), e.getMessage());
            
            return new LauncherVersion("unknown", 0, null);
        }
    }

    @Override
    public int compareTo(LauncherVersion o) {
        return this.getBuild() - o.getBuild();
    }
}
