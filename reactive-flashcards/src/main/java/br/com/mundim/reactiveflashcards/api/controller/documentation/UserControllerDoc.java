package br.com.mundim.reactiveflashcards.api.controller.documentation;

import br.com.mundim.reactiveflashcards.api.controller.request.UserPageRequest;
import br.com.mundim.reactiveflashcards.api.controller.request.UserRequest;
import br.com.mundim.reactiveflashcards.api.controller.response.UserPageResponse;
import br.com.mundim.reactiveflashcards.api.controller.response.UserResponse;
import br.com.mundim.reactiveflashcards.core.validation.MongoId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Customer", description = "Endpoints para manipulação de usuários")
public interface UserControllerDoc {
    @Operation(summary = "Endpoint para criar um novo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Retorna o usuário criado",
                content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<UserResponse> save(@Valid @RequestBody UserRequest request);

    @Operation(summary = "Endpoint para buscar um usuário pelo seu identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retorna o usuário correspondente ao identificador",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<UserResponse> findById(@Parameter(description = "Identificador do usuário", example = "63ed6b4222d5f83268d54c40") @PathVariable
                                @Valid @MongoId(message = "{userController.id}") String id);

    @Operation(summary = "Endpoint para buscar usuários de forma paginada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retorna os usuários de acordo com as informações passadas na request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserPageResponse.class))})
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Mono<UserPageResponse> findOnDemand(@Valid UserPageRequest request);

    @Operation(summary = "Endpoint para atualizar um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retorna o usuário atualizado",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    })
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<UserResponse> update(@Parameter(description = "Identificador do usuário", example = "63ed6b4222d5f83268d54c40") @PathVariable
                              @Valid @MongoId(message = "{userController.id}") String id,
                              @Valid @RequestBody UserRequest request);

    @Operation(summary = "Endpoint para excluir um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "O usuário foi excluído")
    })
    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> delete(@Parameter(description = "Identificador do usuário", example = "63ed6b4222d5f83268d54c40") @PathVariable
                      @Valid @MongoId(message = "{userController.id}") String id);
}
