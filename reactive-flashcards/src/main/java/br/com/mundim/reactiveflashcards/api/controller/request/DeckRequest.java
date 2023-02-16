package br.com.mundim.reactiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public record DeckRequest(@JsonProperty("name") @NotBlank @Size(min = 1, max = 255)
                          @Schema(description = "Nome do deck", example = "Estudo de inglês")
                          String name,
                          @JsonProperty("description") @NotBlank @Size(min = 1, max = 255)
                          @Schema(description = "Descrição do deck", example = "Deck de estudo de inglês para iniciantes")
                          String description,
                          @JsonProperty("cards") @Valid @Size(min = 3) @NotNull
                          @Schema(description = "Cards que compõem o deck")
                          Set<CardRequest> cards) {

    @Builder(toBuilder = true)
    public DeckRequest {};

}
