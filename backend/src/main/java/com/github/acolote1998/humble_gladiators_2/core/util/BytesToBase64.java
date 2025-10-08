package com.github.acolote1998.humble_gladiators_2.core.util;

import java.util.Base64;

public class BytesToBase64 {
    public static String bytesToBase64(byte[] data) {
        if (data == null) {
            return "";
        }
        return Base64.getEncoder().encodeToString(data);
    }
}
