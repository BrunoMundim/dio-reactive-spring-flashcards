package br.com.mundim.reactiveflashcards.domain.service.query;

import br.com.mundim.reactiveflashcards.domain.document.DeckDocument;
import br.com.mundim.reactiveflashcards.domain.exception.NotFoundException;
import br.com.mundim.reactiveflashcards.domain.repository.DeckRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static br.com.mundim.reactiveflashcards.domain.exception.BaseErrorMessage.DECK_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class DeckQueryService {

    private final DeckRepository deckRepository;

    public Mono<DeckDocument> findById(final String id){
        return deckRepository.findById(id).
                doFirst(() -> log.info("===== try to find deck by id {}", id))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new NotFoundException(DECK_NOT_FOUND.params(id).getMessage()))));
    }

    public Flux<DeckDocument> findAll() {
        return deckRepository.findAll()
                .doFirst(() -> log.info("===== try to get all decks"));
    }

}
