package br.com.mundim.reactiveflashcards.domain.service;

import br.com.mundim.reactiveflashcards.core.factorybot.document.DeckDocumentFactoryBot;
import br.com.mundim.reactiveflashcards.domain.document.DeckDocument;
import br.com.mundim.reactiveflashcards.domain.exception.NotFoundException;
import br.com.mundim.reactiveflashcards.domain.repository.DeckRepository;
import br.com.mundim.reactiveflashcards.domain.service.query.DeckQueryService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class DeckServiceTest {

    @Mock
    private DeckRepository deckRepository;
    @Mock
    private DeckQueryService deckQueryService;

    private DeckService deckService;

    @BeforeEach
    void setup(){
        deckService = new DeckService(deckRepository, deckQueryService);
    }

    @Test
    void saveTest() {
        var document = DeckDocumentFactoryBot.builder().preInsert().build();
        when(deckRepository.save(any())).thenAnswer(invocation -> {
            var deck = invocation.getArgument(0, DeckDocument.class);
            return Mono.just(deck.toBuilder()
                    .id(ObjectId.get().toString())
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build());
        });
        StepVerifier.create(deckService.save(document))
                .assertNext(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual).hasNoNullFieldsOrProperties();
                })
                .verifyComplete();
        verify(deckRepository).save(any());
        verifyNoInteractions(deckQueryService);
    }

    @Test
    void updateTest() {
        var storedDeck = DeckDocumentFactoryBot.builder().build();
        var document = DeckDocumentFactoryBot.builder()
                .preUpdate(storedDeck.id())
                .build();

        when(deckRepository.save(any())).thenAnswer(invocation -> {
            var deck = invocation.getArgument(0, DeckDocument.class);
            return Mono.just(deck.toBuilder().updatedAt(OffsetDateTime.now()).build());
        });
        when(deckQueryService.findById(any(String.class))).thenReturn(Mono.just(storedDeck));

        StepVerifier.create(deckService.update(document))
                .assertNext(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual).usingRecursiveComparison()
                            .ignoringFields("createdAt", "updatedAt")
                            .isEqualTo(document);
                    assertThat(actual.createdAt().toEpochSecond()).isEqualTo(storedDeck.createdAt().toEpochSecond());
                })
                .verifyComplete();
        verify(deckRepository).save(any());
        verify(deckQueryService).findById(any(String.class));
    }

    @Test
    void deleteTest() {
        var deckCaptor = ArgumentCaptor.forClass(DeckDocument.class);
        var storedDeck = DeckDocumentFactoryBot.builder().build();
        when(deckRepository.delete(deckCaptor.capture())).thenReturn(Mono.empty());
        when(deckQueryService.findById(anyString())).thenReturn(Mono.just(storedDeck));
        StepVerifier.create(deckService.delete(ObjectId.get().toString())).verifyComplete();
        var capturedDeck = deckCaptor.getValue();
        assertThat(capturedDeck).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(storedDeck);
        assertThat(capturedDeck.createdAt().toEpochSecond()).isEqualTo(storedDeck.createdAt().toEpochSecond());
        assertThat(capturedDeck.updatedAt().toEpochSecond()).isEqualTo(storedDeck.updatedAt().toEpochSecond());
        verify(deckRepository).delete(any(DeckDocument.class));
        verify(deckQueryService).findById(anyString());
    }

    @Test
    void whenTryToUpdateNonStoredDeckThenThrowError() {
        var document = DeckDocumentFactoryBot.builder().build();
        when(deckQueryService.findById(any(String.class))).thenReturn(Mono.error(new NotFoundException("")));
        StepVerifier.create(deckService.update(document))
                .verifyError(NotFoundException.class);
        verify(deckQueryService).findById(any(String.class));
        verify(deckRepository, times(0)).save(any());
    }

    @Test
    void whenTryToDeleteNonStoredDeckThenThrowError(){
        when(deckQueryService.findById(any(String.class))).thenReturn(Mono.error(new NotFoundException("")));
        StepVerifier.create(deckService.delete(ObjectId.get().toString()))
                .verifyError(NotFoundException.class);
        verify(deckQueryService).findById(any(String.class));
        verify(deckRepository, times(0)).delete(any(DeckDocument.class));
    }

}
