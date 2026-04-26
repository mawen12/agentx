package com.github.mawen12.agentx.core.plugins.jdbc.interceptor.metric;

import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.metric.ServiceMetricRegistry;
import com.github.mawen12.agentx.api.metric.Tags;
import com.github.mawen12.agentx.core.plugins.jdbc.common.JdbcMetric;
import com.github.mawen12.agentx.core.plugins.jdbc.common.SqlInfo;

public class JdbcStmtMetricInterceptor implements NonReentrantInterceptor {
    public static final JdbcStmtMetricInterceptor INSTANCE = new JdbcStmtMetricInterceptor();

    private JdbcMetric metric;

    @Override
    public void init() {
        Tags tags = new Tags("app", "jdbc-statement", "signature");
        metric = ServiceMetricRegistry.getOrCreate(tags, JdbcMetric.JDBC_NAME_FACTORY, JdbcMetric::new);
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        SqlInfo sqlInfo = ctx.get(SqlInfo.class);
        String sql = sqlInfo.getSql();
        metric.collectMetric(sql, methodInfo.getThrowable() == null, ctx);
    }

    @Override
    public Order order() {
        return Order.METRIC;
    }
}
