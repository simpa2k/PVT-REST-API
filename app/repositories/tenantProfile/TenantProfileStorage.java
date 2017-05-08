package repositories.tenantProfile;

import models.user.TenantProfile;

/**
 * @author Simon Olofsson
 */
public interface TenantProfileStorage {

    void save(TenantProfile tenantProfile);

}
