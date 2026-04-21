package org.sento.platform.webclient.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.sento.platform.webclient.filter.WebClientFilters;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.concurrent.TimeUnit;

@AutoConfiguration
@ConditionalOnClass(WebClient.class)
@EnableConfigurationProperties(InternalWebClientProperties.class)
public class InternalWebClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReactorClientHttpConnector internalReactorClientHttpConnector(InternalWebClientProperties properties) {
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
    @Order(0)
    public WebClientCustomizer internalWebClientCustomizer(
        ReactorClientHttpConnector connector,
        ExchangeFilterFunction contextPropagationFilter
    ) {
        return builder -> builder
            .clientConnector(connector)
            .filter(contextPropagationFilter);
    }

    @Bean(name = "loadBalancedWebClientBuilder")
    @LoadBalanced
    @ConditionalOnMissingBean(name = "loadBalancedWebClientBuilder")
    public WebClient.Builder loadBalancedWebClientBuilder(WebClientCustomizer internalWebClientCustomizer) {
        WebClient.Builder builder = WebClient.builder();
        internalWebClientCustomizer.customize(builder);
        return builder;
    }
}
