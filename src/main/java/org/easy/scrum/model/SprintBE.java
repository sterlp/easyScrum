package org.easy.scrum.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.easy.validation.date.InDateRange;
import org.easy.validation.html.NoHtml;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.ReadablePeriod;

/**
 *
 * @author Paul
 */
@Entity
@Table(name = "SPRINT")
public class SprintBE extends AbstractEntity {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    @NoHtml
    private String name;
    
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

    @Column(name = "planned_hours")
    @Min(value = 1)
    private int plannedHours = 1;
    
    @Column(name = "story_points")
    @Min(value = 1)
    private int storyPoints = 1;
    
    @ManyToOne(cascade = {}, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    @NotNull
    private TeamBE team;
    
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "sprint")
    @OrderBy("day DESC")
    private List<SprintDayBE> days;
    
    public SprintBE() {
        super();
        if (start == null) {
            start = new Date();
        }
        if (end == null) {
            end = new LocalDate(start).plusWeeks(2).toDate();
        }
    }

    public SprintBE(String name) {
        this();
        this.name = name;
    }
    
    /** Adds the given period to the start date, which will be the new end date */
    public void newEndDate(ReadablePeriod period) {
        this.end = new LocalDate(start).plus(period).toDate();
    }
    
    /** Returns the days between start and end day */
    public int getDayOfIteration() {
        return Days.daysBetween(new LocalDate(start), new LocalDate(end)).getDays();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamBE getTeam() {
        return team;
    }

    public void setTeam(TeamBE team) {
        this.team = team;
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

    public int getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(int plannedHours) {
        this.plannedHours = plannedHours;
    }

    public List<SprintDayBE> getDays() {
        return days;
    }

    public void setDays(List<SprintDayBE> days) {
        this.days = days;
    }

    public int getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(int storyPoints) {
        this.storyPoints = storyPoints;
    }
    
    @Override
    public String toString() {
        ToStringBuilder result = new ToStringBuilder(this);
        result.append("id", id);
        result.append("name", name);
        result.append("team", team);
        result.append("start", start);
        result.append("end", end);
        result.append("plannedHours", this.plannedHours);
        result.append("storyPoints", this.storyPoints);
        return result.toString();
    }
}
