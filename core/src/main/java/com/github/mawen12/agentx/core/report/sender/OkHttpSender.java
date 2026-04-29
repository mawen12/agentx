package com.github.mawen12.agentx.core.report.sender;

import com.github.mawen12.agentx.api.report.sender.Call;
import com.github.mawen12.agentx.api.report.sender.HttpConfig;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class OkHttpSender extends AbstractHttpSender {

    private OkHttpClient client;

    public OkHttpSender(HttpConfig config) {
        super(config);
    }

    @Override
    void initClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(config.getTimeout());
        builder.readTimeout(config.getTimeout());
        builder.writeTimeout(config.getTimeout());

        client = builder.dispatcher(newDispatcher(config.getMaxRequest())).build();
    }

    @Override
    Call<Void> doSend() {
        Request request = null;
        return new HttpCall(client.newCall(request));
    }

    @Override
    public void close() throws IOException {
        Dispatcher dispatcher = client.dispatcher();
        dispatcher.executorService().shutdown();

        try {
            if (!dispatcher.executorService().awaitTermination(1, TimeUnit.SECONDS)) {
                dispatcher.cancelAll();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static Dispatcher newDispatcher(int maxRequests) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, maxRequests, 60, TimeUnit.SECONDS,
                new SynchronousQueue<>(), new HttpThreadFactory());

        Dispatcher dispatcher = new Dispatcher(executor);
        dispatcher.setMaxRequests(maxRequests);
        dispatcher.setMaxRequestsPerHost(maxRequests);
        return dispatcher;
    }
}
