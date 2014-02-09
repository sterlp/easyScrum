package org.easy.jsf.prime.exporter;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import org.primefaces.component.celleditor.CellEditor;
import org.primefaces.component.export.ExcelExporter;

/**
 * http://stackoverflow.com/questions/14411389/pdataexporter-does-not-recognize-pcelleditor
 */
public class ExtendedExcelExporter extends ExcelExporter {
    @Override
    protected String exportValue(FacesContext context, UIComponent component) {
        System.out.println("exportValue: " + component.getClass().getName());
        if (component instanceof CellEditor) {
            return exportValue(context, ((CellEditor) component).getFacet("output"));
        } else if (component instanceof HtmlGraphicImage) {
            return (String) component.getAttributes().get("alt");
        }
        else {
            return super.exportValue(context, component);
        }
    }
}
