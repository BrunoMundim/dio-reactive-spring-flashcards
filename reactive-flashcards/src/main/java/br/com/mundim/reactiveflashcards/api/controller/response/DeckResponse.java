package br.com.mundim.reactiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Set;

public record DeckResponse(@JsonProperty("id")
                           @Schema(description = "Identificador do deck", example = "63ed6b4222d5f83268d54c40", format = "UUID")
                           String id,
                           @JsonProperty("name")
                           @Schema(description = "Nome do deck", example = "Estudo de inglês")
                           String name,
                           @JsonProperty("description")
                           @Schema(description = "Descrição do deck", example = "Deck de estudo de inglês para iniciantes")
                           String description,
                           @JsonProperty("cards")
                           @Schema(description = "Cards que compõem o deck")
                           Set<CardResponse> cards) {

    @Builder(toBuilder = true)
    public DeckResponse { }

}
