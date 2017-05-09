package unit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import controllers.SecurityController;
import org.junit.Test;
import org.mockito.Mock;
import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import play.test.WithApplication;
import services.UsersService;
import testResources.mocks.SecurityMock;

import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

/**
 * @author Simon Olofsson
 */
public class UsersControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {

        return new GuiceApplicationBuilder().overrides(binder -> {

            binder.bind(Security.Authenticator.class).to(SecurityMock.class);

        }).in(Mode.TEST).build();
    }

    @Mock
    private UsersService usersService;

    @Test
    public void testReturnsBadRequestOnMalformedRequestBody() throws JsonProcessingException {

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.UsersController.createProfile());
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, "");
        Result result = route(fakeRequest);
    }
}
