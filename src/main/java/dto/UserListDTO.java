package dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class UserListDTO {

    List<UserDTO> response;
    private Long offset;
    private Long limit;
    private Long max;

}
