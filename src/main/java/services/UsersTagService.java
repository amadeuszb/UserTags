package services;

import dao.UserTagDAO;
import dto.UserTagDTO;
import entity.UserTagEntity;
import exceptions.BadRequestException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class UsersTagService implements UserTagService {

    private static final Logger LOG = LoggerFactory.getLogger(UsersTagService.class);
    private UserTagDAO usersTagsDAO;
    private ModelMapper modelMapper;

    public UsersTagService(UserTagDAO dao, ModelMapper modelMapper) {
        this.usersTagsDAO = dao;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserTagDTO getById(String id) throws BadRequestException {
        UserTagEntity userTagEntity = usersTagsDAO.getById(id);
        if (userTagEntity == null) {
            LOG.error("Incorrect UUID provided: {}, user Tag does not exist", id);
            throw new BadRequestException("User Tag does not exist");
        }
        UserTagDTO userTagDTO = modelMapper.map(userTagEntity, UserTagDTO.class);
        return userTagDTO;
    }

    @Override
    public List<UserTagDTO> getAllWithParams(String userId, Long offset, Long limit) {
        List<UserTagEntity> userTagEntity = usersTagsDAO.getAllWithParams(userId, offset, limit);
        Type listType = new TypeToken<List<UserTagDTO>>() {
        }.getType();
        List<UserTagDTO> userTagDTOList = modelMapper.map(userTagEntity, listType);
        return userTagDTOList;
    }

    @Override
    public void removeByUserId(String userId) throws BadRequestException {
        UUID id;
        try {
            id = UUID.fromString(userId);
        } catch (Exception e) {
            LOG.error("Incorrect UUID provided to remove {}", userId);
            throw new BadRequestException("Incorrect UUID");
        }
        usersTagsDAO.removeByUserId(id);
    }

    @Override
    public void update(UserTagDTO userTagDTO) throws BadRequestException {
        validateUserTag(userTagDTO);
        UserTagEntity userTagEntity = modelMapper.map(userTagDTO, UserTagEntity.class);
        usersTagsDAO.update(userTagEntity);
    }

    @Override
    public String add(UserTagDTO userTagDTO) throws BadRequestException {
        String id = UUID.randomUUID().toString();
        userTagDTO.setId(id);
        validateUserTag(userTagDTO);
        UserTagEntity userTagEntity = modelMapper.map(userTagDTO, UserTagEntity.class);
        usersTagsDAO.add(userTagEntity);
        return id;

    }

    private void validateUserTag(UserTagDTO userTagDTO) throws BadRequestException {
        if (userTagDTO.getTag().isEmpty()) {
            LOG.error("Incorrect fields provided");
            throw new BadRequestException("Incorrect Fields");
        }
    }
}
