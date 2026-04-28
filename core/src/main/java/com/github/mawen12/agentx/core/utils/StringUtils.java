package com.github.mawen12.agentx.core.utils;

import java.nio.charset.StandardCharsets;

public class StringUtils {

    public static String cutStrByDataSize(String str, DataSize size) {
        byte[] now = str.getBytes(StandardCharsets.UTF_8);
        if (now.length < size.toBytes()) {
            return str;
        }

        String tmp = new String(now, 0, (int) size.toBytes(), StandardCharsets.UTF_8);
        char unstable = tmp.charAt(tmp.length() - 1);
        char old = str.charAt(tmp.length() - 1);
        if (unstable == old) {
            return tmp;
        }
        return new String(tmp.toCharArray(), 0, tmp.length() - 1);
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
