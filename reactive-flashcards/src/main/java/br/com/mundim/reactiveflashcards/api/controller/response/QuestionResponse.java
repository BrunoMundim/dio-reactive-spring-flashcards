package br.com.mundim.reactiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;

public record QuestionResponse(@JsonProperty("id")
                               @Schema(description = "Identificador do estudo", example = "63ed6b3322d5f83268d54c3f", format = "UUID")
                               String id,
                               @JsonProperty("asked")
                               @Schema(description = "Pergunta atual do estudo", example = "Azul")
                               String asked,
                               @JsonProperty("askedIn")
                               @Schema(description = "Momento em que a pergunta foi gerada", example = "2023-02-16T16:54:51.48656766Z", format = "datetime")
                               OffsetDateTime askedIn) {

    @Builder(toBuilder = true)
    public QuestionResponse { }
}
