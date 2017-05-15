package testResources.mocks;

import models.Edge;
import models.user.User;
import repositories.EdgesRepository;
import scala.Option;

import java.util.List;

/**
 * @author Simon Olofsson
 */
public class MockInterestRepository extends EdgesRepository {

    @Override
    public Edge create(User renter, User tenant) {
        return null;
    }

    @Override
    public List<Edge> findEdges(Option<Long> tenantId, Option<Long> accommodationId, Option<Boolean> active) {
        return null;
    }

    @Override
    public Edge findEdge(long tenantId, long accommodationId) {
        return null;
    }

    @Override
    public void save(Edge edge) {

    }

    @Override
    public void delete(Edge edge) {

    }
}
