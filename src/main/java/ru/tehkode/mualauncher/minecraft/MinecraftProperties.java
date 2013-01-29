/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.mualauncher.minecraft;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author t3hk0d3
 */
public class MinecraftProperties {
    
    private final Map<String, String> properties = new HashMap<String, String>();

    public MinecraftProperties(String[] args) {        
        this.properties.putAll(parseArguments(args));
    }
    
    private Map<String, String> parseArguments(String[] args) {
        Map<String, String> params = new HashMap<String, String>();
        
        if(args.length < 3) {
            throw new RuntimeException("Invalid params!");
        }
        
        params.put("basePath", args[0]);
        params.put("username", args[1]);
        params.put("sessionid", args[2]);
        
        if(args.length > 3) {
            params.put("server", args[3]);
            
            if(args.length > 4) {
                params.put("port", args[3]);
            } else {
                params.put("port", "25565"); // default minecraft port
            }
        }
        
        params.put("stand-alone", "true");
        params.put("demo", "false");
        params.put("fullscreen", "false");
        
        // @todo add standalone and demo mode args support
        // @todo also fullscreen
        
        return params;
    }
    
    public File getBasePath() {
        return new File(getProperty("basePath"));
    }
    
    public String getLogin() {
        return this.getProperty("username");
    }
    
    public String getSessionId() {
        return this.getProperty("sessionid");
    }
    
    public String getProperty(String name) {
        return this.properties.get(name);
    }
    
    public void setProperty(String name, String value ) { 
        this.properties.put(name, value);
    }
    
}
