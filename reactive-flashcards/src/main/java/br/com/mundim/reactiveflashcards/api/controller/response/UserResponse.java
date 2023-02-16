package br.com.mundim.reactiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record UserResponse(@JsonProperty("id")
                           @Schema(description = "Identificador do usuario", example = "63ed6b3322d5f83268d54c3f", format = "UUID")
                           String id,
                           @JsonProperty("name")
                           @Schema(description = "Nome do usuario", example = "Bruno")
                           String name,
                           @JsonProperty("email")
                           @Schema(description = "Email do usuario", example = "bruno@email.com")
                           String email) {

    @Builder(toBuilder = true)
    public UserResponse { }


}
