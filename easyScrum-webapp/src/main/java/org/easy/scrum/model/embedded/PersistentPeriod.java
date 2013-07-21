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
package org.easy.scrum.model.embedded;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.easy.scrum.enums.PeriodStatus;
import org.easy.validation.date.InDateRange;
import org.joda.time.DateMidnight;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.PeriodType;
import org.joda.time.Weeks;

/**
 * A from to period - mandetory from to.
 */
@Embeddable
public class PersistentPeriod implements Serializable {
    @NotNull
    @InDateRange
    @Column(nullable = false, name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date start;
    @NotNull
    @InDateRange
    @Column(nullable = false, name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date end;

    public PersistentPeriod() {
        super();
    }
    
    public PersistentPeriod(Date start, Date end) {
        this();
        this.start = start;
        this.end = end;
    }
    
    public PersistentPeriod(LocalDate start, LocalDate end) {
        this();
        this.start = start != null ? start.toDate() : null;
        this.end = end != null ? end.toDate() : null;
    }

    public Date getStart() {
        return start;
    }
    public void setStart(Date start) {
        this.start = start;
    }
    public Date getEnd() {
        return end;
    }
    public void setEnd(Date end) {
        this.end = end;
    }
    
    public org.joda.time.Period toPeriod() {
        return new org.joda.time.Period(
                new DateMidnight(start.getTime()), 
                new DateMidnight(end.getTime()));
    }
    public int getWeeksBetween() {
        return Weeks.weeksBetween(
                new DateMidnight(start.getTime()), 
                new DateMidnight(end.getTime())).getWeeks();
    }
    public int getMonthsBetween() {
        return Months.monthsBetween(
                new DateMidnight(start.getTime()), 
                new DateMidnight(end.getTime())).getMonths();
    }
    
    public PeriodStatus getPeriodStatus() {
        return PeriodStatus.fromDates(this);
    }

    @Override
    public String toString() {
        ToStringBuilder t = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        t.append("start", start);
        t.append("end", end);
        return t.toString();
    }
}
