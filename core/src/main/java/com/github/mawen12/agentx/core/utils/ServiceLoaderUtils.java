package com.github.mawen12.agentx.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceLoaderUtils {

    public static <T> List<T> load(Class<T> serviceClass) {
        List<T> result = new ArrayList<>();
        ServiceLoader<T> services = ServiceLoader.load(serviceClass);
        for (T service : services) {
            result.add(service);
        }
        return result;
    }
}
