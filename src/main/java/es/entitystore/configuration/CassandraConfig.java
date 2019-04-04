package es.entitystore.configuration;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.datastax.driver.core.schemabuilder.SchemaBuilder.createKeyspace;
import static com.datastax.driver.mapping.NamingConventions.*;

@Configuration
public class CassandraConfig {

    @Bean
    public Cluster cluster(
            @Value("${cassandra.host:127.0.0.1}") String host,
            @Value("${cassandra.cluster.name:cluster}") String clusterName,
            @Value("${cassandra.port:9042}") int port) {
        return Cluster.builder()
                .addContactPoint(host)
                .withPort(port)
                .withClusterName(clusterName)
                .withoutJMXReporting()
                .build();
    }

    @Bean
    public Session session(Cluster cluster,
                           @Value("${cassandra.keyspace}") String keyspace,
                           @Value("${cassandra.replication.factor:1}") int replicationFactor)
            throws IOException {
        final Session session = cluster.connect();
        setupKeystore(session, keyspace, replicationFactor);
        return session;
    }

    private void setupKeystore(Session session, String keyspace, int replicationFactor) throws IOException {
        final Map<String, Object> replication = new HashMap<>();
        replication.put("class", "SimpleStrategy");
        replication.put("replication_factor", replicationFactor);
        session.execute(createKeyspace(keyspace).ifNotExists().with().replication(replication));
        session.execute("USE " + keyspace);
    }

    @Bean
    public MappingManager mappingManager(Session session) {
        final PropertyMapper propertyMapper =
                new DefaultPropertyMapper()
                    .setNamingStrategy(new DefaultNamingStrategy(LOWER_CAMEL_CASE, LOWER_CASE));
        final MappingConfiguration configuration =
                MappingConfiguration.builder().withPropertyMapper(propertyMapper).build();
        return new MappingManager(session, configuration);
    }

}
