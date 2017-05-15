package integration;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.OffsetOutOfRangeException;
import models.Edge;
import models.user.User;
import org.junit.Test;
import play.Logger;
import play.libs.Json;
import repositories.EdgesRepository;
import repositories.UsersRepository;
import scala.Option;
import services.EdgesService;
import testResources.BaseTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Simon Olofsson
 */
public class EdgesStackTest extends BaseTest {

    private EdgesRepository edgesRepository = new EdgesRepository();
    private EdgesService edgesService = new EdgesService(edgesRepository, new UsersRepository());

    @Test
    public void getEdgesFromDatabase() throws OffsetOutOfRangeException {

        User renter = new User("renter@renter.com", "Renter");
        User tenant = new User("tenant@tenant.com", "Tenant");
        User tenant2 = new User("tenant2@tenant.com", "Tenant2");

        UsersRepository usersRepository = new UsersRepository();

        usersRepository.save(renter);
        usersRepository.save(tenant);
        usersRepository.save(tenant2);

        Edge edge = new Edge(renter, tenant);
        Edge edge2 = new Edge(renter, tenant2);

        edgesRepository.save(edge);
        edgesRepository.save(edge2);

        Option<Integer> count = Option.empty();
        Option<Integer> offset = Option.empty();
        Option<Long> tenantId = Option.apply(tenant.id);
        Option<Long> renterId = Option.empty();
        Option<Boolean> active = Option.empty();

        List<Edge> edges = edgesService.getSubset(count, offset, tenantId, renterId, active);

        assertEquals(1, edges.size());
        assertEquals(edge, edges.get(0));
        assertFalse(edges.contains(edge2));

    }

    @Test
    public void addEdges() throws OffsetOutOfRangeException {

        User renter = new User("renter@renter.com", "Renter");
        User tenant = new User("tenant@tenant.com", "Tenant");
        User tenant2 = new User("tenant2@tenant.com", "Tenant2");

        UsersRepository usersRepository = new UsersRepository();

        usersRepository.save(renter);
        usersRepository.save(tenant);
        usersRepository.save(tenant2);

        ArrayNode tenantIds = Json.newArray();
        tenantIds.add(tenant.id);
        tenantIds.add(tenant2.id);

        edgesService.addEdges(renter, tenantIds);

        Option<Integer> count = Option.empty();
        Option<Integer> offset = Option.empty();
        Option<Long> tenantId = Option.apply(tenant.id);
        Option<Long> renterId = Option.empty();
        Option<Boolean> mutual = Option.empty();

        List<Edge> interests = edgesService.getSubset(count, offset, tenantId, renterId, mutual);

        assertFalse(interests.isEmpty());
        Logger.debug(interests.toString());


    }

    @Test
    public void possibleToCreateInactiveEdge() {

        User renter = new User("renter@renter.com", "Renter");
        User tenant = new User("tenant@tenant.com", "Tenant");

        UsersRepository usersRepository = new UsersRepository();

        usersRepository.save(renter);
        usersRepository.save(tenant);

        ObjectNode requestBody = Json.newObject();
        requestBody.put("tenantId", tenant.id);
        requestBody.put("active", "false");

        Edge edge = edgesService.addEdge(renter, requestBody);

        assertEquals(renter, edge.renter);
        assertEquals(tenant, edge.tenant);
        assertEquals(false, edge.active);

    }

    @Test
    public void canFilterOnInactiveEdges() throws OffsetOutOfRangeException {

        User renter = new User("renter@renter.com", "Renter");
        User tenant = new User("tenant@tenant.com", "Tenant");

        UsersRepository usersRepository = new UsersRepository();

        usersRepository.save(renter);
        usersRepository.save(tenant);

        ArrayNode tenantIds = Json.newArray();
        tenantIds.add(tenant.id);

        edgesService.addEdges(renter, tenantIds);

        Option<Integer> count = Option.empty();
        Option<Integer> offset = Option.empty();
        Option<Long> tenantId = Option.apply(tenant.id);
        Option<Long> renterId = Option.apply(renter.id);
        Option<Boolean> mutual = Option.empty();

        List<Edge> interests = edgesService.getSubset(count, offset, tenantId, renterId, mutual);

        assertFalse(interests.isEmpty());
        assertTrue(interests.get(0).active);

        edgesService.setMutual(renter.id, tenant.id, false);

        interests = edgesService.getSubset(count, offset, tenantId, renterId, mutual);

        assertTrue(interests.isEmpty());

    }

}
