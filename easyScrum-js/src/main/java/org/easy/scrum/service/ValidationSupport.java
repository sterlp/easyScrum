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

package org.easy.scrum.service;

import java.util.List;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;
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
    // @Autowired
    //private MessageSource messageSource;
    
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

        public String getObjectName() {
            return objectName;
        }

        public String getPath() {
            return path;
        }

        public int getErrors() {
            return errors;
        }

        public List<FieldError> getFieldErrors() {
            return fieldErrors;
        }
    }
    
    //static class FieldError {
        
    //}

    
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
}
