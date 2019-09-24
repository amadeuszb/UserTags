package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@Getter
public class UserTagDTO {


    private String id;
    private String userId;
    private String tag;

    public UserTagDTO() {
    }


}
