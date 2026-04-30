package com.github.mawen12.agentx.loader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;

public class AgentClassLoader extends URLClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    private final Set<ClassLoader> externals = Collections.newSetFromMap(new WeakHashMap<>());

    public AgentClassLoader(URL[] urls) {
        super(urls, null);
    }

    public void add(ClassLoader cl) {
        if (cl != null && !Objects.equals(cl, this)) {
            externals.add(cl);
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            for (ClassLoader cl : externals) {
                try {
                    Class<?> aClass = cl.loadClass(name);
                    if (resolve) {
                        resolveClass(aClass);
                    }
                    return aClass;
                } catch (ClassNotFoundException ignored) {
                }
            }

            throw e;
        }
    }

    @Override
    public URL findResource(String name) {
        URL url = super.findResource(name);
        if (url == null) {
            for (ClassLoader cl : externals) {
                try {
                    url = cl.getResource(name);
                    if (url != null) {
                        break;
                    }
                } catch (Exception ignored) {

                }
            }
        }
        return url;
    }

}
