package fr.uga.polytech.greenhouse.service;

import fr.uga.polytech.greenhouse.domain.*; // for static metamodels
import fr.uga.polytech.greenhouse.domain.GreenHouse;
import fr.uga.polytech.greenhouse.repository.GreenHouseRepository;
import fr.uga.polytech.greenhouse.service.criteria.GreenHouseCriteria;
import fr.uga.polytech.greenhouse.service.dto.GreenHouseDTO;
import fr.uga.polytech.greenhouse.service.mapper.GreenHouseMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GreenHouse} entities in the database.
 * The main input is a {@link GreenHouseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GreenHouseDTO} or a {@link Page} of {@link GreenHouseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GreenHouseQueryService extends QueryService<GreenHouse> {

    private final Logger log = LoggerFactory.getLogger(GreenHouseQueryService.class);

    private final GreenHouseRepository greenHouseRepository;

    private final GreenHouseMapper greenHouseMapper;

    public GreenHouseQueryService(GreenHouseRepository greenHouseRepository, GreenHouseMapper greenHouseMapper) {
        this.greenHouseRepository = greenHouseRepository;
        this.greenHouseMapper = greenHouseMapper;
    }

    /**
     * Return a {@link List} of {@link GreenHouseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GreenHouseDTO> findByCriteria(GreenHouseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GreenHouse> specification = createSpecification(criteria);
        return greenHouseMapper.toDto(greenHouseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GreenHouseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GreenHouseDTO> findByCriteria(GreenHouseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GreenHouse> specification = createSpecification(criteria);
        return greenHouseRepository.findAll(specification, page).map(greenHouseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GreenHouseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GreenHouse> specification = createSpecification(criteria);
        return greenHouseRepository.count(specification);
    }

    /**
     * Function to convert {@link GreenHouseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GreenHouse> createSpecification(GreenHouseCriteria criteria) {
        Specification<GreenHouse> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GreenHouse_.id));
            }
            if (criteria.getNameG() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameG(), GreenHouse_.nameG));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), GreenHouse_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), GreenHouse_.longitude));
            }
            if (criteria.getReportId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getReportId(), root -> root.join(GreenHouse_.reports, JoinType.LEFT).get(Report_.id))
                    );
            }
            if (criteria.getObservateurId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getObservateurId(),
                            root -> root.join(GreenHouse_.observateur, JoinType.LEFT).get(Profile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
