package org.easy.scrum.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.easy.scrum.model.validation.DayInSprint;
import org.easy.validation.date.InDateRange;
import org.easy.validation.html.HtmlValidation;
import org.easy.validation.html.SafeHtml;

@Entity
@Table(name = "SPRINT_DAY")
@DayInSprint
@NamedQueries(
        @NamedQuery(name = SprintDayBE.Q_BY_SPRINT_ID, query = "SELECT a from SprintDayBE AS a WHERE a.sprint.id = :id ORDER BY a.day DESC")
)
public class SprintDayBE extends AbstractEntity {

    public static final String Q_BY_SPRINT_ID = "SprintDayBE.bySprintId";

    @NotNull
    @InDateRange
    @Column(name = "sprint_day")
    @Temporal(TemporalType.DATE)
    private Date day = new Date();
    
    @Column(name = "burn_down")
    @Min(0)
    private int burnDown = 0;
    
    @Min(0)
    private int upscaling = 0;
    
    @Column(name = "reason_for_upscaling", length = 255)
    @Size(max = 255)
    @SafeHtml(HtmlValidation.Relaxed)
    private String reasonForUpscaling = null;
    
    @Size(max = 255)
    @Column(length = 255)
    @SafeHtml(HtmlValidation.Relaxed)
    private String comment = null;
    
    @ManyToOne(cascade = {}, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "sprint_id", nullable = false, updatable = false)
    @NotNull 
    @Valid
    private SprintBE sprint;

    @Override
    public String getName() {
        return String.valueOf(day);
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public int getBurnDown() {
        return burnDown;
    }

    public void setBurnDown(int burnDown) {
        this.burnDown = burnDown;
    }

    public int getUpscaling() {
        return upscaling;
    }

    public void setUpscaling(int upscaling) {
        this.upscaling = upscaling;
    }

    public String getReasonForUpscaling() {
        return reasonForUpscaling;
    }

    public void setReasonForUpscaling(String reasonForUpscaling) {
        this.reasonForUpscaling = reasonForUpscaling;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public SprintBE getSprint() {
        return sprint;
    }

    public void setSprint(SprintBE sprint) {
        this.sprint = sprint;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
