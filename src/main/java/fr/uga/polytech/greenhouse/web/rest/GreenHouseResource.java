package fr.uga.polytech.greenhouse.web.rest;

import fr.uga.polytech.greenhouse.repository.GreenHouseRepository;
import fr.uga.polytech.greenhouse.service.GreenHouseQueryService;
import fr.uga.polytech.greenhouse.service.GreenHouseService;
import fr.uga.polytech.greenhouse.service.criteria.GreenHouseCriteria;
import fr.uga.polytech.greenhouse.service.dto.GreenHouseDTO;
import fr.uga.polytech.greenhouse.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.uga.polytech.greenhouse.domain.GreenHouse}.
 */
@RestController
@RequestMapping("/api")
public class GreenHouseResource {

    private final Logger log = LoggerFactory.getLogger(GreenHouseResource.class);

    private static final String ENTITY_NAME = "greenHouse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GreenHouseService greenHouseService;

    private final GreenHouseRepository greenHouseRepository;

    private final GreenHouseQueryService greenHouseQueryService;

    public GreenHouseResource(
        GreenHouseService greenHouseService,
        GreenHouseRepository greenHouseRepository,
        GreenHouseQueryService greenHouseQueryService
    ) {
        this.greenHouseService = greenHouseService;
        this.greenHouseRepository = greenHouseRepository;
        this.greenHouseQueryService = greenHouseQueryService;
    }

    /**
     * {@code POST  /green-houses} : Create a new greenHouse.
     *
     * @param greenHouseDTO the greenHouseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new greenHouseDTO, or with status {@code 400 (Bad Request)} if the greenHouse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/green-houses")
    public ResponseEntity<GreenHouseDTO> createGreenHouse(@Valid @RequestBody GreenHouseDTO greenHouseDTO) throws URISyntaxException {
        log.debug("REST request to save GreenHouse : {}", greenHouseDTO);
        if (greenHouseDTO.getId() != null) {
            throw new BadRequestAlertException("A new greenHouse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GreenHouseDTO result = greenHouseService.save(greenHouseDTO);
        return ResponseEntity
            .created(new URI("/api/green-houses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /green-houses/:id} : Updates an existing greenHouse.
     *
     * @param id the id of the greenHouseDTO to save.
     * @param greenHouseDTO the greenHouseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated greenHouseDTO,
     * or with status {@code 400 (Bad Request)} if the greenHouseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the greenHouseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/green-houses/{id}")
    public ResponseEntity<GreenHouseDTO> updateGreenHouse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GreenHouseDTO greenHouseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GreenHouse : {}, {}", id, greenHouseDTO);
        if (greenHouseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, greenHouseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!greenHouseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GreenHouseDTO result = greenHouseService.save(greenHouseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, greenHouseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /green-houses/:id} : Partial updates given fields of an existing greenHouse, field will ignore if it is null
     *
     * @param id the id of the greenHouseDTO to save.
     * @param greenHouseDTO the greenHouseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated greenHouseDTO,
     * or with status {@code 400 (Bad Request)} if the greenHouseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the greenHouseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the greenHouseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/green-houses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<GreenHouseDTO> partialUpdateGreenHouse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GreenHouseDTO greenHouseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GreenHouse partially : {}, {}", id, greenHouseDTO);
        if (greenHouseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, greenHouseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!greenHouseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GreenHouseDTO> result = greenHouseService.partialUpdate(greenHouseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, greenHouseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /green-houses} : get all the greenHouses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of greenHouses in body.
     */
    @GetMapping("/green-houses")
    public ResponseEntity<List<GreenHouseDTO>> getAllGreenHouses(GreenHouseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GreenHouses by criteria: {}", criteria);
        Page<GreenHouseDTO> page = greenHouseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /green-houses/count} : count all the greenHouses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/green-houses/count")
    public ResponseEntity<Long> countGreenHouses(GreenHouseCriteria criteria) {
        log.debug("REST request to count GreenHouses by criteria: {}", criteria);
        return ResponseEntity.ok().body(greenHouseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /green-houses/:id} : get the "id" greenHouse.
     *
     * @param id the id of the greenHouseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the greenHouseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/green-houses/{id}")
    public ResponseEntity<GreenHouseDTO> getGreenHouse(@PathVariable Long id) {
        log.debug("REST request to get GreenHouse : {}", id);
        Optional<GreenHouseDTO> greenHouseDTO = greenHouseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(greenHouseDTO);
    }

    /**
     * {@code DELETE  /green-houses/:id} : delete the "id" greenHouse.
     *
     * @param id the id of the greenHouseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/green-houses/{id}")
    public ResponseEntity<Void> deleteGreenHouse(@PathVariable Long id) {
        log.debug("REST request to delete GreenHouse : {}", id);
        greenHouseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
