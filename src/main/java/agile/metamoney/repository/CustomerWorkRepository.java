package agile.metamoney.repository;

import agile.metamoney.entity.CustomerWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerWorkRepository extends JpaRepository<CustomerWork,String> {
}
