package br.com.mundim.reactiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record CardResponse(@JsonProperty("front")
                           @Schema(description = "Pergunta do card", example = "Blue")
                           String front,
                           @JsonProperty("back")
                           @Schema(description = "Resposta do card", example = "Azul")
                           String back) {

    @Builder(toBuilder = true)
    public CardResponse { }

}
