package repositories;

import models.user.TenantProfile;

/**
 * @author Simon Olofsson
 */
public class TenantProfileRepository {

    public void save(TenantProfile tenantProfile) {
        tenantProfile.save();
    }
}
