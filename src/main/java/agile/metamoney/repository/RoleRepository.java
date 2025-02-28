package agile.metamoney.repository;

import agile.metamoney.entity.Role;
import agile.metamoney.entity.constant.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(ERole erole);
}
