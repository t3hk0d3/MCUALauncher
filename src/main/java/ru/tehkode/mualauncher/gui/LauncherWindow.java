package ru.tehkode.mualauncher.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import ru.tehkode.mualauncher.Launcher;
import ru.tehkode.mualauncher.LauncherOptions;
import ru.tehkode.mualauncher.MinecraftLauncher;
import ru.tehkode.mualauncher.session.UserCredenitals;
import ru.tehkode.mualauncher.session.UserAuthorization;
import ru.tehkode.mualauncher.session.UserSession;
import ru.tehkode.mualauncher.widgets.ActionButton;
import ru.tehkode.mualauncher.widgets.ImageComponent;
import ru.tehkode.mualauncher.utils.Logger;
import ru.tehkode.mualauncher.widgets.MouseDragger;
import ru.tehkode.mualauncher.widgets.PasswordPromptField;
import ru.tehkode.mualauncher.widgets.PromptField;
import ru.tehkode.mualauncher.updater.VersionManager;

public class LauncherWindow extends JFrame implements ActionListener {

    private final File currentPath = new File("Minecraft");
    private File loginData = new File(currentPath, "lastLogin");
    final LauncherWindow window = this;
    final BufferedImage image;
    private Font font;
    private final LauncherOptions options;
    private final OptionsDialog dialog;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JCheckBox rememberCheckbox;

    public LauncherWindow() throws Exception {
        super("Minecraft");
        
        this.setIconImage(ImageIO.read(Launcher.class.getResourceAsStream("/icon.png")));

        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MouseDragger dragger = new MouseDragger(this);

        this.loadResources();

        image = ImageIO.read(Launcher.class.getResourceAsStream("/background.png"));
        ImageComponent backGround = new ImageComponent(image);
        this.setPreferredSize(backGround.getSize());
        this.setContentPane(backGround);

        this.options = new LauncherOptions(new File("launcher.options"));
        this.dialog = new OptionsDialog(this, options);

        this.initComponents();

        this.pack();
    }

    private void loadResources() throws Exception {
        this.font = this.loadFont().deriveFont(16.0f);

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

    private Font loadFont() throws Exception {
        InputStream fontStream = Launcher.class.getResourceAsStream("/minecraft.ttf");

        return Font.createFont(Font.TRUETYPE_FONT, fontStream);
    }

    private void initComponents() {
        JButton closeButton = new JButton(new ImageIcon(Launcher.class.getResource("/close.png")));
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setContentAreaFilled(false);
        closeButton.setSize(20, 20);
        closeButton.setLocation(460, 10);
        
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        this.add(closeButton);
        
        JButton minimizeButton = new JButton(new ImageIcon(Launcher.class.getResource("/minimize.png")));
        minimizeButton.setBorder(BorderFactory.createEmptyBorder());
        minimizeButton.setContentAreaFilled(false);
        minimizeButton.setSize(20, 20);
        minimizeButton.setLocation(435, 10);
        
        minimizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setState(Frame.ICONIFIED);
            }
        });
        
        this.add(minimizeButton);
        
        
        final JButton loginButton = new ActionButton("Войти", "login");
        loginButton.addActionListener(this);
        loginButton.setSize(90, 25);
        loginButton.setLocation(330, 203);
        this.add(loginButton);
        loginButton.requestFocusInWindow();

        JButton optionsButton = new ActionButton("Опции", "options");
        optionsButton.addActionListener(this);
        optionsButton.setSize(90, 25);
        optionsButton.setLocation(330, 240);
        this.add(optionsButton);

        loginField = new PromptField("Логин");
        loginField.setSize(250, 25);
        loginField.setLocation(75, 203);
        this.add(loginField);

        passwordField = new PasswordPromptField("Пароль");
        passwordField.setSize(250, 25);
        passwordField.setLocation(75, 240);
        this.add(passwordField);

        rememberCheckbox = new JCheckBox("Запомнить пароль");
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

        final LauncherWindow launcherWindow = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UserCredenitals credenitals = new UserCredenitals(loginField.getText(), passwordField.getPassword());
                UserAuthorization authorizer = new UserAuthorization(credenitals);

                VersionManager manager = new VersionManager(currentPath, "http://minecraft.com.ua/dl/mods/", false);
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
                    JOptionPane.showMessageDialog(launcherWindow, "Неверный логин или пароль", "Неверный логин/пароль", JOptionPane.ERROR_MESSAGE);
                    loginField.setForeground(Color.red);
                } catch (Exception e) {
                    Logger.warning("Login Error - %s", e.getMessage());
                }
            }
        });

        thread.start();


    }
}
