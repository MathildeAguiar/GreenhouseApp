package fr.uga.polytech.greenhouse.service;

import fr.uga.polytech.greenhouse.service.dto.GreenHouseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.uga.polytech.greenhouse.domain.GreenHouse}.
 */
public interface GreenHouseService {
    /**
     * Save a greenHouse.
     *
     * @param greenHouseDTO the entity to save.
     * @return the persisted entity.
     */
    GreenHouseDTO save(GreenHouseDTO greenHouseDTO);

    /**
     * Partially updates a greenHouse.
     *
     * @param greenHouseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GreenHouseDTO> partialUpdate(GreenHouseDTO greenHouseDTO);

    /**
     * Get all the greenHouses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GreenHouseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" greenHouse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GreenHouseDTO> findOne(Long id);

    /**
     * Delete the "id" greenHouse.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
