package org.easy.scrum.model.validation;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DayInSprintValidator implements ConstraintValidator<DayInSprint, org.easy.scrum.model.SprintDayBE> {
    
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy");

    @Override
    public void initialize(DayInSprint constraintAnnotation) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean isValid(org.easy.scrum.model.SprintDayBE value, ConstraintValidatorContext context) {
        boolean inSprint = false;
        if (value != null && value.getSprint() != null 
                && value.getDay() != null 
                && value.getSprint().getStart() != null && value.getSprint().getEnd() != null) {
            
            String validate = validate(value.getDay(), value.getSprint().getStart(), value.getSprint().getEnd(), DEFAULT_DATE_FORMAT);

            if (validate != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(validate)
                       .addNode("day")
                       .addConstraintViolation();
            } else {
                inSprint = true; // valid
            }
        }
        return inSprint;
    }
    
    public String validate(Date dayInSprint, Date sprintStart, Date sprintEnd, SimpleDateFormat dateParser) {
        boolean inSprint = dayInSprint.compareTo(sprintStart) >= 0 
                        && dayInSprint.compareTo(sprintEnd) <= 0;
        
        return inSprint ? null : "date must be in Sprint (" 
                + dateParser.format(sprintStart) + " - " + dateParser.format(sprintEnd) + ")";
    }
}