package com.github.mawen12.agentx.core.report;

import zipkin2.reporter.BytesEncoder;
import zipkin2.reporter.Encoding;

public enum BytesWrapperEncoder implements BytesEncoder<BytesWrapper> {
    INSTANCE;

    @Override
    public Encoding encoding() {
        return Encoding.JSON;
    }

    @Override
    public int sizeInBytes(BytesWrapper input) {
        return input.size();
    }

    @Override
    public byte[] encode(BytesWrapper input) {
        return input.bytes();
    }
}
