package clientapi;

import dto.UserListDTO;
import dto.UserTagListDTO;
import feign.Param;
import feign.RequestLine;

public interface UserClient {

    @RequestLine("GET ?offset={offset}&limit={limit}")
    UserListDTO findAllWithLimitAndOffset(@Param("limit") Long limit
            , @Param("offset") Long offset);

    @RequestLine("GET")
    UserTagListDTO findAll();


}
