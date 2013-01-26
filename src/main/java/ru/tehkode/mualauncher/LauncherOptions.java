package ru.tehkode.mualauncher;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import ru.tehkode.mualauncher.utils.Logger;

/**
 *
 * @author t3hk0d3
 */
public class LauncherOptions extends Properties {

    public final static String DEFAULT_JVM_OPTIONS = "-Xms512m -Xmx1024m";
    private boolean forceReload = false;
    private final File file;

    public LauncherOptions(File file) {
        this.file = file;

        if (this.file != null) {
            this.reload();
        }
    }

    public final void reload() {
        if (!file.exists()) {
            return;
        }

        try {
            this.load(new FileReader(file));
        } catch (IOException e) {
            Logger.warning("Failed to load properties from %s", file.getAbsolutePath());
        }
    }

    public void save() {
        try {
            this.store(new FileWriter(file), null);
        } catch (IOException e) {
            Logger.warning("Failed to save properties into %s", file.getAbsolutePath());
        }
    }

    public void setJvmOptions(String options) {
        this.setProperty("jvm-options", options);
    }

    public String getJvmOptions() {
        return this.getProperty("jvm-options", DEFAULT_JVM_OPTIONS);
    }

    public boolean isForceReload() {
        return this.forceReload;
    }

    public void setForceReload(boolean reload) {
        this.forceReload = reload;
    }
}
