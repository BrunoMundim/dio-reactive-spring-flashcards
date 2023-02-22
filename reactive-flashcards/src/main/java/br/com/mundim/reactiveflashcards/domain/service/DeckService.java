package br.com.mundim.reactiveflashcards.domain.service;

import br.com.mundim.reactiveflashcards.domain.document.DeckDocument;
import br.com.mundim.reactiveflashcards.domain.repository.DeckRepository;
import br.com.mundim.reactiveflashcards.domain.service.query.DeckQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;

    private final DeckQueryService deckQueryService;

    public Mono<DeckDocument> save(final DeckDocument document){
        return deckRepository.save(document)
                .doFirst(() -> log.info("===== try to save a follow deck {}", document));
    }

    public Mono<DeckDocument> update(final DeckDocument document){
        return deckQueryService.findById(document.id())
                .map(deck -> document.toBuilder()
                        .createdAt(document.createdAt())
                        .updatedAt(document.updatedAt())
                        .build())
                .flatMap(deckRepository::save)
                .doFirst(() -> log.info("===== try to update a deck with follow info {}", document));
    }

    public Mono<Void> delete(final String id){
        return deckQueryService.findById(id)
                .flatMap(deckRepository::delete)
                .doFirst(() -> log.info("===== try to delete a deck with follow id {}", id));
    }

}
