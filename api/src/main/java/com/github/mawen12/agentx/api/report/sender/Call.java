package com.github.mawen12.agentx.api.report.sender;

import java.io.IOException;

public interface Call<V> {
    Call<Void> NOOP_VOID = new NoOp<>();

    V execute() throws IOException;

    class NoOp<V> implements Call<V> {

        @Override
        public V execute() throws IOException {
            return null;
        }
    }
}
