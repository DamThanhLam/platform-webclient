package org.sento.platform.webclient.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Setter
@Getter
@ConfigurationProperties(prefix = "internal.webclient")
public class InternalWebClientProperties {

    /**
     * TCP connect timeout.
     */
    private Duration connectTimeout = Duration.ofSeconds(3);

    /**
     * Overall response timeout.
     */
    private Duration responseTimeout = Duration.ofSeconds(5);

    /**
     * Reactor Netty connection pool max connections.
     */
    private int maxConnections = 200;

    /**
     * How long to wait for a connection from the pool.
     */
    private Duration pendingAcquireTimeout = Duration.ofSeconds(2);

    /**
     * Whether to log requests/responses.
     */
    private boolean enableLogging = true;

    /**
     * Header name used for trace correlation.
     */
    private String traceHeaderName = "X-Trace-Id";

    /**
     * Reactor Context key for trace id.
     */
    private String traceContextKey = "traceId";

    /**
     * Reactor Context key for auth token.
     */
    private String authContextKey = "authorization";

}
