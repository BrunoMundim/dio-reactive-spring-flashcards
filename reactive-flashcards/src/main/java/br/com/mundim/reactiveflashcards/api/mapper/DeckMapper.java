package br.com.mundim.reactiveflashcards.api.mapper;

import br.com.mundim.reactiveflashcards.api.controller.request.DeckRequest;
import br.com.mundim.reactiveflashcards.api.controller.response.DeckResponse;
import br.com.mundim.reactiveflashcards.domain.document.DeckDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeckMapper {

    DeckDocument toDocument(final DeckRequest request);

    DeckDocument toDocument(final DeckRequest request, final String id);

    DeckResponse toResponse(final DeckDocument document);

}
