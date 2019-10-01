import entity.Period;
import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class UsersTagsManagementConfig extends Configuration {
    public Authentication authentication;
    public Database database;
    public String userApi;
    public Period period;

    @Getter
    @Setter
    public class Authentication {
        public String password;
        public String login;
    }

    @Getter
    @Setter
    public class Database {
        public String name;
    }


}
