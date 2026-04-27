package com.github.mawen12.agentx.core.plugins.jdbc.common;

import com.github.mawen12.agentx.api.Agent;
import com.github.mawen12.agentx.api.logging.Logger;
import com.github.mawen12.agentx.core.utils.DataSize;
import com.github.mawen12.agentx.core.utils.StringUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public enum DataSizeSqlCompression implements SqlCompression {
    INSTANCE;

    private static final Logger LOGGER = Agent.getLogger(DataSizeSqlCompression.class);
    private static final DataSize MAX_SQL_SIZE = DataSize.ofKiloBytes(32);
    private static final Cache<String, String> SQL_CACHE = CacheBuilder.newBuilder().maximumSize(200).build();

    @Override
    public String compress(String origin) {
        try {
            return StringUtils.cutStrByDataSize(origin, MAX_SQL_SIZE);
        } catch (Exception e) {
            LOGGER.warn("compress content[{}] failure", origin);
            return origin;
        }
    }
}
