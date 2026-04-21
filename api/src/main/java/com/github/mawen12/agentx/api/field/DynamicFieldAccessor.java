package com.github.mawen12.agentx.api.field;

public interface DynamicFieldAccessor {
    String FIELD_NAME = "agent$$Field$$Data";

    void setAgent$$Field$$Data(Object data);

    Object getAgent$$Field$$Data();
}
