package com.github.mawen12.agentx.api.plugins;

import com.github.mawen12.agentx.api.config.ConfigAware;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface Plugin extends ConfigAware {

    boolean isEnabled();

    Domain domain();

    Component component();

    @Getter
    @AllArgsConstructor
    enum Domain {
        DRUID("druid"),
        HTTP_SERVLET("http-servlet"),
        JDBC("jdbc"),
        JVM("jvm"),
        LOG4J2("log4j2"),
        LOGBACK("logback"),
        SPRING_BOOT("spring-boot"),
        TOMCAT("tomcat");

        private final String name;
    }

    @Getter
    @AllArgsConstructor
    enum Component {
        DATASOURCE("datasource"),
        CONNECTION("connection"),
        STATEMENT("statement"),
        SERVLET("servlet"),
        GC("gc"),
        MEMORY("memory"),
        LOG("log"),
        ENDPOINT("endpoint"),
        READ_EVENT("ready-event");

        private final String name;
    }
}
