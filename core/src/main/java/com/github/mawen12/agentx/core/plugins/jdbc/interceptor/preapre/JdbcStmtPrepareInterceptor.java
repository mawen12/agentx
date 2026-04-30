package com.github.mawen12.agentx.core.plugins.jdbc.interceptor.preapre;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.field.DynamicFieldAccessor;
import com.github.mawen12.agentx.api.field.DynamicFieldAccessorHelper;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.core.plugins.jdbc.common.SqlInfo;

import java.sql.Statement;

public enum JdbcStmtPrepareInterceptor implements NonReentrantInterceptor {
    INSTANCE;

    private static final Logger LOGGER = Agent.getLogger(JdbcStmtPrepareInterceptor.class);

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        Statement stmt = (Statement) methodInfo.getInvoker();
        if (!(stmt instanceof DynamicFieldAccessor)) {
            LOGGER.error("{} must implements {}", stmt.getClass().getName(), DynamicFieldAccessor.class.getName());
            return;
        }

        SqlInfo sqlInfo = DynamicFieldAccessorHelper.getDynamicFieldValue(stmt);
        if (sqlInfo == null) {
            return;
        }

        String sql = null;
        if (methodInfo.hasArgs()) {
            sql = (String) methodInfo.getArgs()[0];
        }

        String method = methodInfo.getMethod();
        if ("addBatch".equals(method) && sql != null) {
            sqlInfo.addSql(sql, true);
        } else if ("clearBatch".equals(method)) {
            sqlInfo.clear();
        } else if (method.startsWith("execute") && sql != null) {
            sqlInfo.addSql(sql, false);
        }

        ctx.put(SqlInfo.class, sqlInfo);
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public Signal signal() {
        return Signal.PREPARE;
    }
}
