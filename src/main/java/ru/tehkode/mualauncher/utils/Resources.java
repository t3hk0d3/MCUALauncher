package ru.tehkode.mualauncher.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author t3hk0d3
 */
public class Resources {

    public final static String TRANSLATION_MISSING = "<missing>";
    private static Resources instance = null;

    public static Resources getInstance() {
        if (instance == null) {
            instance = new Resources();
        }

        return instance;
    }

    // Static singleton methods
    public static Image image(String name) throws IOException {
        return ImageIO.read(getInstance().getAsset(name));
    }

    public static String string(String name) {
        return getInstance().getString(name);
    }

    public static Font font(String name) throws IOException, FontFormatException {
        return Font.createFont(Font.TRUETYPE_FONT, getInstance().getAssetStream(name));
    }
    // Instance
    private Map<String, String> strings = new HashMap<String, String>();
    private Map<String, URL> assets = new HashMap<String, URL>();
    
    private Set<String> availableLanguages;

    private Resources() {
        this.loadResources();

        Logger.info("%d translations and %d assets available", strings.size(), assets.size());

        Logger.info("Available languages: %s", getAvailableLocales());
    }

    public String getString(String name) {
        if (strings.containsKey(name.toLowerCase())) {
            return strings.get(name.toLowerCase());
        }

        throw new RuntimeException("String missing for '" + name + "'");
    }

    public InputStream getAssetStream(String name) throws IOException {
        return this.getAsset(name).openStream();
    }

    public URL getAsset(String name) {
        if (assets.containsKey(name.toLowerCase())) {
            Logger.info("Loading asset %s", name);
            return assets.get(name.toLowerCase());
        }

        throw new RuntimeException("Asset missing for '" + name + "'");
    }
    
    public Set<Locale> getAvailableLocales() {
        Set<Locale> locales = new HashSet<Locale>();
        
        for(String language : this.availableLanguages) {
            locales.add(Locale.forLanguageTag(language));            
        }
        
        return locales;
    }

    private void loadResources() {
        Locale locale = Locale.getDefault();

        Map<String, ?> manifest = loadResourceManifest();

        Logger.info("Current locale - %s", locale.toString());

        if (!manifest.containsKey("default")) {
            throw new RuntimeException("Default resources not available!");
        }
        Logger.info("Loading default resource");
        this.loadResources((Map<String, ?>) manifest.get("default"));
        
        availableLanguages = new HashSet<String>(manifest.keySet());
        availableLanguages.remove("default");
        
        String language = locale.toString();
        if (!manifest.containsKey(language)) { // check full code (en_US / en_GB first)
            language = locale.getLanguage(); // if not fallback to simple code (en, ru, fr, etc)
        }

        if (!manifest.containsKey(language)) {
            Logger.warning("Translations missing for %s (%s) lanugange", locale.getDisplayLanguage(Locale.ENGLISH), language);
            return;
        }

        Logger.info("Loading language-specific resources");
        this.loadResources((Map<String, ?>) manifest.get(language));
    }

    private void loadResources(Map<String, ?> resources) {
        if (resources.containsKey("strings")) {
            Logger.info("Loading strings");
            this.loadStrings((Map<String, String>) resources.get("strings"));
        }

        if (resources.containsKey("assets")) {
            Logger.info("Loading assets");
            this.loadAssets((Map<String, String>) resources.get("assets"));
        }
    }

    private void loadStrings(Map<String, String> strings) {
        for (Map.Entry<String, String> entry : strings.entrySet()) {
            String value = entry.getValue();
            if (value == null || value.isEmpty()) {
                continue;
            }

            this.strings.put(entry.getKey().toLowerCase(), entry.getValue());
        }
    }
    
    private void loadAssets(Map<String, String> files) {
        Class classInstance = Resources.class;

        for (Map.Entry<String, String> entry : files.entrySet()) {
            String value = entry.getValue();
            if (value == null || value.isEmpty()) {
                continue;
            }

            URL resource = classInstance.getResource("/" + value);

            if (resource == null) {
                throw new RuntimeException("Asset '" + entry.getKey() + "' (" + value + ") not found");
            }

            this.assets.put(entry.getKey().toLowerCase(), resource);
        }
    }

    private Map<String, ?> loadResourceManifest() {
        Logger.info("Reading resource manifest");
        Yaml yaml = new Yaml();
        return (Map<String, ?>) yaml.loadAs(Resources.class.getResourceAsStream("/resources.yml"), Map.class).get("resources");
    }
}
