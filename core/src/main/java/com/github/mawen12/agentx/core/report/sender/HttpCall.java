package com.github.mawen12.agentx.core.report.sender;

import com.github.mawen12.agentx.api.report.sender.Call;
import lombok.AllArgsConstructor;
import okhttp3.Response;

import java.io.IOException;

@AllArgsConstructor
public class HttpCall implements Call<Void> {

    private okhttp3.Call delegate;

    @Override
    public Void execute() throws IOException {
        try (Response resp = delegate.execute()) {
            handleResp(resp);
        }
        return null;
    }

    void handleResp(Response resp) throws IOException {
        if (resp.isSuccessful()) {
            return;
        }
        throw new IOException("response failed: " + resp);
    }
}
