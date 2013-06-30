package org.easy.scrum.model;

import java.io.Serializable;

/**
 * Marker interface for entities and common menthods for abstract classes.
 * 
 * @author Paul
 * @param <IdTye> 
 */
public interface IEntity<IdTye> extends Serializable {
    IdTye getId();
}
