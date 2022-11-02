package com.example.product_service.controller.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BadRequestAlertException extends AbstractThrowableProblem {

    private final String entityName;
    private final String errorKey;
    
    public BadRequestAlertException(
            String title,
            StatusType status,
            String detail, 
            String entityName, 
            String errorKey) {
        super(ErrorConstants.DEFAULT_TYPE, title, status, detail);
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public BadRequestAlertException(
            URI type,
            String title,
            String entityName,
            String errorKey) {
        super(type, title, Status.BAD_REQUEST, null, null, null, getParamater(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }
    /**
     *  
     * @param entityName
     * @param errorKey
     * @return
     */
    private static Map<String, Object> getParamater(String entityName, String errorKey) {
        Map<String, Object> parameter = new HashMap<>();

        parameter.put("message", "error." + errorKey);
        parameter.put("params", entityName);

        return parameter;
    }
}
