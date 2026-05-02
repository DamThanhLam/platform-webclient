package org.sento.platform.webclient.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.concurrent.TimeUnit;

@ComponentScan("org.sento.platform.webclient")
@AutoConfiguration
@EnableConfigurationProperties(InternalWebClientProperties.class)
public class InternalWebClientAutoConfiguration {

    @Bean
    public ReactorClientHttpConnector reactorClientHttpConnector(InternalWebClientProperties properties) {
        ConnectionProvider provider = ConnectionProvider.builder("internal-webclient-pool")
            .maxConnections(properties.getMaxConnections())
            .pendingAcquireTimeout(properties.getPendingAcquireTimeout())
            .build();

        HttpClient httpClient = HttpClient.create(provider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Math.toIntExact(properties.getConnectTimeout().toMillis()))
            .responseTimeout(properties.getResponseTimeout())
            .doOnConnected(connection ->
                connection.addHandlerLast(new ReadTimeoutHandler(
                    properties.getResponseTimeout().toMillis(),
                    TimeUnit.MILLISECONDS)));

        return new ReactorClientHttpConnector(httpClient);
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
