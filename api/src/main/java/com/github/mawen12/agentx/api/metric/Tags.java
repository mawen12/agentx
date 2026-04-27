package com.github.mawen12.agentx.api.metric;

import lombok.Data;

@Data
public class Tags {
    private final String category;
    private final String type;
    private final String keyFieldName;
    private String help;

    public Tags(String category, String type, String keyFieldName) {
        this.category = category;
        this.type = type;
        this.keyFieldName = keyFieldName;
    }

    public Tags(String category, String type, String keyFieldName, String help) {
        this.category = category;
        this.type = type;
        this.keyFieldName = keyFieldName;
        this.help = help;
    }
}
