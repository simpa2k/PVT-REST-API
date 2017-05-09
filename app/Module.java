import com.google.inject.AbstractModule;
import controllers.Secured;
import play.mvc.Security;
import repositories.RentalPeriodRepository;
import repositories.AccommodationRepository;
import repositories.AddressRepository;
import repositories.FacebookDataRepository;
import repositories.InterestsRepository;
import repositories.TenantProfileRepository;
import repositories.UsersRepository;

/**
 * @author Simon Olofsson
 */
public class Module extends AbstractModule {

    @Override
    protected void configure() {

        bind(Security.Authenticator.class).to(Secured.class);

    }
}
