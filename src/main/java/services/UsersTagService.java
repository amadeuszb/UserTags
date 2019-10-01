package services;

import clientapi.UserClient;
import dao.UserTagDAO;
import dto.UserDTO;
import dto.UserListDTO;
import dto.UserTagDTO;
import dto.UserTagListDTO;
import entity.UserTagEntity;
import exceptions.BadRequestException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UsersTagService implements UserTagService {

    private static final Logger LOG = LoggerFactory.getLogger(UsersTagService.class);
    private UserTagDAO usersTagsDAO;
    private ModelMapper modelMapper;
    private UserClient userClient;

    public UsersTagService(UserTagDAO dao, ModelMapper modelMapper, UserClient userClient) {
        this.userClient = userClient;
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
        return modelMapper.map(userTagEntity, UserTagDTO.class);
    }

    @Override
    public UserTagListDTO getAllWithParams(String userId, Long offset, Long limit) {
        List<UserTagEntity> userTagEntity = usersTagsDAO.getAllWithParams(userId, offset, limit);
        Type listType = new TypeToken<List<UserTagDTO>>() {
        }.getType();
        List<UserTagDTO> userTagDTOList = modelMapper.map(userTagEntity, listType);
        return new UserTagListDTO(userTagDTOList, offset, limit, usersTagsDAO.getAmountOfTags());
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

    @Override
    public UserListDTO getUsersWithoutTag(Long limit, Long offset) {
        UserListDTO userDTOList = userClient
                .findAllWithLimitAndOffset(limit, offset);

        List<UserDTO> userDTOListFiltered = userDTOList
                .getResponse()
                .stream()
                .filter(element -> getAllWithParams(element.getId(), null, null).getResponse().isEmpty())
                .collect(Collectors.toList());
        userDTOList.setResponse(userDTOListFiltered);
        return userDTOList;
    }


    private void validateUserTag(UserTagDTO userTagDTO) throws BadRequestException {
        if (userTagDTO.getTag().isEmpty()) {
            LOG.error("Incorrect fields provided");
            throw new BadRequestException("Incorrect Fields");
        }
    }
}
