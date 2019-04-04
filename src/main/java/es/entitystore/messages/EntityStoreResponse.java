package es.entitystore.messages;

import es.entitystore.model.Entity;

import java.util.List;

public class EntityStoreResponse {

    private List<Entity> entities;

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }
}
