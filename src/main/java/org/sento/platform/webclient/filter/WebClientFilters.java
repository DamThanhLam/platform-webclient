package org.sento.platform.webclient.filter;

import org.sento.platform.common.constants.HeaderConstants;
import org.sento.platform.common.constants.MdcConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientFilters {

    @Bean
    public ExchangeFilterFunction contextPropagationFilter() {
        return (request, next) ->
            Mono.deferContextual(contextView -> {

                ClientRequest.Builder builder = ClientRequest.from(request);

                addHeaderIfPresent(contextView, MdcConstants.TRACE_ID,
                    HeaderConstants.TRACE_ID, builder);

                addHeaderIfPresent(contextView, MdcConstants.USER_ID,
                    HeaderConstants.USER_ID, builder);

                addHeaderIfPresent(contextView, MdcConstants.ROLES,
                    HeaderConstants.ROLES, builder);

                return next.exchange(builder.build());
            });
    }

    private void addHeaderIfPresent(
        reactor.util.context.ContextView contextView,
        String contextKey,
        String headerName,
        ClientRequest.Builder builder
    ) {
        if (!contextView.hasKey(contextKey)) {
            return;
        }

        Object value = contextView.get(contextKey);

        String headerValue = String.valueOf(value);

        if (!headerValue.isBlank()) {
            builder.header(headerName, headerValue);
        }
    }
}