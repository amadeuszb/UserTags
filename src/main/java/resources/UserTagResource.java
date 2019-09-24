package resources;

import auth.User;
import com.codahale.metrics.annotation.Timed;
import dto.UserTagDTO;
import exceptions.BadRequestException;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.UserTagService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/user-tag")
public class UserTagResource {
    private static final Logger LOG = LoggerFactory.getLogger(UserTagResource.class);
    private UserTagService usersTagService;

    public UserTagResource(UserTagService service) {
        usersTagService = service;
    }

    @RolesAllowed({"ADMIN"})
    @POST
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addUserTag(@Valid UserTagDTO userTag) throws BadRequestException {
        LOG.info("Adding new user tag with tag: {}", userTag.getTag());
        return Response.ok(usersTagService.add(userTag)).build();
    }

    @RolesAllowed({"ADMIN"})
    @GET
    @Timed
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserTag(@PathParam("id") String id) throws BadRequestException {
        LOG.info("Querying about userTag with id: {}", id);
        return Response.ok().entity(usersTagService.getById(id)).build();
    }

    @RolesAllowed({"ADMIN"})
    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersTags(@QueryParam("userId") String userId,
                                 @DefaultValue("0") @QueryParam("offset") @Min(0) Long offset,
                                 @DefaultValue("20") @QueryParam("limit") @Min(1) @Max(100) Long limit,
                                 @Auth User user) {
        LOG.info("Querying about userTags with params userId: {}, offset: {}, limit: {}", userId, offset, limit);
        return Response.ok().entity(usersTagService.getAllWithParams(userId, offset, limit)).build();
    }

    @RolesAllowed({"ADMIN"})
    @PUT
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateUserTag(UserTagDTO userTag) throws BadRequestException {
        usersTagService.update(userTag);
        LOG.info("Updating userTag with id: {}", userTag.getId());
        return Response.ok().build();
    }

    @RolesAllowed({"ADMIN"})
    @DELETE
    @Timed
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUserTag(@PathParam("userId") String userId) throws BadRequestException {
        LOG.info("Removing userTag with userId: {}", userId);
        usersTagService.removeByUserId(userId);
        return Response.ok().build();
    }


}