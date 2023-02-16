package br.com.mundim.reactiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

import static br.com.mundim.reactiveflashcards.api.controller.request.UserSortBy.NAME;
import static br.com.mundim.reactiveflashcards.api.controller.request.UserSortDirection.ASC;
import static br.com.mundim.reactiveflashcards.api.controller.request.UserSortDirection.DESC;

public record UserPageRequest(@JsonProperty("sentence") String sentence,
                              @JsonProperty("page") @PositiveOrZero Long page,
                              @JsonProperty("limit") @Min(value = 1) @Max(value = 50) Integer limit,
                              @JsonProperty("sortBy") UserSortBy sortBy,
                              @JsonProperty("sortDirection") UserSortDirection sortDirection) {

    @Builder(toBuilder = true)
    public UserPageRequest {
        sentence = ObjectUtils.defaultIfNull(sentence, "");
        sortBy = ObjectUtils.defaultIfNull(sortBy, NAME);
        sortDirection = ObjectUtils.defaultIfNull(sortDirection, ASC);
        limit = ObjectUtils.defaultIfNull(limit, 20);
        page = ObjectUtils.defaultIfNull(page, 0L);
    }

    public Sort getSort(){
        return sortDirection.equals(DESC) ? Sort.by(sortBy.getField()).descending() : Sort.by(sortBy.getField()).ascending();
    }

    public Long getSkip() {
        return page > 0 ? ((page - 1) * limit) : 0;
    }

}
