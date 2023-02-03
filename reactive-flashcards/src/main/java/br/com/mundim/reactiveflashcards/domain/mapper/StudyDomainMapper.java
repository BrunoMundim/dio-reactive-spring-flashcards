package br.com.mundim.reactiveflashcards.domain.mapper;

import br.com.mundim.reactiveflashcards.domain.document.Card;
import br.com.mundim.reactiveflashcards.domain.document.Question;
import br.com.mundim.reactiveflashcards.domain.document.StudyCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface StudyDomainMapper {

    StudyCard toStudyCard (final Card card);

    default Question generateRandomQuestion(final Set<StudyCard> cards){
        var values = new ArrayList<>(cards);
        var random = new Random();
        var position = random.nextInt(values.size());
        return toQuestion(values.get(position));
    }

    @Mapping(target = "asked", source = "front")
    @Mapping(target = "askedIn", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "answered", ignore = true)
    @Mapping(target = "answeredIn", ignore = true)
    @Mapping(target = "expected", source = "back")
    Question toQuestion (final StudyCard card);

}
