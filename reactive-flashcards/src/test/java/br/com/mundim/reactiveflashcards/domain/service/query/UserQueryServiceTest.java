package br.com.mundim.reactiveflashcards.domain.service.query;

import br.com.mundim.reactiveflashcards.api.controller.request.UserPageRequest;
import br.com.mundim.reactiveflashcards.core.factorybot.document.UserDocumentFactoryBot;
import br.com.mundim.reactiveflashcards.core.factorybot.request.UserPageRequestFactoryBot;
import br.com.mundim.reactiveflashcards.domain.document.UserDocument;
import br.com.mundim.reactiveflashcards.domain.exception.NotFoundException;
import br.com.mundim.reactiveflashcards.domain.repository.UserRepository;
import br.com.mundim.reactiveflashcards.domain.repository.UserRepositoryImpl;
import com.github.javafaker.Faker;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static br.com.mundim.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserQueryServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRepositoryImpl userRepositoryImpl;
    private UserQueryService userQueryService;
    private static final Faker faker = getFaker();

    @BeforeEach
    void setup() {
        userQueryService = new UserQueryService(userRepository, userRepositoryImpl);
    }

    @Test
    void findByIdTest() {
        var document = UserDocumentFactoryBot.builder().build();
        when(userRepository.findById(anyString())).thenReturn(Mono.just(document));

        StepVerifier.create(userQueryService.findById(ObjectId.get().toString()))
                .assertNext(actual -> assertThat(actual)
                        .usingRecursiveComparison()
                        .ignoringFields("createdAt", "updatedAt")
                        .isEqualTo(document))
                .verifyComplete();
        verify(userRepository).findById(anyString());
        verifyNoInteractions(userRepositoryImpl);
    }

    @Test
    void whenTryToFindNonStoredUserByIdThenThrowError() {
        var document = UserDocumentFactoryBot.builder().build();
        when(userRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(userQueryService.findById(ObjectId.get().toString()))
                .verifyError(NotFoundException.class);
        verify(userRepository).findById(anyString());
        verifyNoInteractions(userRepositoryImpl);
    }

    @Test
    void findByEmailTest() {
        var document = UserDocumentFactoryBot.builder().build();
        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(document));

        StepVerifier.create(userQueryService.findByEmail(faker.internet().emailAddress()))
                .assertNext(actual -> assertThat(actual)
                        .usingRecursiveComparison()
                        .ignoringFields("createdAt", "updatedAt")
                        .isEqualTo(document))
                .verifyComplete();

        verify(userRepository).findByEmail(anyString());
        verifyNoInteractions(userRepositoryImpl);
    }

    @Test
    void whenTryToFindNonStoredUserByEmailThenThrowError() {
        var document = UserDocumentFactoryBot.builder().build();
        when(userRepository.findByEmail(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(userQueryService.findByEmail(faker.internet().emailAddress()))
                .verifyError(NotFoundException.class);

        verify(userRepository).findByEmail(anyString());
        verifyNoInteractions(userRepositoryImpl);
    }

    private static Stream<Arguments> findOnDemandTest() {
        var documents = Stream.generate(() -> UserDocumentFactoryBot.builder().build())
                .limit(faker.number().randomDigitNotZero())
                .toList();
        var total = faker.number().numberBetween(documents.size(), documents.size()*3L);
        var pageRequest = UserPageRequestFactoryBot.builder().build();
        var expectTotalPages = (total / pageRequest.limit()) + ((total % pageRequest.limit() > 0) ? 1 : 0);

        return Stream.of(
                Arguments.of(documents, total, pageRequest, expectTotalPages),
                Arguments.of(new ArrayList<>(), 0L, pageRequest, 0L)
        );
    }

    @MethodSource
    @ParameterizedTest
    void findOnDemandTest(final List<UserDocument> documents,
                          final Long total,
                          final UserPageRequest pageRequest,
                          final Long expectedTotalPages) {
        when(userRepositoryImpl.findOnDemand(any(UserPageRequest.class))).thenReturn(Flux.fromIterable(documents));
        when(userRepositoryImpl.count(any(UserPageRequest.class))).thenReturn(Mono.just(total));

        StepVerifier.create(userQueryService.findOnDemand(pageRequest))
                .assertNext(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.totalItems()).isEqualTo(total);
                    assertThat(actual.content()).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(documents);
                    assertThat(actual.totalPages()).isEqualTo(expectedTotalPages);
                })
                .verifyComplete();
    }

}
