package ru.tehkode.mualauncher.updater;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.io.IOUtils;
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

    @Override
    public int compareTo(LauncherVersion o) {
        return this.getBuild() - o.getBuild();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.version != null ? this.version.hashCode() : 0);
        hash = 31 * hash + this.build;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LauncherVersion other = (LauncherVersion) obj;
        if ((this.version == null) ? (other.version != null) : !this.version.equals(other.version)) {
            return false;
        }
        if (this.build != other.build) {
            return false;
        }
        return true;
    }

    public String toJson() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public static LauncherVersion fromJson(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, LauncherVersion.class);
    }

    private static LauncherVersion readLocalVersion() {
        try {
            String localVersion = IOUtils.toString(LauncherVersion.class.getResourceAsStream("/version.json"));

            return fromJson(localVersion);
        } catch (Throwable e) {
            Logger.error("Failed to read local version: (%s) %s ", e.getClass().getSimpleName(), e.getMessage());

            return new LauncherVersion("unknown", 0, null);
        }
    }
}
