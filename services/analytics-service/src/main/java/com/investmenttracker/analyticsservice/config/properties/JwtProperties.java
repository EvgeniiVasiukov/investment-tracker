package com.investmenttracker.analyticsservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.crypto.SecretKey;

@ConfigurationProperties(prefix = "jwt")
@EnableConfigurationProperties(JwtProperties.class)
@Getter
@Setter
public class JwtProperties {
    private String secret;
}
