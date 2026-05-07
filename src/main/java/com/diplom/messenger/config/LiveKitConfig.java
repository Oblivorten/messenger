package com.diplom.messenger.config;

import io.livekit.server.AccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LiveKitConfig {

    @Value("${livekit.api-key}")
    public String apiKey;

    @Value("${livekit.api-secret}")
    public String apiSecret;

    @Value("${livekit.url}")
    public String url;
}