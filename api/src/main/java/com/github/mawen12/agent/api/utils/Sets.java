package com.github.mawen12.agent.api.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Sets {
    public static <T> Set<T> of(T... ts) {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, ts);
        return set;
    }
}
