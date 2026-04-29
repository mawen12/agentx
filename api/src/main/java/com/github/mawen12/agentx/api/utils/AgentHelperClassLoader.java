package com.github.mawen12.agentx.api.utils;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AgentHelperClassLoader extends URLClassLoader {
    private static final ConcurrentMap<URL, URL> helpUrls = new ConcurrentHashMap<>();

    public static void registerUrl(Class<?> clazz) {
        URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
        helpUrls.putIfAbsent(url, url);
    }

    public static AgentHelperClassLoader getClassLoader(ClassLoader parent, ClassLoader agent) {
        URL[] urls;
        if (helpUrls.isEmpty()) {
            urls = new URL[0];
        } else {
            urls = helpUrls.keySet().toArray(new URL[0]);
        }

        return new AgentHelperClassLoader(urls, parent, agent);
    }

    private final ClassLoader agent;

    public AgentHelperClassLoader(URL[] urls, ClassLoader parent, ClassLoader agent) {
        super(urls, parent);
        this.agent = agent;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            try {
                Class<?> aClass = agent.loadClass(name);
                if (resolve) {
                    resolveClass(aClass);
                }
                return aClass;
            } catch (ClassNotFoundException ignored) {

            }
            throw e;
        }
    }

    @Override
    public URL findResource(String name) {
        URL url = super.findResource(name);
        if (url == null) {
            try {
                url = agent.getResource(name);
            } catch (Exception ignored) {

            }
        }
        return url;
    }
}
