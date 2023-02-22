package br.com.mundim.reactiveflashcards.domain.service;

import br.com.mundim.reactiveflashcards.core.validation.MongoId;
import br.com.mundim.reactiveflashcards.domain.document.UserDocument;
import br.com.mundim.reactiveflashcards.domain.exception.EmailAlreadyUsedException;
import br.com.mundim.reactiveflashcards.domain.exception.NotFoundException;
import br.com.mundim.reactiveflashcards.domain.repository.UserRepository;
import br.com.mundim.reactiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Objects;

import static br.com.mundim.reactiveflashcards.domain.exception.BaseErrorMessage.EMAIL_ALREADY_USED;

@AllArgsConstructor
@Slf4j
@Service
public class UserService{

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public Mono<UserDocument> save(final UserDocument document) {
        return userQueryService.findByEmail(document.email())
                .doFirst(() -> log.info("===== Try to save a follow user {}", document))
                .filter(Objects::isNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new EmailAlreadyUsedException(EMAIL_ALREADY_USED
                        .params(document.email()).getMessage()))))
                .onErrorResume(NotFoundException.class, e -> userRepository.save(document));
    }

    public Mono<UserDocument> update(final UserDocument document) {
        return userQueryService.verifyEmail(document)
                .then(userQueryService.findById(document.id())
                    .map(user -> document.toBuilder()
                        .createdAt(user.createdAt())
                        .updatedAt(user.updatedAt())
                        .build())
                    .flatMap(userRepository::save)
                    .doFirst(() -> log.info("===== try to update a user with follow info {}", document)));
    }

    public Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id) {
        return userQueryService.findById(id)
                .flatMap(userRepository::delete)
                .doFirst(() -> log.info("===== try to delete user with follow id {}", id));
    }

}
