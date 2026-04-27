package com.github.mawen12.agentx.core.utils;

public class DataSize {

    private static final long BYTES_PER_KB = 1024;

    private static final long BYTES_PER_MB = BYTES_PER_KB * 1024;

    private static final long BYTES_PER_GB = BYTES_PER_MB * 1024;

    private static final long BYTES_PER_TB = BYTES_PER_GB * 1024;

    private final long bytes;

    private DataSize(long bytes) {
        this.bytes = bytes;
    }

    public static DataSize ofBytes(long bytes) {
        return new DataSize(bytes);
    }

    public static DataSize ofKiloBytes(long kiloBytes) {
        return new DataSize(Math.multiplyExact(kiloBytes, BYTES_PER_KB));
    }

    public static DataSize ofMegaBytes(long megaBytes) {
        return new DataSize(Math.multiplyExact(megaBytes, BYTES_PER_MB));
    }

    public static DataSize ofGigaBytes(long gigaBytes) {
        return new DataSize(Math.multiplyExact(gigaBytes, BYTES_PER_GB));
    }

    public static DataSize ofTeraBytes(long teraBytes) {
        return new DataSize(Math.multiplyExact(teraBytes, BYTES_PER_TB));
    }

    public long toBytes() {
        return this.bytes;
    }
}
