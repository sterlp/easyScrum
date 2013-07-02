package org.easy.validation.html;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.constraints.SafeHtml;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestNoHtml {

    static class TestClass {
        @SafeHtml(whitelistType = SafeHtml.WhiteListType.RELAXED)
        String value;

        public TestClass(String value) {
            this.value = value;
        }
    }
    
    private static Validator validator;
    
    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    public void testBasicWithList() {
        String testString = 
                "<ul><li>asdasdas</li><li>asdasdsa</li></ul>";
        Set<ConstraintViolation<TestClass>> validate = validator.validate(new TestClass(testString));
        System.out.println("validate: " + validate);
        assertEquals(0, validate.size());
    }
}