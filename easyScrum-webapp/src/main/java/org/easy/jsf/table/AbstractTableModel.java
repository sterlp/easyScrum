package org.easy.jsf.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.easy.jsf.model.ModelClass;
import org.easy.scrum.service.AbstractFacade;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTableModel<T extends ModelClass<Long>, FacadeType extends AbstractFacade<T, Long>> implements Serializable {
    protected final Logger logger;

    protected T newElement;
    /** The current selected element */
    protected T selected;
    /** All current elements which can be displayed in a table */
    protected List<T> elements = new ArrayList<T>();
    /** All current elements which can be displayed in a table */
    protected List<T> filteredElements = new ArrayList<T>();
    /** 
     * Flag if currently a new object is in "construction" 
     * set to true if beforeNew is called and set to false if add was called
     * cancelAdd switches the flag always to false. Call it onHide
     */
    private boolean inCreateNew = false;

    public AbstractTableModel() {
        super();
        logger = LoggerFactory.getLogger(this.getClass());
    }
    
    protected abstract T newModel();
    
    protected abstract FacadeType getFacade();

    @PostConstruct
    protected void init() {
        this.elements.clear();
        setElements(findAll());
        logger.debug("Initilized {} and ready. {} elements found.", this.getClass().getSimpleName(), elements.size());
    }

    
    /** The element which is currently added. */
    public T getFirstElement() {
        if (elements.isEmpty()) return null;
        return elements.get(0);
    }
    
    public T getElement(Long id) {
        for (T t : elements) {
            if (id.longValue() == t.getId().longValue()) {
                return t;
            }
        }
        return this.getFacade().find(id);
    }

    /** The element which is currently added. */
    public T getNewElement() {
        if (newElement == null) newElement = newModel();
        return newElement;
    }
    /** The element which is currently added. */
    public void setNewElement(T newElement) {
        this.newElement = newElement;
    }
    protected List<T> findAll() {
        return getFacade().findAll();
    }
    /** All current elements which can be displayed in a table */
    public List<T> getElements() {
        return elements;
    }
    /** All current elements which can be displayed in a table */
    public void setElements(List<T> elements) {
        this.elements = elements;
    }
    /** The current selected element */
    public T getSelected() {
        return selected;
    }
    /** The current selected element */
    public void setSelected(T selected) {
        this.selected = selected;
    }
    public void onEditRow(RowEditEvent event) {
        T editElement = (T)event.getObject();
        if (editElement != null) {
            edit(editElement);
        } else {
            throw new RuntimeException("RowEditEvent does not contain any data! " + editElement);
        }
    }
    
    public void edit(final T element) {
        T result = getFacade().edit(element);
        
        FacesContext context = FacesContext.getCurrentInstance(); 
        context.addMessage(null, new FacesMessage("'" + result.getName() + "' updated."));
        
        boolean repalced = false;
        if (!elements.contains(result)) {
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).getId().longValue() == result.getId().longValue()) {
                    elements.remove(i);
                    elements.add(i, result);
                    repalced = true;
                    break;
                }
            }
        } else {
            repalced = true; // already in the list
        }
        
        if (!repalced) {
            System.err.println("*** Reload ***");
            elements = findAll();
        }
    }
    
    public void delete(T element) {
        logger.debug("delete -> {}", element);
        elements.remove(element);
        getFacade().remove(element);
        FacesContext context = FacesContext.getCurrentInstance(); 
        context.addMessage(null, new FacesMessage("'" + element.getName() + "' deleted."));
    }
    
    public void deleteSelected() {
        if (selected != null) {
            this.delete(selected);
            this.selected = null;
        } else {
            FacesContext context = FacesContext.getCurrentInstance(); 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Nothing selected."));
        }
    }
    
    public void beforeAdd() {
        this.newElement = this.newModel();
        this.inCreateNew = true;
    }
    
    public void cancelAdd() {
        this.inCreateNew = false;
    }
    
    public void newSelectedForAdd() {
        this.selected = newModel();
        this.inCreateNew = true;
    }
    
    /**
     * Creates or update a new element based on the data in the selected
     * element.
     */
    public void addOrUpdateSelected() {
        if (this.inCreateNew) {
            this.newElement = selected;
            add();
        } else {
            update();
        }
    }
    /**
     * Updates the selected element.
     */
    public void update() {
        logger.debug("*** update: {} ***", this.newElement);
        FacesContext context = FacesContext.getCurrentInstance(); 
        selected = getFacade().edit(selected);
        context.addMessage(null, new FacesMessage("'" + selected.getName() + "' updated."));
        setElements(findAll());
    }
    
    public void add() {
        logger.debug("*** add: {} ***", this.newElement);
        FacesContext context = FacesContext.getCurrentInstance(); 
        selected = getFacade().create(this.newElement);
        this.newElement = null;
        context.addMessage(null, new FacesMessage("'" + selected.getName() + "' created."));
        setElements(findAll());
        this.inCreateNew = false;
    }
    
    public void onCancelEditRow(RowEditEvent event) {
        this.realod();
    }
    public void realod() {
        setElements(findAll());
    }

    /** 
     * Flag if currently a new object is in "construction" 
     * set to true if beforeNew is called and set to false if add was called
     */
    public boolean isInCreateNew() {
        return inCreateNew;
    }
    /** 
     * Flag if currently a new object is in "construction" 
     * set to true if beforeNew is called and set to false if add was called
     */
    public void setInCreateNew(boolean inCreateNew) {
        this.inCreateNew = inCreateNew;
    }

    public List<T> getFilteredElements() {
        return filteredElements;
    }

    public void setFilteredElements(List<T> filteredElements) {
        this.filteredElements = filteredElements;
    }
}
