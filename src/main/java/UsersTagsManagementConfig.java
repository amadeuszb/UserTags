import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;


class UsersTagsManagementConfig extends Configuration {

    public String password;

    public String login;

    public String databaseName;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

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
