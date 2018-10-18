package group.greenbyte.lunchplanner.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanRepository extends JpaRepository<Can, Long> {

}
