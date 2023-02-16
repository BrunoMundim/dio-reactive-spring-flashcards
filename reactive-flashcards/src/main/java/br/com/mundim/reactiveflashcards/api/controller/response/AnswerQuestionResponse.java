package br.com.mundim.reactiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.util.Objects;

public record AnswerQuestionResponse(@JsonProperty("asked")
                                     @Schema(description = "Pergunta feita", example = "Blue")
                                     String asked,
                                     @JsonProperty("askedIn")
                                     @Schema(description = "Momento em que a pergunta foi gerada", example = "2023-02-16T16:54:51.48656766Z", format = "datetime")
                                     OffsetDateTime askedIn,
                                     @JsonProperty("answered")
                                     @Schema(description = "Resposta do usuário", example = "Azul")
                                     String answered,
                                     @JsonProperty("answeredIn")
                                     @Schema(description = "Momento em que a pergunta foi respondida", example = "2023-02-16T16:54:51.48657011Z", format = "datetime")
                                     OffsetDateTime answeredIn,
                                     @JsonProperty("expected")
                                     @Schema(description = "Resposta esperada", example = "Azul")
                                     String expected) {

    @Builder(toBuilder = true)
    public AnswerQuestionResponse {
    }
}
