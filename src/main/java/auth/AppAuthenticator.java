package auth;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AppAuthenticator implements Authenticator<BasicCredentials, User> {
   
    private static final Map<String, Set<String>> VALID_USERS = ImmutableMap.of(
        "guest", ImmutableSet.of(),
        "user", ImmutableSet.of("USER"),
        "admin", ImmutableSet.of("ADMIN", "USER")
    );

    private String password;
    private String login;
    public AppAuthenticator(String password, String login){
        super();
        this.password = password;
        this.login = login;
    }
    @Override
    public Optional<User> authenticate(BasicCredentials credentials) {
        if (VALID_USERS.containsKey(credentials.getUsername()) && password.equals(credentials.getPassword())) {
            return Optional.of(new User(credentials.getUsername(), VALID_USERS.get(credentials.getUsername())));
        }
        return Optional.empty();
    }
}
