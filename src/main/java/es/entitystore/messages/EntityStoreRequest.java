package es.entitystore.messages;

import es.entitystore.model.Entity;

public class EntityStoreRequest {

    private Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
