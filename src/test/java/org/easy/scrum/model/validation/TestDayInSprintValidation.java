package org.easy.scrum.model.validation;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.easy.scrum.model.SprintBE;
import org.easy.scrum.model.SprintDayBE;
import org.easy.scrum.model.TeamBE;
import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestDayInSprintValidation {
    
    private static Validator validator;
    
    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    public void testSprintDay() {
        SprintDayBE sprintDay = new SprintDayBE();
        sprintDay.setDay(new LocalDate(2000, 01, 01).toDate());
        sprintDay.setSprint(new SprintBE("test 1"));
        sprintDay.getSprint().setTeam(new TeamBE());
        Set<ConstraintViolation<SprintDayBE>> validate = validator.validate(sprintDay);
        System.err.println(validate);
        assertEquals(1, validate.size());
        assertEquals("day", validate.iterator().next().getPropertyPath().toString());
    }
}