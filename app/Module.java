import com.google.inject.AbstractModule;
import controllers.Secured;
import play.mvc.Security;

/**
 * @author Simon Olofsson
 */
public class Module extends AbstractModule {

    @Override
    protected void configure() {

        bind(Security.Authenticator.class).to(Secured.class);

    }
}
