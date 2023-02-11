package br.com.mundim.reactiveflashcards.domain.exception;

public class RetryException extends ReactiveFlashcardsException{

    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }

}
