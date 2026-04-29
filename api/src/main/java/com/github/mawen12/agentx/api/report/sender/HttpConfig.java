package com.github.mawen12.agentx.api.report.sender;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

@Getter
@AllArgsConstructor
public class HttpConfig {
    protected String url = "localhost:9082";

    protected String username;

    protected String password;

    protected Duration timeout = Duration.ofSeconds(5);

    protected int maxRequest = 2;

    protected boolean enabled = true;

    protected boolean gzip = true;

    protected boolean isAuth = false;
}


