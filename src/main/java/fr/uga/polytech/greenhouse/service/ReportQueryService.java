package fr.uga.polytech.greenhouse.service;

import fr.uga.polytech.greenhouse.domain.*; // for static metamodels
import fr.uga.polytech.greenhouse.domain.Report;
import fr.uga.polytech.greenhouse.repository.ReportRepository;
import fr.uga.polytech.greenhouse.service.criteria.ReportCriteria;
import fr.uga.polytech.greenhouse.service.dto.ReportDTO;
import fr.uga.polytech.greenhouse.service.mapper.ReportMapper;
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
 * Service for executing complex queries for {@link Report} entities in the database.
 * The main input is a {@link ReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReportDTO} or a {@link Page} of {@link ReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportQueryService extends QueryService<Report> {

    private final Logger log = LoggerFactory.getLogger(ReportQueryService.class);

    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;

    public ReportQueryService(ReportRepository reportRepository, ReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    /**
     * Return a {@link List} of {@link ReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReportDTO> findByCriteria(ReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Report> specification = createSpecification(criteria);
        return reportMapper.toDto(reportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportDTO> findByCriteria(ReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Report> specification = createSpecification(criteria);
        return reportRepository.findAll(specification, page).map(reportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Report> specification = createSpecification(criteria);
        return reportRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Report> createSpecification(ReportCriteria criteria) {
        Specification<Report> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Report_.id));
            }
            if (criteria.getTitleR() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitleR(), Report_.titleR));
            }
            if (criteria.getAlerts() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAlerts(), Report_.alerts));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Report_.createdAt));
            }
            if (criteria.getModifiedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedAt(), Report_.modifiedAt));
            }
            if (criteria.getLangue() != null) {
                specification = specification.and(buildSpecification(criteria.getLangue(), Report_.langue));
            }
            if (criteria.getTaskId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTaskId(), root -> root.join(Report_.tasks, JoinType.LEFT).get(Task_.id))
                    );
            }
            if (criteria.getAuthorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAuthorId(), root -> root.join(Report_.author, JoinType.LEFT).get(Profile_.id))
                    );
            }
            if (criteria.getHouseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getHouseId(), root -> root.join(Report_.house, JoinType.LEFT).get(GreenHouse_.id))
                    );
            }
        }
        return specification;
    }
}
