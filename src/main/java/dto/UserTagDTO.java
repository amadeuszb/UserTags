package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@Setter
@Getter
public class UserTagDTO {

    @NotEmpty
    private String id;
    @NotEmpty
    private String userId;
    @NotEmpty
    private String tag;

    public UserTagDTO() {
    }


}
