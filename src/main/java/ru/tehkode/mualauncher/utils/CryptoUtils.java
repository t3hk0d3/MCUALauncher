/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.mualauncher.utils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author t3hk0d3
 */
public class CryptoUtils {

    private final static String ALGORITHM = "PBEWithMD5AndDES";
    private byte[] salt = { // default values
        (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
        (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99
    };
    private final PBEKeySpec key;

    public CryptoUtils() {
        this.key = generateKey();
    }

    public CryptoUtils(String salt) {
        this();

        // @todo generate 8byte salt from given
        // this.salt = salt.getBytes();
    }

    public String decode(String encrypted) {
        return new String(crypt(Cipher.DECRYPT_MODE, DatatypeConverter.parseBase64Binary(encrypted)));
    }

    public String encrypt(String data) {
        return DatatypeConverter.printBase64Binary(crypt(Cipher.ENCRYPT_MODE, data.getBytes()));
    }

    private byte[] crypt(final int mode, byte[] data) {
        try {

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey secretKey = keyFactory.generateSecret(this.key);

            Cipher pbeCipher = Cipher.getInstance(ALGORITHM);

            pbeCipher.init(mode, secretKey, new PBEParameterSpec(this.salt, 8));

            return pbeCipher.doFinal(data);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PBEKeySpec generateKey() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();

                byte[] mac = iface.getHardwareAddress();

                if (mac == null) {
                    continue;
                }

                return new PBEKeySpec(new String(mac).toCharArray());
            }
        } catch (SocketException e) {
            Logger.error("Couldn't determine MAC address. Fallback to insecure mode");
        }

        // default key
        return new PBEKeySpec(new char[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
    }
}
