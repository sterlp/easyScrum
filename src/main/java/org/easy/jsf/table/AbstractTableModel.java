package org.easy.jsf.table;

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

public abstract class AbstractTableModel<T extends ModelClass, FacadeType extends AbstractFacade<T>> implements ITableModel<T> {
    protected final Logger logger;

    protected T newElement;
    /** The selected element */
    protected T selected;
    protected List<T> elements = new ArrayList<T>();

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

    @Override
    public void clearNew() {
        this.newElement = null;
    }
    
    /** method called on each update */
    protected void updateHook() {
        
    }
    
    /**
     * @return the first element from the list or null if list is empty
     */
    public T getFirstElement() {
        if (elements.isEmpty()) return null;
        return elements.get(0);
    }

    @Override
    public T getNewElement() {
        if (newElement == null) newElement = newModel();
        return newElement;
    }

    @Override
    public void setNewElement(T newElement) {
        this.newElement = newElement;
    }
    protected List<T> findAll() {
        return getFacade().findAll();
    }
    @Override
    public List<T> getElements() {
        return elements;
    }
    @Override
    public void setElements(List<T> elements) {
        this.elements = elements;
    }
    @Override
    public T getSelected() {
        return selected;
    }
    @Override
    public void setSelected(T selected) {
        this.selected = selected;
    }
    @Override
    public void onEditRow(RowEditEvent event) {
        T editElement = (T)event.getObject();
        if (editElement != null) {
            edit(editElement);
        } else {
            throw new RuntimeException("RowEditEvent does not contain any data! " + editElement);
        }
    }
    
    @Override
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
        updateHook();
    }
    
    @Override
    public void delete(T element) {
        logger.debug("delete -> {}", element);
        elements.remove(element);
        getFacade().remove(element);
        FacesContext context = FacesContext.getCurrentInstance(); 
        context.addMessage(null, new FacesMessage("'" + element.getName() + "' deleted."));
        updateHook();
    }
    @Override
    public void add() {
        logger.debug("*** add: {} ***", this.newElement);
        FacesContext context = FacesContext.getCurrentInstance(); 
        selected = getFacade().create(this.newElement);
        this.newElement = null;
        context.addMessage(null, new FacesMessage("'" + selected.getName() + "' created."));
        setElements(findAll());
        updateHook();
    }
    
    @Override
    public void onCancelEditRow(RowEditEvent event) {
        this.realod();
    }
    @Override
    public void realod() {
        setElements(findAll());
        updateHook();
    }
}
