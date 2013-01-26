package ru.tehkode.mualauncher;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import ru.tehkode.mualauncher.session.UserSession;

/**
 *
 * @author t3hk0d3
 */
public class MinecraftLauncher {

    private final static FilenameFilter JAR_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".zip") || name.endsWith(".jar");
        }
    };
    private final LauncherOptions options;
    private final File baseDir;

    public MinecraftLauncher(File baseDir, LauncherOptions options) {
        this.options = options;
        this.baseDir = baseDir;
    }

    public Process launchMinecraft(UserSession session) throws IOException {
        List<String> arguments = new ArrayList<String>();
        
        arguments.add(getJavaBinary().getAbsolutePath());
        
        String jvmOptions = options.getJvmOptions();
        if(jvmOptions != null && !jvmOptions.isEmpty()) { // split additional jvm options, or they would be escaped
            arguments.addAll(Arrays.asList(jvmOptions.split("\\s+")));
        }
        
        arguments.add("-cp");
        arguments.add(this.getClassPath());
        
        arguments.add(Minecraft.class.getCanonicalName());
        
        arguments.add(baseDir.getAbsolutePath());
        
        arguments.add(session.getLogin());
        arguments.add(session.getSessionId());
    
        ProcessBuilder builder = new ProcessBuilder(arguments);
        
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        
        return builder.start();        
    }
    
    public File getJavaBinary() {
        File javaBinaryPath = new File(System.getProperty("java.home"), "bin");
        
        String[] possibleJavaBinaries = new String[] { "javaw.exe", "java.exe", "java" };
        
        for(String binaryName : possibleJavaBinaries) {
            File binaryFile = new File(javaBinaryPath, binaryName);
            
            if(binaryFile.exists() && binaryFile.canExecute()) {
                return binaryFile;
            }
        }
        
        throw new RuntimeException("Can't find java binary!");              
    }
    
    public String getClassPath() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(System.getProperty("java.class.path"));
        
        for(File file : getJars()) {
            buffer.append(File.pathSeparator);
            buffer.append(file.getAbsolutePath());
        }
        
        return buffer.toString();
    }

    public List<File> getJars() {
        List<File> files = new ArrayList<File>();

        File instMods = new File(baseDir, "instMods");
        if (instMods.isDirectory()) {


            File forgeLoader = null;

            for (File loader : instMods.listFiles(JAR_FILTER)) {
                if (loader.getName().toLowerCase().contains("forge")) {
                    forgeLoader = loader;
                    continue;
                }

                files.add(loader);
            }

            if (forgeLoader == null) {
                throw new RuntimeException("Forge not found. Probably bogus update.");
            }

            files.add(forgeLoader);
        }

        File minecraftJars = FileUtils.getFile(baseDir, "minecraft", "bin");

        if (!minecraftJars.isDirectory()) {
            throw new RuntimeException("Minecraft binaries not found!");
        }

        // Add all jars in bin directory
        files.addAll(Arrays.asList(minecraftJars.listFiles(JAR_FILTER)));

        return files;
    }
}
