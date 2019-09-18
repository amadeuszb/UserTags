package dao;

import com.mongodb.DuplicateKeyException;
import entity.UserTagEntity;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class UsersTagDAO implements UserTagDAO {

    private static final Logger LOG = LoggerFactory.getLogger(UsersTagDAO.class);

    private Datastore datastore;

    public UsersTagDAO(Datastore datastore) {
        this.datastore = datastore;
    }

    private void filterQueryToUUID(Query<UserTagEntity> query, String fieldName, UUID value) {
        if (value != null && !value.equals("")) {
            query.field(fieldName).equal(value);
        }
    }

    @Override
    public UserTagEntity getById(String id) {
        LOG.info("Querying database about User with id: {}", id);
        return datastore
                .get(UserTagEntity.class, UUID.fromString(id));
    }

    @Override
    public List<UserTagEntity> getAllWithParams(String userId,Long offset, Long limit) {
        LOG.info("Querying database about users with params userId: {}, offset: {}, limit: {}", userId, offset, limit);
        Query<UserTagEntity> query = datastore.find(UserTagEntity.class);
        if(userId != null) {
            query.field("userId").equal(UUID.fromString(userId));
        }if (offset != null) {
            query.offset(offset.intValue());
        }
        if (limit != null) {
            query.limit(limit.intValue());
        }
        return query.asList();
    }

    @Override
    public void removeByUserId(UUID id) {
        LOG.info("Removing userTag with id: {} in database", id);
        Query<UserTagEntity> query = datastore.find(UserTagEntity.class);
        query.field("userId").equal(id);
        datastore.delete(query);
    }

    @Override
    public void update(UserTagEntity userTag) {
        LOG.info("Updating userTag with id: {} in database", userTag.getId());
        UpdateOperations<UserTagEntity> ops = datastore
                .createUpdateOperations(UserTagEntity.class)
                .set("tag", userTag.getTag());
        Query<UserTagEntity> query = datastore
                .createQuery(UserTagEntity.class)
                .field("_id")
                .equal(userTag.getId());
        datastore.update(query, ops);
    }

    @Override
    public void add(UserTagEntity userTag) {
        LOG.info("Adding userTag with id: {} in database", userTag.getId());
        datastore.save(userTag);
    }
}
