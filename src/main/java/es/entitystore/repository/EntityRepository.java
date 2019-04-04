package es.entitystore.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import es.entitystore.model.Entity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.datastax.driver.core.DataType.text;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

@Repository
public class EntityRepository {

    private Mapper<Entity> mapper;
    private Session session;

    private static final String TABLE = "entity";

    public EntityRepository(MappingManager mappingManager) {
        createTable(mappingManager.getSession());
        this.mapper = mappingManager.mapper(Entity.class);
        this.session = mappingManager.getSession();
    }

    private void createTable(Session session) {
        session.execute(
                SchemaBuilder.createTable(TABLE)
                    .ifNotExists()
                    .addPartitionKey("id", text())
                    .addColumn("remoteid1", text())
                    .addColumn("remoteid2", text())
                    .addColumn("active", text()));
        session.execute(
                SchemaBuilder.createIndex("active_idx")
                    .ifNotExists()
                    .onTable(TABLE)
                    .andColumn("active"));
        session.execute(
                SchemaBuilder.createIndex("remoteid1_idx")
                    .ifNotExists()
                    .onTable(TABLE)
                    .andColumn("remoteid1"));
        session.execute(
                SchemaBuilder.createIndex("remoteid2_idx")
                    .ifNotExists()
                    .onTable(TABLE)
                    .andColumn("remoteid2"));
    }

    public Entity find(String id) {
        return mapper.get(id);
    }

    public Entity findByRemoteIds(String remoteId1, String remoteId2) {
        final ResultSet result = session.execute(select().all().from(TABLE)
                .where(eq("remoteid1", remoteId1)));
        Optional<Entity> optionalEntity = mapper.map(result).all().stream().filter(entity -> entity.getRemoteId2().equals(remoteId2)).findFirst();
        if (optionalEntity.isPresent()) {
            return optionalEntity.get();
        } else {
            return null;
        }
    }

    public List<Entity> listRemoteId1(String remoteId1) {
        final ResultSet result = session.execute(select().all().from(TABLE)
                .where(eq("remoteid1", remoteId1)));
        return mapper.map(result).all();
    }

    public List<Entity> listRemoteId2(String remoteId2) {
        final ResultSet result = session.execute(select().all().from(TABLE)
                .where(eq("remoteid2", remoteId2)));
        return mapper.map(result).all();
    }

    public List<Entity> all() {
        final ResultSet result = session.execute(select().all().from(TABLE));
        return mapper.map(result).all();
    }

    public List<Entity> active() {
        final ResultSet result = session.execute(select().all().from(TABLE)
                .where(eq("active", true)));
        return mapper.map(result).all();
    }

    public List<Entity> inactive() {
        final ResultSet result = session.execute(select().all().from(TABLE)
                .where(eq("active", false)));
        return mapper.map(result).all();
    }

    public void delete(String id) {
        mapper.delete(id);
    }

    public Entity save(Entity entity) {
        mapper.save(entity);
        return entity;
    }

}
