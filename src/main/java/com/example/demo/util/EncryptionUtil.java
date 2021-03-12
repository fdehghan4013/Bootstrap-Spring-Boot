package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;


@Component
public class EncryptionUtil {
    private static KeyPair rsaPair;

    private final RsaUtil rsaUtil;

    // this will read from resources -> application.properties
    @Value("${spring.application.name}")
    private String rsaFilesPath;

    @Autowired
    public EncryptionUtil(RsaUtil rsaUtil) {
        this.rsaUtil = rsaUtil;
    }

    @PostConstruct
    public void loadRsa() throws Exception {
        // this line just needs to run once in the startup
        if (rsaPair == null) rsaPair = rsaUtil.loadKey(rsaFilesPath);
    }

    public String encryptAndEncodeBase64(String text) {
        return encodeBase64(encrypt(text));
    }

    public byte[] encrypt(String text) {
        byte[] result = null;
        try {
            Cipher rsa;
            rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.ENCRYPT_MODE, rsaPair.getPublic());
            result = rsa.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String encodeBase64(byte[] result) {
        return Base64.getEncoder().encodeToString(result);
    }

    public String decodeBase64AndDecrypt(String text) {
        return decrypt(decodeBase64(text));
    }

    public byte[] decodeBase64(String result) {
        return Base64.getDecoder().decode(result);
    }


    public String decrypt(byte[] buffer) {
        String result = null;
        try {
            Cipher rsa;
            rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.DECRYPT_MODE, rsaPair.getPrivate());
            byte[] utf8 = rsa.doFinal(buffer);
            result = new String(utf8, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
