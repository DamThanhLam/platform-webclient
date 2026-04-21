package org.sento.platform.webclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

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

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getResponseTimeout() {
        return responseTimeout;
    }

    public void setResponseTimeout(Duration responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public Duration getPendingAcquireTimeout() {
        return pendingAcquireTimeout;
    }

    public void setPendingAcquireTimeout(Duration pendingAcquireTimeout) {
        this.pendingAcquireTimeout = pendingAcquireTimeout;
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }

    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    public String getTraceHeaderName() {
        return traceHeaderName;
    }

    public void setTraceHeaderName(String traceHeaderName) {
        this.traceHeaderName = traceHeaderName;
    }

    public String getTraceContextKey() {
        return traceContextKey;
    }

    public void setTraceContextKey(String traceContextKey) {
        this.traceContextKey = traceContextKey;
    }

    public String getAuthContextKey() {
        return authContextKey;
    }

    public void setAuthContextKey(String authContextKey) {
        this.authContextKey = authContextKey;
    }
}
