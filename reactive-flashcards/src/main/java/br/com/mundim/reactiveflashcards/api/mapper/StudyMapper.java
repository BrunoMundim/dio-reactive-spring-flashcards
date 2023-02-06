package br.com.mundim.reactiveflashcards.api.mapper;

import br.com.mundim.reactiveflashcards.api.controller.request.StudyRequest;
import br.com.mundim.reactiveflashcards.api.controller.response.AnswerQuestionResponse;
import br.com.mundim.reactiveflashcards.api.controller.response.QuestionResponse;
import br.com.mundim.reactiveflashcards.domain.document.Question;
import br.com.mundim.reactiveflashcards.domain.document.StudyDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface StudyMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studyDeck.deckId", source = "deckId")
    @Mapping(target = "studyDeck.cards", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    StudyDocument toDocument(final StudyRequest request);

    QuestionResponse toResponse(final Question question, final String id);

    AnswerQuestionResponse toResponse(final Question question);

}
