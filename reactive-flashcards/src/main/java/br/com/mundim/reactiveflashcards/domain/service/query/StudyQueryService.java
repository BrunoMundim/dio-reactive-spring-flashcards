package br.com.mundim.reactiveflashcards.domain.service.query;

import br.com.mundim.reactiveflashcards.domain.document.Question;
import br.com.mundim.reactiveflashcards.domain.document.StudyDocument;
import br.com.mundim.reactiveflashcards.domain.exception.NotFoundException;
import br.com.mundim.reactiveflashcards.domain.repository.StudyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static br.com.mundim.reactiveflashcards.domain.exception.BaseErrorMessage.*;

@Service
@Slf4j
@AllArgsConstructor
public class StudyQueryService {

    private final StudyRepository studyRepository;

    public Mono<StudyDocument> findPendingStudyByUserIdAndDeckId(final String userId, final String deckId){
        return studyRepository.findByUserIdAndCompleteFalseAndStudyDeck_DeckId(userId, deckId)
                .doFirst(() -> log.info("===== try to get pending study with userId {} and DeckId {}", userId, deckId))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new NotFoundException(STUDY_DECK_NOT_FOUND.params(userId, deckId).getMessage()))));
    }

    public Mono<StudyDocument> findById(final String id){
        return studyRepository.findById(id)
                .doFirst(() -> log.info("===== getting a study with id {}", id))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new NotFoundException(STUDY_NOT_FOUND.params(id).getMessage()))));
    }

    public Mono<Question> getLastPendingQuestion(final String id){
        return findById(id)
                .flatMap(this::verifyIfFinished)
                .flatMapMany(study -> Flux.fromIterable(study.questions()))
                .doFirst(() -> log.info("===== getting a current pending question in study {}", id))
                .filter(Question::isNotAnswered)
                .single();
    }

    public Mono<StudyDocument> verifyIfFinished(final StudyDocument document) {
        return Mono.just(document)
                .doFirst(() -> log.info("===== verify if study has some question without right answer"))
                .filter(study -> BooleanUtils.isFalse(document.complete()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(STUDY_QUESTION_NOT_FOUND
                        .params(document.id()).getMessage()))));

    }

}
