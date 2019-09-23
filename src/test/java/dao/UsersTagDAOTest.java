package dao;


import com.github.fakemongo.Fongo;
import entity.UserTagEntity;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UsersTagDAOTest {

    private UserTagDAO userTagDao;
    private UUID uuid1 = UUID.randomUUID();
    private UUID uuid2 = UUID.randomUUID();
    private UserTagEntity userTagEntity = new UserTagEntity(uuid1, uuid1, "tefa@gmail.com");
    private UserTagEntity userTagEntity2 = new UserTagEntity(uuid2, uuid2, "tefa2@gmail.com");

    @Before
    public void init() {
        Fongo fongo = new Fongo("testDb");
        Morphia morphia = new Morphia();
        morphia.map(UserTagEntity.class);
        Datastore datastore = morphia.createDatastore(fongo.getMongo(), "testdb");
        userTagDao = new UsersTagDAO(datastore);
        userTagDao.add(userTagEntity);
        userTagDao.add(userTagEntity2);
    }

    @Test
    public void getByIdShouldReturnUserTagIfExist() {
        UserTagEntity result = userTagDao.getById(uuid1.toString());
        assertEquals(userTagDao.getById(uuid1.toString()).getId(), result.getId());
    }

    @Test
    public void getByIdShouldNotReturnUserIfDoesNotExist() {
        UserTagEntity result = userTagDao.getById(UUID.randomUUID().toString());
        assertNull(result);
    }


    @Test
    public void getAllWithParamsShouldReturnListOfUsers() {
        List<UserTagEntity> result = userTagDao.getAllWithParams(null, 0L, 20L);
        assertEquals(2, result.size());
    }

    @Test
    public void updateShouldUpdateUser() {
        userTagDao.update(new UserTagEntity(uuid1, uuid1, "kiełbasa"));
        assertEquals("kiełbasa", userTagDao.getById(uuid1.toString()).getTag());
    }

    @Test
    public void addShouldAddUser() {
        UUID randomUUID = UUID.randomUUID();
        userTagDao.add(new UserTagEntity(randomUUID, randomUUID, "kaszanka"));
        assertEquals("kaszanka", userTagDao.getById(randomUUID.toString()).getTag());
    }
}