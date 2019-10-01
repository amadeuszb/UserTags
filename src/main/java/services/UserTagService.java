package services;

import dto.UserListDTO;
import dto.UserTagDTO;
import dto.UserTagListDTO;
import exceptions.BadRequestException;

public interface UserTagService {
    UserTagDTO getById(String id) throws BadRequestException;

    UserTagListDTO getAllWithParams(String userId, Long offset, Long limit);

    void removeByUserId(String id) throws BadRequestException;

    void update(UserTagDTO userTagDTO) throws BadRequestException;

    String add(UserTagDTO userTagDTO) throws BadRequestException;

    UserListDTO getUsersWithoutTag(Long limit, Long offset);
}
