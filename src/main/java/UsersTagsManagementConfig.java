import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotEmpty;

class UsersTagsManagementConfig extends Configuration {

    public String password;


    public String login;

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty
    public String getLogin() {
        return login;
    }

    @JsonProperty
    public void setLogin(String login) {
        this.login = login;
    }
}
