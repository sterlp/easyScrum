package org.easy.scrum.controller;

import java.util.Date;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.joda.time.LocalDate;

/**
 * Application wide default configuration and settings. 
 */
@Named
@ApplicationScoped
public class ApplicationController {
    
    public Date getMinDate() {
        return new LocalDate(1900, 1, 1).toDate();
    }
    public Date getMaxDate() {
        return new LocalDate(9999, 12, 31).toDate();
    }
    
    public String getCalendarAnimation() {
        return "slideDown";
    }
}
