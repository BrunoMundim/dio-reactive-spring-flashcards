package br.com.mundim.reactiveflashcards.core.factorybot.request;

import br.com.mundim.reactiveflashcards.api.controller.request.UserPageRequest;
import br.com.mundim.reactiveflashcards.api.controller.request.UserSortBy;
import br.com.mundim.reactiveflashcards.api.controller.request.UserSortDirection;
import com.github.javafaker.Faker;

import static br.com.mundim.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static br.com.mundim.reactiveflashcards.core.factorybot.RandomData.randomEnum;

public class UserPageRequestFactoryBot {

    public static UserPageRequestFactoryBotBuilder builder() {
        return new UserPageRequestFactoryBotBuilder();
    }


    public static class UserPageRequestFactoryBotBuilder{
        private String sentence;
        private Long page;
        private Integer limit;
        private UserSortBy sortBy;
        private UserSortDirection sortDirection;
        private Faker faker = getFaker();

        public UserPageRequestFactoryBotBuilder() {
            this.sentence = faker.lorem().sentence();
            this.page = faker.number().numberBetween(0L, 3L);
            this.limit = faker.number().numberBetween(15, 50);
            this.sortBy = randomEnum(UserSortBy.class);
            this.sortDirection = randomEnum(UserSortDirection.class);
        }

        public UserPageRequest build() {
            return UserPageRequest.builder()
                    .sentence(sentence)
                    .page(page)
                    .limit(limit)
                    .sortBy(sortBy)
                    .sortDirection(sortDirection)
                    .build();
        }

    }

}
