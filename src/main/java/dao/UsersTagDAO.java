package dao;

import entity.UserTagEntity;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class UsersTagDAO implements UserTagDAO {

    private static final Logger LOG = LoggerFactory.getLogger(UsersTagDAO.class);
    private Datastore datastore;

    public UsersTagDAO(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public UserTagEntity getById(String id) {
        LOG.info("Querying database about UserTag with id: {}", id);
        return datastore
                .get(UserTagEntity.class, UUID.fromString(id));
    }

    @Override
    public List<UserTagEntity> getAllWithParams(String userId, Long offset, Long limit) {
        LOG.info("Querying database about UserTag with params userId: {}, offset: {}, limit: {}", userId, offset, limit);
        Query<UserTagEntity> query = datastore.find(UserTagEntity.class);
        query.order("userId");
        if (userId != null) {
            query.field("userId").equal(UUID.fromString(userId));
        }
        if (offset != null) {
            query.offset(offset.intValue());
        }
        if (limit != null) {
            query.limit(limit.intValue());
        }
        return query.asList();
    }

    @Override
    public Long getAmountOfTags() {
        return datastore.getCount(UserTagEntity.class);
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
