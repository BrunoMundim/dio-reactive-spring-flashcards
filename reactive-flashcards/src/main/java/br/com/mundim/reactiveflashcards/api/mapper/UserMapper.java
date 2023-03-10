package br.com.mundim.reactiveflashcards.api.mapper;

import br.com.mundim.reactiveflashcards.api.controller.request.UserRequest;
import br.com.mundim.reactiveflashcards.api.controller.response.UserPageResponse;
import br.com.mundim.reactiveflashcards.api.controller.response.UserResponse;
import br.com.mundim.reactiveflashcards.domain.document.UserDocument;
import br.com.mundim.reactiveflashcards.domain.dto.UserPageDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDocument toDocument(final UserRequest request);

    UserDocument toDocument(final UserRequest request, final String id);

    UserResponse toResponse(final UserDocument document);

    UserPageResponse toResponse(final UserPageDocument document, final Integer limit);

}
