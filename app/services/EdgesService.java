package services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Edge;
import models.user.User;
import repositories.EdgesRepository;
import repositories.UsersRepository;
import scala.Option;
import exceptions.OffsetOutOfRangeException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Simon Olofsson
 */
public class EdgesService {

    private final EdgesRepository edgesRepository;
    private final UsersRepository usersRepository;

    @Inject
    public EdgesService(EdgesRepository edgesRepository, UsersRepository usersRepository) {

        this.edgesRepository = edgesRepository;
        this.usersRepository = usersRepository;

    }

    public void addEdges(User renter, ArrayNode tenantIds) {

        tenantIds.forEach(tenantId -> {

            User tenant = usersRepository.findById(tenantId.asLong());

            if (tenant != null) {

                Edge interest = edgesRepository.create(renter, tenant);
                renter.addInterest(interest);

            }
        });
    }

    public Edge setMutual(long renterId, long tenantId, boolean active) {

        Edge edge = edgesRepository.findEdge(renterId, tenantId);
        edge.active = active;

        edgesRepository.save(edge);

        return edge;

    }

    public void withdrawInterest(long renterId, long tenantId) {

        Edge interest = edgesRepository.findEdge(renterId, tenantId);

        if (interest != null) {
            edgesRepository.delete(interest);
        }
    }

    public List<Edge> getSubset(Option<Integer> count, Option<Integer> offset,
                                Option<Long> tenantId, Option<Long> renterId, Option<Boolean> active) throws OffsetOutOfRangeException {

        if (!active.isDefined()) { // Setting a default value to true.
            active = Option.apply(true);
        }

        List<Edge> edges = edgesRepository.findEdges(tenantId, renterId, active);

        int evaluatedOffset = offset.isDefined() ? offset.get() : 0;
        int evaluatedCount = count.isDefined() && ((count.get() + evaluatedOffset) < edges.size()) ? count.get() : edges.size();

        if (evaluatedOffset > edges.size()) {
            throw new OffsetOutOfRangeException("The offset you have requested is larger than the number of results.");
        }

        return edges.subList(evaluatedOffset, evaluatedCount);
    }

    public boolean deleteInterest(long tenantId, long accommodationId) {

        Edge interest = edgesRepository.findEdge(tenantId, accommodationId);

        if (interest == null) {
            return false;
        }

        edgesRepository.delete(interest);

        return true;
    }
}
