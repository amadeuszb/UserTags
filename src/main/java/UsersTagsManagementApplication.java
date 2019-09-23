import auth.AppAuthenticator;
import auth.AppAuthorizer;
import auth.User;
import dao.UserTagDAO;
import dao.UsersTagDAO;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.modelmapper.ModelMapper;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources.UserTagResource;
import services.RequestExceptionMapper;
import services.UserTagService;
import services.UsersTagService;

public class UsersTagsManagementApplication extends Application<UsersTagsManagementConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(UsersTagsManagementApplication.class);
    private Datastore datastore;
    private UserTagDAO usersTagDao;
    private UserTagService usersTagService;
    private ModelMapper modelMapper;

    public static void main(String[] args) throws Exception {
        new UsersTagsManagementApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<UsersTagsManagementConfig> bootstrap) {
        modelMapper = new ModelMapper();
        super.initialize(bootstrap);

    }

    @Override
    public void run(UsersTagsManagementConfig configuration, Environment environment) {
        LOG.info("Application Users Management Started");
        datastore = new MorphiaConfig(configuration.getDatabaseName()).getDatastore();
        usersTagDao = new UsersTagDAO(datastore);
        usersTagService = new UsersTagService(usersTagDao, modelMapper);
        final UserTagResource personService = new UserTagResource(usersTagService, environment.getValidator());
        initializeAuth(environment, configuration.getPassword(), configuration.getLogin());
        environment.jersey().register(new RequestExceptionMapper());
        environment.jersey().register(personService);
    }

    private void initializeAuth(Environment environment, String password, String login){
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new AppAuthenticator(password, login))
                .setAuthorizer(new AppAuthorizer())
                .setRealm("App Security")
                .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }


}
