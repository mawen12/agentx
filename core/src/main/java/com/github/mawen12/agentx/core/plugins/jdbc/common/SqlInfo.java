package com.github.mawen12.agentx.core.plugins.jdbc.common;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SqlInfo {
    private final Connection conn;
    private final List<String> sqls = new ArrayList<>();

    public SqlInfo(Connection conn) {
        this.conn = conn;
    }

    public void addSql(String sql, boolean isBatch) {
        if (isBatch) {
            sqls.clear();
        }
        sqls.add(sql);
    }

    public void clear() {
        sqls.clear();
    }

    public String getSql() {
        if (sqls.isEmpty()) {
            return null;
        }
        return String.join("\n", sqls);
    }
}
