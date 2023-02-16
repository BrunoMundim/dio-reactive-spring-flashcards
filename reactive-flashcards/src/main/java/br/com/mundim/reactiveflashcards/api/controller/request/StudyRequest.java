package br.com.mundim.reactiveflashcards.api.controller.request;

import br.com.mundim.reactiveflashcards.core.validation.MongoId;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record StudyRequest(@MongoId @JsonProperty("userId")
                           @Schema(description = "Identificador do usuário", example = "63ee49128dfeea11b7549a10", format = "UUID")
                           String userId,
                           @MongoId @JsonProperty("deckId")
                           @Schema(description = "Identificador do deck", example = "63ed6ba222d5f83268d54c42", format = "UUID")
                           String deckId) {

    @Builder(toBuilder = true)
    public StudyRequest { };

}
