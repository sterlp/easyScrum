package org.easy.jsf.table;

import java.io.Serializable;
import java.util.List;
import javax.faces.event.ActionEvent;
import org.easy.jsf.model.ModelClass;
import org.primefaces.event.RowEditEvent;

public interface ITableModel<T extends ModelClass> extends Serializable {

    /** The element which is currently added. */
    T getNewElement();
    /** The element which is currently added. */
    void setNewElement(T current);
    /** The current selected element */
    T getSelected();
    /** The current selected element */
    void setSelected(T selected);

    /** All current elements which can be displayed in a table */
    List<T> getElements();
    /** All current elements which can be displayed in a table */
    void setElements(List<T> elements);

    /** clears the input of the new element */
    void clearNew();
    void delete(T element);
    void edit(T element);
    /** adds the new element */
    void add();

    void onEditRow(RowEditEvent event);
    void onCancelEditRow(RowEditEvent event);
    void realod();
}
