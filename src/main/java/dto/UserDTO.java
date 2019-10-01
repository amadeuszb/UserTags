package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@Setter
@Getter
public class UserDTO {

    @NotEmpty
    private String id;
    @NotEmpty
    private String email;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String creationDate;

    private String birthDate;

    private List<UserTagDTO> userTags;

    public UserDTO() {
    }


}
