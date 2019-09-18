package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Entity("UserTag")
public class UserTagEntity {
    @Id
    @NotEmpty
    private UUID id;
    @NotEmpty
    private UUID userId;
    @NotEmpty
    private String tag;

    public UserTagEntity() {
    }

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }
    public void setUserId(String userId) {
        this.userId = UUID.fromString(userId);
    }

}
