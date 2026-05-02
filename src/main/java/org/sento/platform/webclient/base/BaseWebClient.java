package org.sento.platform.webclient.base;

import lombok.RequiredArgsConstructor;
import org.sento.platform.webclient.filter.ContextPropagationFilter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BaseWebClient {

    private final WebClient.Builder builder;
    private final ContextPropagationFilter contextFilter;

    private WebClient buildClient() {
        return builder
            .filter(contextFilter)
            .build();
    }

    protected <T> Mono<T> get(
        String uri,
        ParameterizedTypeReference<T> responseType
    ) {
        return buildClient()
            .get()
            .uri(uri)
            .retrieve()
            .bodyToMono(responseType);
    }

    protected <T, B> Mono<T> post(
        String uri,
        B body,
        ParameterizedTypeReference<T> responseType
    ) {
        return buildClient()
            .post()
            .uri(uri)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(responseType);
    }

    protected <T, B> Mono<T> put(
        String uri,
        B body,
        ParameterizedTypeReference<T> responseType
    ) {
        return buildClient()
            .put()
            .uri(uri)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(responseType);
    }

    protected <T> Mono<T> delete(
        String uri,
        ParameterizedTypeReference<T> responseType
    ) {
        return buildClient()
            .delete()
            .uri(uri)
            .retrieve()
            .bodyToMono(responseType);
    }
}
