package org.easy.scrum.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CONFIG")
public class ConfigBE implements IEntity<String> {

    /** the client id of this configuration */
    @Id
    private String id;
    
    @Column(name = "burn_down_type", nullable = false, length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    private BurnDownType burnDownType = BurnDownType.BURN_DOWN_BEFORE_UP_SCALING;
    
    @Column(name = "last_login", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date lastLogin = new Date();
    
    @Column(name = "burn_down_width")
    private Long burnDownWidth = null;
    
    @Column(name = "burn_down_height")
    @Min(value = 100)
    private long burnDownHeight = 500;
    
    /** the client id of this configuration */
    @Override
    public String getId() {
        return id;
    }
    /** the client id of this configuration */
    public void setId(String id) {
        this.id = id;
    }

    public BurnDownType getBurnDownType() {
        return burnDownType;
    }

    public void setBurnDownType(BurnDownType burnDownType) {
        this.burnDownType = burnDownType;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Long getBurnDownWidth() {
        return burnDownWidth;
    }

    public void setBurnDownWidth(Long burnDownWidth) {
        this.burnDownWidth = burnDownWidth;
        if (this.burnDownWidth != null && this.burnDownWidth.longValue() <= 0) {
            this.burnDownWidth = null;
        }
    }

    public Long getBurnDownHeight() {
        return burnDownHeight;
    }

    public void setBurnDownHeight(Long burnDownHeight) {
        this.burnDownHeight = burnDownHeight;
    }
}
