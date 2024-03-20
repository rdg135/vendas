package com.rdg135.vendas.validation;

import com.rdg135.vendas.validation.contraintvalidation.NotEmptyListValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = NotEmptyListValidator.class)
public @interface NotEmptyList {

    String message () default "O pedido não pode ser realizado sem itens.";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payLoad() default {};
}
