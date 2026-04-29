package com.github.mawen12.agentx.api.report.sender;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

@Getter
@AllArgsConstructor
public class HttpConfig {
    protected String url;

    protected String username;

    protected String password;

    protected Duration timeout;

    protected int maxRequest;

    protected boolean enabled;

    protected boolean gzip;

    protected boolean isAuth;
}


