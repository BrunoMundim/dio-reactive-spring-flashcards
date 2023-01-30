package br.com.mundim.reactiveflashcards.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {MongoIdValidator.class})
public @interface MongoId {

    String message() default "{br.com.mundim.reactiveflashcards.MongoId.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
