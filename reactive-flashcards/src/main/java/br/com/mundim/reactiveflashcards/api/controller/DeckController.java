package br.com.mundim.reactiveflashcards.api.controller;

import br.com.mundim.reactiveflashcards.api.controller.request.DeckRequest;
import br.com.mundim.reactiveflashcards.api.controller.response.DeckResponse;
import br.com.mundim.reactiveflashcards.api.mapper.DeckMapper;
import br.com.mundim.reactiveflashcards.core.validation.MongoId;
import br.com.mundim.reactiveflashcards.domain.service.DeckService;
import br.com.mundim.reactiveflashcards.domain.service.query.DeckQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("decks")
@Slf4j
@AllArgsConstructor
public class DeckController {

    public final DeckService deckService;
    public final DeckMapper deckMapper;
    public final DeckQueryService deckQueryService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<DeckResponse> save(@Valid @RequestBody final DeckRequest request){
        return deckService.save(deckMapper.toDocument(request))
                .doFirst(() -> log.info("===== try to save a follow deck {}", request))
                .map(deckMapper::toResponse);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Flux<DeckResponse> findAll(){
        return deckQueryService.findAll()
                .doFirst(() -> log.info("===== finding all decks"))
                .map(deckMapper::toResponse);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<DeckResponse> findById(@PathVariable @Valid @MongoId(message = "{deckController.id}") final String id){
        return deckQueryService.findById(id)
                .doFirst(() -> log.info("===== finding a deck with follow id {}", id))
                .map(deckMapper::toResponse);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<DeckResponse> update(@PathVariable @Valid @MongoId(message = "{deckController.id}") final String id,
                                     @Valid @RequestBody final DeckRequest request){
        return deckService.update(deckMapper.toDocument(request, id))
                .doFirst(() -> log.info("===== updating a deck with follow info [bodY: {}, id: {}]", request, id))
                .map(deckMapper::toResponse);
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{deckController.id}") final String id){
        return deckService.delete(id)
                .doFirst(() -> log.info("===== deleting a deck with follow id {}", id));
    }
}
