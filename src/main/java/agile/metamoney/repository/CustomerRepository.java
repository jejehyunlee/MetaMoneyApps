package agile.metamoney.repository;

import agile.metamoney.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findFirstByIdAndIsActive(String id, Boolean isActive);
}
