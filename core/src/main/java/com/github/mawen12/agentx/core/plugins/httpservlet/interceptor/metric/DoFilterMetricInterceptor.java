package com.github.mawen12.agentx.core.plugins.httpservlet.interceptor.metric;

import com.github.mawen12.agentx.api.metric.ServiceMetricRegistry;
import com.github.mawen12.agentx.api.metric.Tags;
import com.github.mawen12.agentx.core.plugins.httpservlet.interceptor.BaseInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DoFilterMetricInterceptor extends BaseInterceptor {
    public static final DoFilterMetricInterceptor INSTANCE = new DoFilterMetricInterceptor();

    private static final String AFTER_MARK = DoFilterMetricInterceptor.class.getName() + "$AfterMark";

    private ServerMetric metric;

    @Override
    public void init() {
        Tags tags = new Tags("app", "http-request", "url");
        metric = ServiceMetricRegistry.getOrCreate(tags, ServerMetric.SERVER_NAME_FACTORY, ServerMetric::new);
    }

    @Override
    public String getAfterMark() {
        return AFTER_MARK;
    }

    @Override
    public void internalAfter(String key, Throwable throwable, HttpServletRequest request, HttpServletResponse response, long start) {
        long end = System.currentTimeMillis();
        metric.collectMetric(key, response.getStatus(), throwable, start, end);
    }

    @Override
    public Order order() {
        return Order.METRIC;
    }
}
