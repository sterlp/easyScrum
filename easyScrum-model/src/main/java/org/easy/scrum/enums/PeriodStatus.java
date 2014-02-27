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

import java.util.Date;
import org.easy.scrum.model.embedded.PersistentPeriod;

/**
 * Generic enum to determine the current state of on Object with Dates.
 * 
 * e.g. If the start date is in the fu
 */
public enum PeriodStatus {
    /**
     * Status returned if the start date is in the Future.
     */
    PLANNING,
    /**
     * This state tells if the current start date was reached but not the end date.
     */
    RUNNING,
    /**
     * This status represenets that the end date was reached.
     */
    CLOSED;
    
    public static PeriodStatus fromDates(Date start, Date end) {
        long now = System.currentTimeMillis();
        if (start == null) throw new NullPointerException("Start date cannot be null!");
        if (start.getTime() > now) return PLANNING;
        if (end == null) throw new NullPointerException("End date cannot be null!");
        if (end.getTime() < now) return CLOSED;
        return RUNNING;
    }

    public static PeriodStatus fromDates(PersistentPeriod period) {
        if (period == null) throw new NullPointerException(PersistentPeriod.class.getSimpleName() + " cannot be null!");
        return fromDates(period.getStart(), period.getEnd());
    }
    
    public String getName() {
        return name();
    }
    public String getLocalizationCode() {
        return PeriodStatus.class.getSimpleName() + "_" + name();
    }
}
