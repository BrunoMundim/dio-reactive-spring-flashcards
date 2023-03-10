package br.com.mundim.reactiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UserRequest(@NotBlank
                          @Size(min = 1, max = 256)
                          @JsonProperty("name")
                          @Schema(description = "Nome do usuario", example = "Bruno")
                          String name,
                          @NotBlank
                          @Size(min = 1, max = 256)
                          @Email
                          @JsonProperty("email")
                          @Schema(description = "Email do usuario", example = "bruno@email.com")
                          String email) {
    @Builder(toBuilder = true)
    public UserRequest { };

}
