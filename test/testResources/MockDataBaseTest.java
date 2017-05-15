package testResources;

import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;
import repositories.AccommodationRepository;
import repositories.FacebookDataRepository;
import repositories.EdgesRepository;
import repositories.UsersRepository;
import testResources.mocks.MockAccommodationRepository;
import testResources.mocks.MockFacebookDataRepository;
import testResources.mocks.MockInterestRepository;
import testResources.mocks.MockUserRepository;

/**
 * @author Simon Olofsson
 */
public class MockDataBaseTest extends WithApplication {

    @Override
    protected Application provideApplication() {

        return new GuiceApplicationBuilder().overrides(binder -> {

            binder.bind(AccommodationRepository.class).to(MockAccommodationRepository.class);
            binder.bind(FacebookDataRepository.class).to(MockFacebookDataRepository.class);
            binder.bind(EdgesRepository.class).to(MockInterestRepository.class);
            binder.bind(UsersRepository.class).to(MockUserRepository.class);

        }).in(Mode.TEST).build();
    }
}
