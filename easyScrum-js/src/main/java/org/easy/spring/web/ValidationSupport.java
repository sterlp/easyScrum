/*
 * Copyright 2014 Paul.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.easy.spring.web;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Piece of shitty code make out of Sping a JSR compatible web framework.
 * Why is this not part of the framwork? Maybe simply not found the right documentaion?
 * 
 */
@ControllerAdvice
public class ValidationSupport {
    protected final static Logger LOG = LoggerFactory.getLogger(JsonErrorSupport.class);
    // @Autowired
    //private MessageSource messageSource;


    @Data
    static class ValidationError {
        String objectName;
        String path;
        int errors;
        List<FieldError> fieldErrors;

        public ValidationError(String objectName, String path, int errors, List<FieldError> fieldErrors) {
            this.objectName = objectName;
            this.path = path;
            this.errors = errors;
            this.fieldErrors = fieldErrors;
        }
    }

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationError processValidationError(MethodArgumentNotValidException ex) {
        BindingResult errors = ex.getBindingResult();
        ValidationError result = new ValidationError(
                errors.getObjectName(), 
                errors.getNestedPath(), 
                errors.getErrorCount(),
                errors.getFieldErrors());
        
        return result ;//processFieldErrors(fieldErrors);
    }
    
    @Data
    @AllArgsConstructor
    static class ErrorMessage {
        String message;
        String details;
    }
    
    /**
     * Has to be in one class as Spring seems to take a magic order in this handlers
     * as this is the last "method" the other handlers are takes before this fallback
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage onError(Exception e) {
        LOG.error(e.getMessage(), e);
        return new ErrorMessage(e.getMessage(), ExceptionUtils.getStackTrace(e));
    }
}
