package br.com.mundim.reactiveflashcards.domain.service.query;

import br.com.mundim.reactiveflashcards.api.controller.request.UserPageRequest;
import br.com.mundim.reactiveflashcards.domain.document.UserDocument;
import br.com.mundim.reactiveflashcards.domain.dto.UserPageDocument;
import br.com.mundim.reactiveflashcards.domain.exception.EmailAlreadyUsedException;
import br.com.mundim.reactiveflashcards.domain.exception.NotFoundException;
import br.com.mundim.reactiveflashcards.domain.repository.UserRepository;
import br.com.mundim.reactiveflashcards.domain.repository.UserRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static br.com.mundim.reactiveflashcards.domain.exception.BaseErrorMessage.EMAIL_ALREADY_USED;
import static br.com.mundim.reactiveflashcards.domain.exception.BaseErrorMessage.USER_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;
    private final UserRepositoryImpl userRepositoryImpl;

    public Mono<UserDocument> findById(final String id){
        return userRepository.findById(id)
                .doFirst(() -> log.info("===== Try to find user with id {}", id))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(USER_NOT_FOUND.params("id", id).getMessage()))));
    }

    public Mono<UserDocument> findByEmail(final String email){
        return userRepository.findByEmail(email)
                .doFirst(() -> log.info("===== Try to find user with email {}", email))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(USER_NOT_FOUND.params("email", email).getMessage()))));
    }

    public Mono<UserPageDocument> findOnDemand(final UserPageRequest request){
        return userRepositoryImpl.findOnDemand(request)
                .collectList()
                .zipWhen(userDocuments -> userRepositoryImpl.count(request))
                .map(tuple -> UserPageDocument.builder()
                        .limit(request.limit())
                        .currentPage(request.page())
                        .totalItems(tuple.getT2())
                        .content(tuple.getT1())
                        .build());
    }

}
