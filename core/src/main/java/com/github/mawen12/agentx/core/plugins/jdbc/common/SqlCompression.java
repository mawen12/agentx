package com.github.mawen12.agentx.core.plugins.jdbc.common;

public interface SqlCompression {

    SqlCompression DEFAULT = origin -> origin;

    String compress(String origin);
}
