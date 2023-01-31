package br.com.mundim.reactiveflashcards.domain.document;

import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.util.Set;

public record StudyDeck(String deckId, Set<StudyCard> cards) {

    @Builder(toBuilder = true)
    public StudyDeck {}

}
