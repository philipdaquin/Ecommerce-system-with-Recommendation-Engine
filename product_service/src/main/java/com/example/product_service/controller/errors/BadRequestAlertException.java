package com.example.product_service.controller.errors;

import org.zalando.problem.AbstractThrowableProblem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
public class BadRequestAlertException extends AbstractThrowableProblem {

    private final String entityName;
    private final String errorKey;

    
}
