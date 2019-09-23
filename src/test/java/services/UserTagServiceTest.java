package services;

import dao.UserTagDAO;
import dto.UserTagDTO;
import entity.UserTagEntity;
import exceptions.BadRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTagServiceTest {
    @Mock
    UserTagDAO userDAO;
    ModelMapper modelMapper;

    private UserTagService userService;
    private UUID uuid1 = UUID.randomUUID();
    private UUID uuid2 = UUID.randomUUID();
    private UserTagEntity userTagEntity = new UserTagEntity(uuid1, uuid1, "Random");
    private UserTagEntity userTagEntity2 = new UserTagEntity(uuid2, uuid2, "Random2");

    private UserTagDTO userTagDto = new UserTagDTO(uuid1.toString(), uuid1.toString(), "Random");
    private UserTagDTO userTagDto2 = new UserTagDTO(uuid2.toString(), uuid2.toString(), "Random2");
    private LinkedList<UserTagDTO> listUserTagDTO;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        userService = new UsersTagService(userDAO, modelMapper);
        listUserTagDTO = new LinkedList<>();
        listUserTagDTO.add(userTagDto);
        listUserTagDTO.add(userTagDto);
    }

    @Test
    public void getByIdShouldReturnCorrectUserTag() throws BadRequestException {
        when(userDAO.getById(anyString())).thenReturn(userTagEntity);
        UserTagDTO result = userService.getById(uuid1.toString());
        assertEquals(userTagDto.getId(), result.getId());
    }

    @Test
    public void getAllWithParamsShouldReturnListOfUserTags() {
        List<UserTagEntity> sampleList = new LinkedList<>();
        List<UserTagDTO> sampleListDTO = new LinkedList<>();
        sampleListDTO.add(userTagDto);
        sampleList.add(userTagEntity);
        when(userDAO.getAllWithParams(any(), anyLong(), anyLong())).thenReturn(sampleList);
        List<UserTagDTO> result = userService.getAllWithParams(null, 0L, 20L);
        assertEquals(1, result.size());
    }


    @Test
    public void updateShouldUpdateUserTag() throws BadRequestException {
        UserTagEntity sampleUserTag = new UserTagEntity(uuid1, uuid1, "Random Text");
        when(userDAO.getById(anyString())).thenReturn(sampleUserTag);
        assertEquals("Random Text", userService.getById(uuid1.toString()).getTag());
    }

    @Test
    public void addShouldAddUserTag() throws BadRequestException {
        UUID randomUUID = UUID.randomUUID();
        UserTagEntity sampleUserTag = new UserTagEntity(randomUUID, randomUUID, "Tag Random");
        when(userDAO.getById(randomUUID.toString())).thenReturn(sampleUserTag);
        userService.add(new UserTagDTO(randomUUID.toString(), randomUUID.toString(), "Tag Random"));
        assertEquals("Tag Random", userService.getById(randomUUID.toString()).getTag());
    }

}