package ru.tehkode.mualauncher.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import ru.tehkode.mualauncher.LauncherOptions;
import ru.tehkode.mualauncher.MinecraftLauncher;
import ru.tehkode.mualauncher.updater.VersionManager;
import ru.tehkode.mualauncher.session.*;
import ru.tehkode.mualauncher.widgets.*;
import ru.tehkode.mualauncher.utils.*;

import static ru.tehkode.mualauncher.utils.Resources.*;

public class LauncherWindow extends JFrame implements ActionListener {

    private final File currentPath = PlatformUtils.getApplicationPath("mualauncher");
    private File loginData = new File(currentPath, "lastLogin");
    private Font font;
    private final LauncherOptions options;
    private final OptionsDialog dialog;
    
    private JTextField loginField;
    private JPasswordField passwordField;
    private JCheckBox rememberCheckbox;
    
    private boolean locked = false;
    
    
    private MouseDragger dragger = new MouseDragger(this);

    public LauncherWindow() throws Exception {
        super(string("window_title"));
        
        if(!currentPath.isDirectory()) {
            if(currentPath.isFile()) { // not actually possible, just in case
                currentPath.delete();
            }
            
            currentPath.mkdirs();
        }
        
         Logger.info("Working directory is '%s'", currentPath.getAbsolutePath());

        this.setIconImage(Resources.image("window_icon"));
        this.setUndecorated(true);
        this.setLayout(null);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.loadResources();

        this.options = new LauncherOptions(new File(currentPath, "launcher.options"));
        this.dialog = new OptionsDialog(this, options);

        this.initComponents();
        
        this.pack();
       
        this.setLocationRelativeTo(null);
    }

    private void loadResources() throws Exception {
        this.font = font("default_font").deriveFont(16.0f);

        FontUIResource fontUI = new FontUIResource(this.font);

        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, fontUI);
            }
        }

    }

    private void initComponents() throws IOException {
        ImageComponent backGround = new ImageComponent(image("background"));
        this.setPreferredSize(backGround.getSize());
        this.setContentPane(backGround);

        JButton closeButton = new JButton(new ImageIcon(image("close_button")));
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setContentAreaFilled(false);
        closeButton.setSize(20, 20);
        closeButton.setLocation(460, 10);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.add(closeButton);

        JButton minimizeButton = new JButton(new ImageIcon(image("minimize_button")));
        minimizeButton.setBorder(BorderFactory.createEmptyBorder());
        minimizeButton.setContentAreaFilled(false);
        minimizeButton.setSize(20, 20);
        minimizeButton.setLocation(435, 10);
        minimizeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        minimizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setState(Frame.ICONIFIED);
            }
        });

        this.add(minimizeButton);


        final JButton loginButton = new ActionButton(string("login_button"), "login");
        loginButton.addActionListener(this);
        loginButton.setSize(90, 25);
        loginButton.setLocation(330, 203);
        this.add(loginButton);
        loginButton.requestFocusInWindow();

        JButton optionsButton = new ActionButton(string("options_button"), "options");
        optionsButton.addActionListener(this);
        optionsButton.setSize(90, 25);
        optionsButton.setLocation(330, 240);
        this.add(optionsButton);

        loginField = new PromptField(string("login_placeholder"));
        loginField.setSize(250, 25);
        loginField.setLocation(75, 203);
        this.add(loginField);

        passwordField = new PasswordPromptField(string("password_placeholder"));
        passwordField.setSize(250, 25);
        passwordField.setLocation(75, 240);
        this.add(passwordField);

        rememberCheckbox = new JCheckBox(string("remember_checkbox"));
        rememberCheckbox.setOpaque(false);
        rememberCheckbox.setForeground(Color.white);
        rememberCheckbox.setSize(250, 25);
        rememberCheckbox.setLocation(75, 270);

        this.add(rememberCheckbox);

        if (loginData.exists()) {
            try {
                UserCredenitals credenitals = UserCredenitals.readFromDisk(loginData);

                loginField.setText(credenitals.getLogin());
                passwordField.setText(credenitals.getPassword());

                rememberCheckbox.setSelected(true);
            } catch (Throwable e) {
                Logger.warning("Failed to read last login data - %s", e.getMessage());
            }
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loginButton.grabFocus();
                loginButton.requestFocus();//or inWindow
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("login".equals(e.getActionCommand())) {
            this.performLogin();
        } else if ("options".equals(e.getActionCommand())) {
            this.dialog.setVisible(true);
        }
    }

    public void performLogin() {
        if(this.locked) {
            Logger.warning("Already launched!");
            return;
        }
        
        this.locked = true;

        final LauncherWindow launcherWindow = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UserCredenitals credenitals = new UserCredenitals(loginField.getText(), passwordField.getPassword());
                UserAuthorization authorizer = new UserAuthorization(credenitals);

                VersionManager manager = new VersionManager(currentPath, "http://minecraft.com.ua/dl/mods/", options.isForceReload());
                MinecraftLauncher launcher = new MinecraftLauncher(currentPath, options);

                try {

                    UserSession session = authorizer.authorize();

                    if (rememberCheckbox.isSelected()) {
                        Logger.info("Saving login data on disk");
                        credenitals.saveToDisk(loginData);
                    }

                    launcherWindow.setVisible(false);

                    manager.checkForUpdates();

                    Process minecraftProcess = launcher.launchMinecraft(session);

                    Logger.info("Children minecraft process created...");

                    // freeze this thread until minecraft closed
                    minecraftProcess.waitFor();

                    Logger.info("Children process was terminated. Exiting...");

                    System.exit(0);
                } catch (UserAuthorization.BadLoginException e) {
                    Logger.warning("Bad Login");
                    JOptionPane.showMessageDialog(launcherWindow, string("bad_login_message"), string("bad_login_title"), JOptionPane.ERROR_MESSAGE);
                    loginField.setForeground(Color.red);
                } catch (Exception e) {
                    Logger.warning("Login Error: (%s) %s", e.getClass().getSimpleName(), e.getMessage());
                }
                
                locked = false;
            }
        });

        thread.start();
    }
}
