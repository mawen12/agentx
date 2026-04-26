package com.github.mawen12.agentx.core.plugins.jdbc.interceptor.preapre;

import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.field.DynamicFieldAccessorHelper;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.core.plugins.jdbc.common.SqlInfo;

import java.sql.Connection;
import java.sql.Statement;

public enum JdbcConnPrepareOrCreateStmtInterceptor implements NonReentrantInterceptor {
    INSTANCE;


    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        Statement stmt = (Statement) methodInfo.getRetValue();
        SqlInfo sqlInfo = new SqlInfo((Connection) methodInfo.getInvoker());
        if (methodInfo.getMethod().startsWith("prepare") && methodInfo.hasArgs()) {
            String sql = (String) methodInfo.getArgs()[0];
            if (sql != null) {
                sqlInfo.addSql(sql, false);
            }
        }

        DynamicFieldAccessorHelper.setDynamicFieldValue(stmt, sqlInfo);
    }

    @Override
    public Order order() {
        return Order.PREPARE;
    }
}
