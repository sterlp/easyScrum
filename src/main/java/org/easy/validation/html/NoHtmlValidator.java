package org.easy.validation.html;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.jsoup.Jsoup;

public class NoHtmlValidator implements ConstraintValidator<NoHtml, String> {
    
    private NoHtml constraintAnnotation;
    @Override
    public void initialize(NoHtml constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // validation makes only senese if you have more then 2 chars
        // e.g. "<a>"
        if (value != null && value.length() > 2) {
            return Jsoup.isValid(value, constraintAnnotation.value().getWhitelist());
        } else {
            return true;
        }
    }
}