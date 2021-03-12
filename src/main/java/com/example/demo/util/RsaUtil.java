package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class RsaUtil {
    private static KeyPair pair;

    /**
     * lazy singleton for loading key pair.
     * please use this method once at the startup of ur program
     *
     * @param path the path of private key file
     * @return KeyPair
     */
    public KeyPair loadKey(String path) throws Exception {
        try {
            if (RsaUtil.pair == null) {
                RsaUtil.pair = readFromFileOrGenerate(path);
            }

            return RsaUtil.pair;
        } catch (Exception ex) {
            throw new Exception("cannot load or even generate key files", ex);
        }
    }

    private KeyPair readFromFileOrGenerate(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPair pair = readFromFile(path);

        if (pair == null) {
            pair = generate(path);
        }

        return pair;
    }

    private KeyPair readFromFile(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File file = new File(path + ".key");

        KeyPair pair = null;
        if (file.exists()) {
            byte[] bytes = Files.readAllBytes(file.toPath());

            KeyFactory kf = KeyFactory.getInstance("RSA");

            PKCS8EncodedKeySpec ks  = new PKCS8EncodedKeySpec(bytes);
            PrivateKey          pvt = kf.generatePrivate(ks);

            file = new File(path + ".pub");
            bytes = Files.readAllBytes(file.toPath());

            X509EncodedKeySpec ks2 = new X509EncodedKeySpec(bytes);
            PublicKey          pub = kf.generatePublic(ks2);

            pair = new KeyPair(pub, pvt);
        }

        return pair;
    }

    private KeyPair generate(String path) throws IOException, NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);

        KeyPair    keyPair = kpg.generateKeyPair();
        PrivateKey pvt     = keyPair.getPrivate();
        PublicKey  pub     = keyPair.getPublic();
        File       file    = new File(path + ".key");

        FileOutputStream pvtOut = null;
        FileOutputStream pubOut = null;
        try {
            pvtOut = new FileOutputStream(file);
            pvtOut.write(pvt.getEncoded());
            pvtOut.flush();

            file = new File(path + ".pub");

            pubOut = new FileOutputStream(file);
            pubOut.write(pub.getEncoded());
            pubOut.flush();
        } finally {
            if (pvtOut != null) pvtOut.close();
            if (pubOut != null) pubOut.close();
        }

        return keyPair;
    }


}
