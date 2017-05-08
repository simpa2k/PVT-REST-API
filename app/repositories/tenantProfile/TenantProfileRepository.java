package repositories.tenantProfile;

import models.user.TenantProfile;

/**
 * @author Simon Olofsson
 */
public class TenantProfileRepository implements TenantProfileStorage {

    @Override
    public void save(TenantProfile tenantProfile) {
        tenantProfile.save();
    }
}
