package com.github.mawen12.agentx.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static ObjectMapper MAPPER = new ObjectMapper();

    public static <T> byte[] encode(T input) {
        try {
            return MAPPER.writeValueAsBytes(input);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
