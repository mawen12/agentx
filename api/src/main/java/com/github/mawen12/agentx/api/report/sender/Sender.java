package com.github.mawen12.agentx.api.report.sender;

import java.io.Closeable;
import java.io.IOException;

public interface Sender extends Closeable {
    Sender NOOP = NoOp.INSTANCE;

    String name();

    boolean isAvailable();

    Call<Void> send();

    enum NoOp implements Sender {
        INSTANCE;

        @Override
        public boolean isAvailable() {
            return false;
        }

        @Override
        public Call<Void> send() {
            return Call.NOOP_VOID;
        }

        @Override
        public void close() throws IOException {

        }
    }
}
