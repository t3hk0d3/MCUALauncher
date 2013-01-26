package ru.tehkode.mualauncher.tools;

import ru.tehkode.mualauncher.utils.CryptoUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author t3hk0d3
 */

public class CryptoUtilsTest {
    
    protected CryptoUtils instance;
    
    @Before
    public void setup() {
        this.instance = new CryptoUtils("testsalt");    
    }
    
    @Test
    public void testEncoding() {
        String password = "testpasswordtest";
        
        String encrypted = this.instance.encrypt(password);               
        String decrypted = this.instance.decode(encrypted);
        
        
        assertEquals(decrypted, password);
        assertNotEquals(password, encrypted);
    }
    
    
}
