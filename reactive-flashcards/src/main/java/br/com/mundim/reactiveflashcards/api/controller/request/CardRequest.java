package br.com.mundim.reactiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CardRequest(@JsonProperty("front") @NotBlank @Size(min = 1, max = 255)
                          @Schema(description = "Pergunta do card", example = "Blue")
                          String front,
                          @JsonProperty("back") @NotBlank @Size(min = 1, max = 255)
                          @Schema(description = "Resposta do card", example = "Azul")
                          String back) {

    @Builder(toBuilder = true)
    public CardRequest { }

}
