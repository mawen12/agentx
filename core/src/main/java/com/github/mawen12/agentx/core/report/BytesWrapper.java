package com.github.mawen12.agentx.core.report;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BytesWrapper {

    private final byte[] bytes;

    public int size() {
        return bytes.length;
    }

    public byte[] bytes() {
        return bytes;
    }
}
