package org.sento.platform.webclient.support;

import org.sento.platform.webclient.exception.InternalWebClientHttpException;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public final class WebClientResponseMapper {

    private WebClientResponseMapper() {
    }

    public static Mono<Throwable> toException(ClientResponse response) {
        HttpStatusCode status = response.statusCode();
        return response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .map(body -> new InternalWebClientHttpException(status.value(), body));
    }
}
