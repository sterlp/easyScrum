package org.easy.jsf.phaselistener;

import javax.faces.application.FacesMessage;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagePhaseListener implements PhaseListener {
    private static final Logger LOG = LoggerFactory.getLogger(MessagePhaseListener.class);
    @Override
    public void afterPhase(PhaseEvent event) {
        // nothing
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        RequestContext rc = RequestContext.getCurrentInstance();
        if (rc != null && event.getFacesContext().getMaximumSeverity() != null) {
            FacesMessage.Severity maximumSeverity = event.getFacesContext().getMaximumSeverity();
            rc.addCallbackParam("maximumSeverity", maximumSeverity);
            rc.addCallbackParam("hasErrorMessage", 
                FacesMessage.SEVERITY_ERROR.equals(maximumSeverity)
            );
            LOG.debug("Added '{}' as maximumSeverity to response.", maximumSeverity);
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
