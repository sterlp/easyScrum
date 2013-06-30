package org.easy.scrum.controller;

import java.io.IOException;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.easy.jsf.prime.exporter.ExtendedExcelExporter;
import org.easy.scrum.controller.day.DailyController;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application wide default configuration and settings. 
 */
@Named
@ApplicationScoped
public class ApplicationController {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationController.class);
    
    private Exporter exporter = new ExtendedExcelExporter();
    public String getCalendarAnimation() {
        return "slideDown";
    }
    
    public void exportExcel(DataTable table, String filename) throws IOException {
        LOG.debug("exportExcel: {} --> {}", table, filename);
        if (table == null) throw new NullPointerException("DataTable cannot be null!");
        if (filename == null) throw new NullPointerException("Filename cannot be null!");

        FacesContext context = FacesContext.getCurrentInstance();
        exporter.export(context, table, filename.replace(" ", "_"), false, false, "UTF-8", null, null);
        context.responseComplete();
    }
}