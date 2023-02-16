package br.com.mundim.reactiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record AnswerQuestionRequest(@JsonProperty("answer") @NotBlank @Size(min=1, max=255)
                                    @Schema(description = "Resposta da pergunta atual", example = "Azul")
                                    String answer) {

    @Builder(toBuilder = true)
    public AnswerQuestionRequest {
    }
}
