package agile.metamoney.service;

import agile.metamoney.entity.Role;
import agile.metamoney.entity.constant.ERole;

public interface RoleService {
    Role getOrsave(ERole erole);
}
