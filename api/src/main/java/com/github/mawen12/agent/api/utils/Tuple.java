package com.github.mawen12.agent.api.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Tuple<X, Y> {
    private final X x;
    private final Y y;
}
