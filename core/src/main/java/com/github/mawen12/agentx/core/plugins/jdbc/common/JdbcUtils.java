package com.github.mawen12.agentx.core.plugins.jdbc.common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class JdbcUtils {

    public static String getUrl(Connection conn) {
        try {
            DatabaseMetaData meta = conn.getMetaData();
            String url = meta.getURL();
            int idx = url.indexOf("?");
            return idx == -1 ? url : url.substring(0, idx);
        } catch (SQLException ignored) {
        }

        return null;
    }
}
