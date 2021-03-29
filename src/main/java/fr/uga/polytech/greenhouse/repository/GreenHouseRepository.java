package fr.uga.polytech.greenhouse.repository;

import fr.uga.polytech.greenhouse.domain.GreenHouse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GreenHouse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GreenHouseRepository extends JpaRepository<GreenHouse, Long>, JpaSpecificationExecutor<GreenHouse> {}
