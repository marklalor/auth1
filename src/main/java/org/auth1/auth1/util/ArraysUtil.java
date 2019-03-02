package org.auth1.auth1.util;

public class ArraysUtil {
    public static byte[] convertToPrimitiveByteArray(Byte[] arr) {
        byte[] res = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }
}
