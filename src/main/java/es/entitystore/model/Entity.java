package es.entitystore.model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "entity")
public class Entity {

    @PartitionKey
    private String id;

    private String remoteId1;
    private String remoteId2;
    private Boolean active;

    public Entity() {
    }

    public Entity(String id, String remoteId1, String remoteId2, Boolean active) {
        this.id = id;
        this.remoteId1 = remoteId1;
        this.remoteId2 = remoteId2;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemoteId1() {
        return remoteId1;
    }

    public void setRemoteId1(String remoteId1) {
        this.remoteId1 = remoteId1;
    }

    public String getRemoteId2() {
        return remoteId2;
    }

    public void setRemoteId2(String remoteId2) {
        this.remoteId2 = remoteId2;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
