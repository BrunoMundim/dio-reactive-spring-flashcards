package br.com.mundim.reactiveflashcards.domain.dto;

import lombok.Builder;

public record StudyCardDTO(String front, String back) {

    @Builder(toBuilder = true)
    public StudyCardDTO {  }
}
