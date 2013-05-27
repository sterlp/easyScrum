package org.easy.jsf.converter;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.easy.scrum.model.IEntity;
import org.easy.scrum.service.AbstractFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The inheritet class must have a zero-arguments public constructor 
 * @author Paul
 * @param <ModelType>
 * @param <ModelFacade> 
 */
public abstract class AbstractConverter<ModelType extends IEntity<Long>, ModelFacade extends AbstractFacade<ModelType, Long>> implements Converter {
    protected Logger logger = LoggerFactory.getLogger(AbstractConverter.class);
    protected final Class<ModelType> clazz;
    
    protected abstract ModelFacade getFacade(FacesContext facesContext);
    protected abstract List<ModelType> getLocalCache(FacesContext facesContext);
    
    public AbstractConverter(Class<ModelType> clazz) {
        this.clazz = clazz;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        return find(facesContext, Long.valueOf(value));
    }
    
    protected ModelType find(FacesContext facesContext, Long id) {
        logger.debug("find: {}", id);
        ModelType result = null;
        if (id != null) {
            List<ModelType> localCache = getLocalCache(facesContext);
            if (localCache != null && !localCache.isEmpty()) {
                for (ModelType modeltype : localCache) {
                    if (modeltype.getId() != null && modeltype.getId().equals(id)) {
                        result = modeltype;
                        break;
                    }
                }
            }
            if (result == null) {
                logger.debug("Loading {}[id = {}] from facade.", this.clazz.getSimpleName(), id);
                result = getFacade(facesContext).find(id);
            }
        }
        return result;
    }

    String getStringKey(java.lang.Long value) {
        return value != null ? value.toString() : null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (clazz.isAssignableFrom(object.getClass())) {
            return getStringKey(((ModelType)object).getId());
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + clazz.getName());
        }
    }
    
    protected Object getBeanFromJsfContex(FacesContext facesContext, String name) {
        return facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, name);
    }
}
