package org.easy.jsf.model;

import org.easy.scrum.model.IEntity;

public interface ModelClass<IdType> extends IEntity<IdType> {
    String getName();
}
