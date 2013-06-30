package org.easy.scrum.model;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.easy.jsf.model.ModelClass;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable, ModelClass<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    
    @Override
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AbstractEntity)) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
