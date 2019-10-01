import auth.AppAuthenticator;
import auth.AppAuthorizer;
import auth.User;
import clientapi.UserClient;
import dao.UserTagDAO;
import dao.UsersTagDAO;
import dto.UserTagDTO;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
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
import scheduler.TriggerJob;
import services.RequestExceptionMapper;
import services.UserTagService;
import services.UsersTagService;

public class UsersTagsManagementApplication extends Application<UsersTagsManagementConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(UsersTagsManagementApplication.class);
    private Datastore datastore;
    private UserTagDAO usersTagDao;
    private UserTagService usersTagService;
    private ModelMapper modelMapper;
    private UserClient userClient;
    private String userApi;

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
        userApi = configuration.getUserApi();
        LOG.info("Application Users Management Started");
        datastore = new MorphiaConfig(configuration.getDatabase().getName()).getDatastore();
        usersTagDao = new UsersTagDAO(datastore);
        initializeUserClient(configuration.getAuthentication().getPassword(), configuration.getAuthentication().getLogin());
        usersTagService = new UsersTagService(usersTagDao, modelMapper, userClient);

        initializeJobs(usersTagService, configuration.getPeriod().getZodiac());
        final UserTagResource userTagResource = new UserTagResource(usersTagService);
        initializeAuth(environment, configuration.getAuthentication().getPassword(), configuration.getAuthentication().getLogin());
        environment.jersey().register(new RequestExceptionMapper());
        environment.jersey().register(userTagResource);
    }

    private void initializeJobs(UserTagService userTagService, int time) {
        try {
            new TriggerJob(userTagService, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeAuth(Environment environment, String password, String login) {
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new AppAuthenticator(password, login))
                .setAuthorizer(new AppAuthorizer())
                .setRealm("App Security")
                .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }

    private void initializeUserClient(String password, String login) {
        userClient = Feign
                .builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(new BasicAuthRequestInterceptor(login, password))
                .logger(new Slf4jLogger(UserTagDTO.class))
                .target(UserClient.class, userApi);
    }


}
