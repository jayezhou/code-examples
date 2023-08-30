package com.tpzwl.octopus.netty.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



/**
 * Netty ConfigurationProperties
 *
 * @author Jibeom Jung akka. Manty
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "netty")
public class NettyProperties {
    @NotNull
    @Size(min=1000, max=65535)
    private int tcpPort;

    @NotNull
    @Min(1)
    private int bossCount;

    @NotNull
    @Min(2)
    private int workerCount;

    @NotNull
    private boolean keepAlive;

    @NotNull
    private int backlog;
}
