package br.com.mundim.reactiveflashcards.api.controller.documentation;

import br.com.mundim.reactiveflashcards.api.controller.request.AnswerQuestionRequest;
import br.com.mundim.reactiveflashcards.api.controller.request.StudyRequest;
import br.com.mundim.reactiveflashcards.api.controller.response.AnswerQuestionResponse;
import br.com.mundim.reactiveflashcards.api.controller.response.QuestionResponse;
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
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Study", description = "Endpoint para gerenciar estudos")
public interface StudyControllerDoc {

    @Operation(summary = "Endpoint para iniciar o estudo de um deck")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "O estudo foi criado e retorna a primeira pergunta gerada",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = QuestionResponse.class))})
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<QuestionResponse> start(@Valid @RequestBody StudyRequest request);

    @Operation(summary = "Endpoint para buscar a última pergunta não respondida")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retorna a última pergunta que não foi respondida corretamente",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = QuestionResponse.class))})
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}/current-question")
    Mono<QuestionResponse> getCurrentQuestion(@Parameter(description = "Identificador do estudo", example = "63ed6b4222d5f83268d54c40")
                                              @Valid @MongoId(message = "{studyController.id}") @PathVariable String id);

    @Operation(summary = "Endpoint para responder a pergunta atual")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retorna a pergunta, a resposta fornecida e a resposta esperada",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AnswerQuestionResponse.class))})
    })
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE, value = "{id}/answer")
    Mono<AnswerQuestionResponse> answer(@Parameter(description = "Identificador do estudo", example = "63ed6b4222d5f83268d54c40") @PathVariable@Valid @MongoId(message = "{studyController.id}") String id,
                                        @Valid @RequestBody AnswerQuestionRequest request);
}
