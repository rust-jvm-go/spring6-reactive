package initiative.guru.spring6plus.spring6reactive.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class GlobalErrorHandlerConfig implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    @Nonnull
    @SneakyThrows
    public Mono<Void> handle(@Nonnull final ServerWebExchange exchange, @Nonnull final Throwable throwable) {

        if (throwable instanceof WebExchangeBindException wbe) {

            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            exchange.getResponse().getHeaders().setLocation(URI.create(exchange.getRequest().getURI().getPath()));

            final Map<String, String> messageMap = new HashMap<>();
            messageMap.put("request id", exchange.getRequest().getId());
            messageMap.put("method", exchange.getRequest().getMethod().name());
            messageMap.put("status", String.valueOf(wbe.getStatusCode().value()));
            messageMap.put("code", HttpStatus.valueOf(wbe.getStatusCode().value()).name());
            messageMap.put("reason", wbe.getReason());
            messageMap.put("message", getValidationErrors(wbe));

            try {
                return writeResponse(exchange, objectMapper.writeValueAsBytes(messageMap));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            return Mono.error(throwable);
        }
    }

    private String getValidationErrors(final WebExchangeBindException wbe) {
        return wbe
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));
    }

    private Mono<Void> writeResponse(final ServerWebExchange exchange, final byte[] responseBytes) {
        return exchange
            .getResponse()
            .writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(responseBytes))
            );
    }
}
