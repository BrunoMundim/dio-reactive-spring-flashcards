package br.com.mundim.reactiveflashcards.domain.repository;

import br.com.mundim.reactiveflashcards.api.controller.request.UserPageRequest;
import br.com.mundim.reactiveflashcards.domain.document.UserDocument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static org.springframework.data.mongodb.core.query.Criteria.where;


@Repository
@Slf4j
@AllArgsConstructor
public class UserRepositoryImpl {

    private ReactiveMongoTemplate template;

    public Flux<UserDocument> findOnDemand(final UserPageRequest request) {
        return Mono.just(new Query())
                .flatMap(query -> buildWhere(query, request.sentence()))
                .map(query -> query.with(request.getSort()).skip(request.getSkip()).limit(request.limit()))
                .doFirst(() -> log.info("===== find users on demand with follow request {}", request))
                .flatMapMany(query -> template.find(query, UserDocument.class));
    }

    public Mono<Long> count(final UserPageRequest request) {
        return Mono.just(new Query())
                .flatMap(query -> buildWhere(query, request.sentence()))
                .doFirst(() -> log.info("===== counting users on demand with follow request {}", request))
                .flatMap(query -> template.count(query, UserDocument.class));
    }

    private Mono<Query> buildWhere(final Query query, String sentence) {
        return Mono.just(query)
                .filter(q -> StringUtils.isBlank(sentence))
                .switchIfEmpty(Mono.defer(() -> Mono.just(query)))
                    .flatMapIterable(q -> List.of("name", "email"))
                    .map(dbFields -> where(dbFields).regex(sentence, "i"))
                    .collectList()
                    .map(setWhereClause(query));
    }

    private static Function<List<Criteria>, Query> setWhereClause(Query query) {
        return criterias -> {
            var whereClause = new Criteria();
            whereClause.orOperator(criterias);
            return query.addCriteria(whereClause);
        };
    }

}
