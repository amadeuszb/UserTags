package services;

import dto.UserTagDTO;
import exceptions.BadRequestException;

import java.util.List;

public interface UserTagService {
    UserTagDTO getById(String id) throws BadRequestException;

    List<UserTagDTO> getAllWithParams(String userId, Long offset, Long limit);

    void removeByUserId(String id) throws BadRequestException;

    void update(UserTagDTO userTagDTO) throws BadRequestException;

    String add(UserTagDTO userTagDTO) throws BadRequestException;
}
