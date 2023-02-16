package br.com.mundim.reactiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record ProblemResponse(@JsonProperty("status")
                              @Schema(description = "HttpStatus retornado", example = "400")
                              Integer status,
                              @JsonProperty("timestamp")
                              @Schema(description = "Momento em que o erro aconteceu", example = "2023-02-16T16:54:51.48656766Z", format = "datetime")
                              OffsetDateTime timestamp,
                              @JsonProperty("errorDescription")
                              @Schema(description = "Descrição do erro", example = "Sua requisição tem informações inválidas")
                              String errorDescription,
                              @JsonProperty("fields")
                              @Schema(description = "Caso a requisição tenha parâmetros inválidos aqui serão informados os erros referentes aos mesmos")
                              List<ErrorFieldResponse> fields) {

    @Builder(toBuilder = true)
    public ProblemResponse { };
}
