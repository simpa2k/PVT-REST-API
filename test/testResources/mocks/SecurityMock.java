package testResources.mocks;

import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * @author Simon Olofsson
 */
public class SecurityMock extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        return "demo@example.com";
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return unauthorized();
    }
}
