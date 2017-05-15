package repositories;

import com.avaje.ebean.ExpressionList;
import models.Edge;
import models.user.User;
import play.Logger;
import scala.Option;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Simon Olofsson
 */
public class EdgesRepository {

    public Edge create(User renter, User tenant) {
        return create(renter, tenant, true);
    }

    public Edge create(User renter, User tenant, boolean active) {

        Edge edge = Edge.findByRenterAndTenant(renter.id, tenant.id);

        if (edge == null) {

            edge = new Edge(renter, tenant, active);
            save(edge);

        }
        return edge;

    }

    public List<Edge> findEdges(Option<Long> tenantId, Option<Long> renterId, Option<Boolean> active) {

        List<Function<ExpressionList<Edge>, ExpressionList<Edge>>> functions = Arrays.asList(

                exprList -> tenantId.isDefined() ? exprList.eq("tenant_id", tenantId.get()) : exprList,
                exprList -> renterId.isDefined() ? exprList.eq("renter_id", renterId.get()) : exprList,
                exprList -> active.isDefined() ? exprList.eq("active", active.get()) : exprList

        );

        return Edge.filterBy(functions);
    }

    public Edge findEdge(long renterId, long tenantId) {
        return Edge.findByRenterAndTenant(renterId, tenantId);
    }

    public void save(Edge edge) {
        edge.save();
    }

    public void delete(Edge edge) {

        if (edge == null) {
            throw new IllegalArgumentException("Edge to be deleted may not be null.");
        }

        edge.delete();

    }
}
