package br.com.mundim.reactiveflashcards.api.exceptionHandler;

import br.com.mundim.reactiveflashcards.domain.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
public class NotFoundHandler extends AbstractHandlerException<NotFoundException>{

    public NotFoundHandler(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    Mono<Void> handlerException(ServerWebExchange exchange, NotFoundException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, NOT_FOUND);
                    return ex.getMessage();
                }).map(message -> buildError(NOT_FOUND, message))
                .doFirst(() -> log.error("===== NotFoundException", ex))
                .flatMap(response -> writeResponse(exchange, response));
    }
}
