package com.github.mawen12.agentx.core.plugins.jdbc.interceptor.metric;

import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.metric.ServiceMetricRegistry;
import com.github.mawen12.agentx.api.metric.Tags;
import com.github.mawen12.agentx.core.plugins.jdbc.common.DataSizeSqlCompression;
import com.github.mawen12.agentx.core.plugins.jdbc.common.JdbcMetric;
import com.github.mawen12.agentx.core.plugins.jdbc.common.SqlCompression;
import com.github.mawen12.agentx.core.plugins.jdbc.common.SqlInfo;
import com.google.common.cache.Cache;

public class JdbcStmtMetricInterceptor implements NonReentrantInterceptor {
    public static final JdbcStmtMetricInterceptor INSTANCE = new JdbcStmtMetricInterceptor();

    private static final int MAX_CACHE_SIZE = 200;

    private JdbcMetric metric;
    private Cache<String, String> cache;

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
        String key = DataSizeSqlCompression.INSTANCE.compress(sql);
        metric.collectMetric(key, methodInfo.getThrowable() == null, ctx);
    }

    @Override
    public Order order() {
        return Order.METRIC;
    }
}
