package ru.tehkode.mualauncher.minecraft;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author t3hk0d3
 */
public class MinecraftAppletWrapper extends Applet implements AppletStub {

    private Applet applet;
    private final MinecraftProperties parameters;

    public MinecraftAppletWrapper(Applet applet, MinecraftProperties parameters) {
        this.parameters = parameters;
        this.applet = applet;

        setLayout(new BorderLayout());
    }

    @Override
    public void start() {
        applet.setStub(this);
        applet.setSize(getWidth(), getHeight());
        add(applet, BorderLayout.CENTER);
        validate();
        applet.init();
        applet.start();
        
    }

    @Override
    public URL getDocumentBase() {
        try {
            return new URL("http://www.minecraft.net/game/");
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public String getParameter(String name) {
        String custom = (String) parameters.getProperty(name);
        if (custom != null) {
            return custom;
        }

        try {
            return super.getParameter(name);
        } catch (Throwable e) {
            parameters.setProperty(name, null);
        }
        
        return null;
    }

    @Override
    public boolean isActive() {
        return true;
    }  
    
    public void replace(Applet applet) {
        this.stop();

        this.applet = applet;

        this.start();
    }

    @Override
    public void stop() {
        applet.stop();
    }

    @Override
    public void destroy() {
        applet.destroy();
    }
    
    @Override
    public void appletResize(int width, int height) {
        // do nothing
    }
}
