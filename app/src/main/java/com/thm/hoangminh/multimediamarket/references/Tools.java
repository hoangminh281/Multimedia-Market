package com.thm.hoangminh.multimediamarket.references;

import java.text.DecimalFormat;

public class Tools {
    public static String md5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String FormatDecimal(double value) {
        DecimalFormat format = new DecimalFormat("##,###,###");
        return  format.format(value);
    }
}
