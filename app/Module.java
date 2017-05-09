import com.google.inject.AbstractModule;
import controllers.Secured;
import play.mvc.Security;
import repositories.RentalPeriodRepository.RentalPeriodRepository;
import repositories.RentalPeriodRepository.RentalPeriodStorage;
import repositories.accommodation.AccommodationRepository;
import repositories.accommodation.AccommodationStorage;
import repositories.address.AddressRepository;
import repositories.address.AddressStorage;
import repositories.facebookData.FacebookDataRepository;
import repositories.facebookData.FacebookDataStorage;
import repositories.interests.InterestStorage;
import repositories.interests.InterestsRepository;
import repositories.tenantProfile.TenantProfileRepository;
import repositories.tenantProfile.TenantProfileStorage;
import repositories.users.UserStorage;
import repositories.users.UsersRepository;

/**
 * @author Simon Olofsson
 */
public class Module extends AbstractModule {

    @Override
    protected void configure() {

        bind(AccommodationStorage.class).to(AccommodationRepository.class);
        bind(FacebookDataStorage.class).to(FacebookDataRepository.class);
        bind(InterestStorage.class).to(InterestsRepository.class);
        bind(UserStorage.class).to(UsersRepository.class);
        bind(TenantProfileStorage.class).to(TenantProfileRepository.class);
        bind(RentalPeriodStorage.class).to(RentalPeriodRepository.class);
        bind(AddressStorage.class).to(AddressRepository.class);

        bind(Security.Authenticator.class).to(Secured.class);

    }
}
