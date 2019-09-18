package dao;

import com.mongodb.DuplicateKeyException;
import entity.UserTagEntity;

import java.util.List;
import java.util.UUID;

public interface UserTagDAO {
    UserTagEntity getById(String id);

    List<UserTagEntity> getAllWithParams(String userId, Long offset, Long limit);

    void removeByUserId(UUID id);

    void update(UserTagEntity userTag);

    void add(UserTagEntity userTag);
}
