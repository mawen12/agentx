package com.github.mawen12.agentx.api.utils;

import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class Lists {

    public static <T> List<T> of(T... ts) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, ts);
        return list;
    }
}
