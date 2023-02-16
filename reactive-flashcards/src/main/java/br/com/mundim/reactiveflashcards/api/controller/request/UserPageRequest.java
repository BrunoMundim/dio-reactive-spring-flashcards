package br.com.mundim.reactiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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

public record UserPageRequest(@JsonProperty("sentence")
                              @Schema(description = "Texto para filtrar por nome e mail (case insensitive)", example = "ana")
                              String sentence,
                              @JsonProperty("page") @PositiveOrZero
                              @Schema(description = "Página solicitada", example = "1", defaultValue = "0")
                              Long page,
                              @JsonProperty("limit") @Min(value = 1) @Max(value = 50)
                              @Schema(description = "Tamanho da página", example = "30", defaultValue = "20")
                              Integer limit,
                              @JsonProperty("sortBy")
                              @Schema(description = "Campo para ordenação", enumAsRef = true, defaultValue = "NAME")
                              UserSortBy sortBy,
                              @JsonProperty("sortDirection")
                              @Schema(description = "Sentido da ordenação", enumAsRef = true, defaultValue = "ASC")
                              UserSortDirection sortDirection) {

    @Builder(toBuilder = true)
    public UserPageRequest {
        sentence = ObjectUtils.defaultIfNull(sentence, "");
        sortBy = ObjectUtils.defaultIfNull(sortBy, NAME);
        sortDirection = ObjectUtils.defaultIfNull(sortDirection, ASC);
        limit = ObjectUtils.defaultIfNull(limit, 20);
        page = ObjectUtils.defaultIfNull(page, 0L);
    }

    @Schema(hidden = true)
    public Sort getSort(){
        return sortDirection.equals(DESC) ? Sort.by(sortBy.getField()).descending() : Sort.by(sortBy.getField()).ascending();
    }

    @Schema(hidden = true)
    public Long getSkip() {
        return page > 0 ? ((page - 1) * limit) : 0;
    }

}
