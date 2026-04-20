package com.github.mawen12.agent.api.metric;

import lombok.Data;

@Data
public class Tags {
    private final String category;
    private final String type;
    private final String keyFieldName;
}
