package ru.tehkode.mualauncher.session;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import javax.xml.bind.DatatypeConverter;
import ru.tehkode.mualauncher.utils.CryptoUtils;

/**
 * UserCredenital bean
 * 
 * @author t3hk0d3
 */
public class UserCredenitals {

    private String login;
    private char[] password;

    public UserCredenitals(String login, char[] password) {
        this.login = login;
        this.password = password;
    }

    public UserCredenitals(String login, String encryptedPassword) {
        this.login = login;
        this.password = decryptPassword(encryptedPassword);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return new String(this.password);
    }
    
    public void setPassword(String password) {
        this.password = password.toCharArray();
    }
    
    public void setPassword(char[] password) {
        this.password = password;
    }
    
    public String getEncryptedPassword() {
        return new CryptoUtils(login).encrypt(this.getPassword());
    }

    private char[] decryptPassword(String password) {
        return new CryptoUtils(login).decode(password).toCharArray();
    }

    public void saveToDisk(File file) throws IOException {
        FileWriter writer = null;
        
        try {
            writer = new FileWriter(file);

            String data = String.format("%s|%s", this.getLogin(), this.getEncryptedPassword());

            writer.write(DatatypeConverter.printBase64Binary(data.getBytes()));
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static UserCredenitals readFromDisk(File file) throws Exception {
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            CharBuffer buffer = CharBuffer.allocate(1024);

            reader.read(buffer);
            buffer.flip();

            String data = new String(DatatypeConverter.parseBase64Binary(buffer.toString()));
            
            String[] credenitals = data.split("\\|", 2);

            String login = credenitals[0];
            String password = credenitals[1];
                        
            return new UserCredenitals(login, password);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
