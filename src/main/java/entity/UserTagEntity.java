package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Entity("UserTag")
public class UserTagEntity {
    @Id
    private UUID id;
    private UUID userId;
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
