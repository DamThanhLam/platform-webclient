package org.sento.platform.webclient.filter;

import lombok.extern.slf4j.Slf4j;
import org.sento.platform.common.constants.HeaderConstants;
import org.sento.platform.common.constants.MdcConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

@Slf4j
@Component
public class ContextPropagationFilter implements ExchangeFilterFunction {

    @Override
    public Mono<org.springframework.web.reactive.function.client.ClientResponse> filter(
        ClientRequest request,
        ExchangeFunction next
    ) {
        return Mono.deferContextual(contextView -> {
            ClientRequest newRequest = buildRequestWithHeaders(request, contextView);
            return next.exchange(newRequest);
        });
    }

    private ClientRequest buildRequestWithHeaders(ClientRequest request, ContextView context) {

        return ClientRequest.from(request)
            .headers(headers -> {
                addHeaderIfPresent(context, headers, HeaderConstants.TRACE_ID, HeaderConstants.TRACE_ID);
                addHeaderIfPresent(context, headers, HeaderConstants.USER_ID, HeaderConstants.USER_ID);
                addHeaderIfPresent(context, headers, HeaderConstants.ROLES, HeaderConstants.ROLES);
            })
            .build();
    }

    private void addHeaderIfPresent(
        ContextView context,
        HttpHeaders headers,
        String contextKey,
        String headerName
    ) {
        if (!context.hasKey(contextKey)) return;

        Object value = context.get(contextKey);

        String headerValue = value.toString();
        if (!headerValue.isBlank()) {
            headers.set(headerName, headerValue);
        }
    }
}
