/*
 * Copyright 2013 Paul Sterl.
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
package org.easy.scrum.enums;

import org.joda.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

public class PeriodStatusTest {
    
    @Test
    public void testFromDates() {
        assertEquals(PeriodStatus.CLOSED,
            PeriodStatus.fromDates(
                    new LocalDate(2013, 1, 20).toDate(),
                    new LocalDate(2013, 7, 20).toDate()));
        
        assertEquals(PeriodStatus.RUNNING,
            PeriodStatus.fromDates(
                    new LocalDate().minusDays(1).toDate(),
                    new LocalDate().plusDays(1).toDate()));
        
        assertEquals(PeriodStatus.PLANNING,
            PeriodStatus.fromDates(
                    new LocalDate().plusDays(1).toDate(),
                    new LocalDate().plusDays(10).toDate()));
    }
}