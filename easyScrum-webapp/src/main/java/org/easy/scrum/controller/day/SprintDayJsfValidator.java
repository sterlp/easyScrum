package org.easy.scrum.controller.day;

import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.easy.scrum.model.SprintBE;
import org.easy.scrum.model.SprintDayBE;
import org.easy.scrum.model.validation.DayInSprintValidator;

@FacesValidator("SprintDayJsfValidator")
public class SprintDayJsfValidator implements Validator {

    private static DayInSprintValidator validator = new DayInSprintValidator();
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        UIInput findComponent = (UIInput)component.getAttributes().get("sprintSelect");
        
        if (findComponent == null) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "sprintSelect component not found!"));
        }
        
        System.out.println("SprintDayJsfValidator: " + value 
                + " sprint SubmittedValue: " + findComponent.getSubmittedValue()
                + " LocalValue: "  + findComponent.getLocalValue());
        
        Object sprintValue = findComponent.getSubmittedValue() != null ? findComponent.getSubmittedValue() : findComponent.getLocalValue();
        
        if (sprintValue != null
                && value instanceof Date
                && sprintValue instanceof SprintBE) {
            
            SprintBE sprint = (SprintBE)sprintValue;
            String validate = validator.validate((Date)value, 
                                      sprint.getStart(), 
                                      sprint.getEnd(), DayInSprintValidator.DEFAULT_DATE_FORMAT);
            if (validate != null) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, null, validate));
            }
        }
    }
}
