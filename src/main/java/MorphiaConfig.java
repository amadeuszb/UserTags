import com.mongodb.MongoClient;
import entity.UserTagEntity;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MorphiaConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MorphiaConfig.class);
    private static String dbName = "usersAmad"; //todo: Properties
    private Datastore datastore;

    public Datastore getDatastore() {
        if (datastore == null) {
            LOG.debug("Connecting with database: {}", dbName);
            Morphia morphia = new Morphia();
            morphia.mapPackage("com.baeldung.morphia");
            morphia.map(UserTagEntity.class);
            datastore = morphia.createDatastore(new MongoClient(), dbName);
            datastore.ensureIndexes();
        }
        return datastore;
    }
}
