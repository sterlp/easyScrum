package org.easy.time;

import org.easy.scrum.model.SprintBE;
import org.joda.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestTimeCalcUtil {
    @Test
    public void testWithOneWeek() {
        assertEquals(5, TimeCalcUtil.daysBetween(new LocalDate(2013, 6, 1), new LocalDate(2013, 6, 8), false));
        assertEquals(8, TimeCalcUtil.daysBetween(new LocalDate(2013, 6, 1), new LocalDate(2013, 6, 8), true));
        
        SprintBE s = new SprintBE();
        s.setStart(new LocalDate().toDate());
        s.setEnd(new LocalDate().plusDays(7).toDate());
        System.out.println(s.getDaysRemaining());
        assertTrue(s.getDaysRemaining() >= 5);
    }
    
    @Test
    public void testTwoWeekIteration() {
        assertEquals(10, TimeCalcUtil.daysBetween(new LocalDate(2013, 6, 3), new LocalDate(2013, 6, 14), false));
        assertEquals(12, TimeCalcUtil.daysBetween(new LocalDate(2013, 6, 3), new LocalDate(2013, 6, 14), true));
        
    }
}