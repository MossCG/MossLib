package org.mossmc.mosscg.MossLib.Encrypt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 * 无法解密喵
 */
public class EncryptMD5 {
    public static String encode(String inputStr) throws NoSuchAlgorithmException {
        BigInteger bigInteger;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(inputStr.getBytes());
        bigInteger = new BigInteger(md.digest());
        return bigInteger.toString();
    }
}
