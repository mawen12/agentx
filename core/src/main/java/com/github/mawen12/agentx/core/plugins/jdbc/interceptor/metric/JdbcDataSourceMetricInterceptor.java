package com.github.mawen12.agentx.core.plugins.jdbc.interceptor.metric;

import com.github.mawen12.agentx.api.context.Context;
import com.github.mawen12.agentx.api.interceptor.MethodInfo;
import com.github.mawen12.agentx.api.interceptor.NonReentrantInterceptor;
import com.github.mawen12.agentx.api.metric.ServiceMetricRegistry;
import com.github.mawen12.agentx.api.metric.Tags;
import com.github.mawen12.agentx.core.plugins.jdbc.common.JdbcMetric;
import com.github.mawen12.agentx.core.plugins.jdbc.common.JdbcUtils;

import java.sql.Connection;

public class JdbcDataSourceMetricInterceptor implements NonReentrantInterceptor {
    public static JdbcDataSourceMetricInterceptor INSTANCE = new JdbcDataSourceMetricInterceptor();

    private JdbcMetric metric;

    @Override
    public void init() {
        Tags tags = new Tags("app", "jdbc-connection", "url");
        metric = ServiceMetricRegistry.getOrCreate(tags, JdbcMetric.JDBC_NAME_FACTORY, JdbcMetric::new);
    }

    @Override
    public void doBefore(MethodInfo methodInfo, Context ctx) {
        // NOP
    }

    @Override
    public void doAfter(MethodInfo methodInfo, Context ctx) {
        Connection conn = (Connection) methodInfo.getRetValue();
        String key;
        boolean success = true;

        if (methodInfo.getRetValue() == null || methodInfo.getThrowable() != null) {
            key = "err-conn";
            success = false;
        } else {
            key = JdbcUtils.getUrl(conn);
        }


    }

    @Override
    public Order order() {
        return Order.METRIC;
    }
}
