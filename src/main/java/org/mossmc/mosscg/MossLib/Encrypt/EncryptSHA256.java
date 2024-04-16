package org.mossmc.mosscg.MossLib.Encrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA-256加密
 * 无法解密喵
 */
public class EncryptSHA256 {
    public static String encode(String inputStr) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(inputStr.getBytes(StandardCharsets.UTF_8));
        byte[] result = md.digest();
        StringBuilder res = new StringBuilder();
        for (byte b : result) res.append(String.format("%02X", b));
        return res.toString();
    }
}
