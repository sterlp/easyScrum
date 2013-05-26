package org.easy.validation.html;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class NoHtmlValidator implements ConstraintValidator<NoHtml, String> {
    
    @Override
    public void initialize(NoHtml constraintAnnotation) {
        // nothing
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // validation makes only senese if you have more then 2 chars
        // e.g. "<a>"
        if (value != null && value.length() > 2) {
            return Jsoup.isValid(value, Whitelist.none());
        } else {
            return true;
        }
    }
}