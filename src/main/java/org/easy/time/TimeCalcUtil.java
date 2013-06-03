package org.easy.time;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class TimeCalcUtil {
    
    public static int daysBetween(LocalDate start, LocalDate end, boolean includeWeekend) {
        final int daysWithWeekend = Days.daysBetween(start, end).getDays() + 1;
        if (includeWeekend) return daysWithWeekend;
        
        int daysWithoutWeekend = 0;
        for (int i = 0; i < daysWithWeekend; i++) {
            LocalDate current = start.plusDays(i);
            if (current.getDayOfWeek() != DateTimeConstants.SATURDAY
                    && current.getDayOfWeek() != DateTimeConstants.SUNDAY) {
                ++daysWithoutWeekend;
            }
        }
        return daysWithoutWeekend;
    }
    
}
