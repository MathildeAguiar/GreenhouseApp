package fr.uga.polytech.greenhouse.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.uga.polytech.greenhouse.IntegrationTest;
import fr.uga.polytech.greenhouse.domain.GreenHouse;
import fr.uga.polytech.greenhouse.domain.Profile;
import fr.uga.polytech.greenhouse.domain.Report;
import fr.uga.polytech.greenhouse.repository.GreenHouseRepository;
import fr.uga.polytech.greenhouse.service.criteria.GreenHouseCriteria;
import fr.uga.polytech.greenhouse.service.dto.GreenHouseDTO;
import fr.uga.polytech.greenhouse.service.mapper.GreenHouseMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GreenHouseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GreenHouseResourceIT {

    private static final String DEFAULT_NAME_G = "AAAAAAAAAA";
    private static final String UPDATED_NAME_G = "BBBBBBBBBB";

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;
    private static final Float SMALLER_LATITUDE = 1F - 1F;

    private static final Float DEFAULT_LONGITUDE = 1F;
    private static final Float UPDATED_LONGITUDE = 2F;
    private static final Float SMALLER_LONGITUDE = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/green-houses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GreenHouseRepository greenHouseRepository;

    @Autowired
    private GreenHouseMapper greenHouseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGreenHouseMockMvc;

    private GreenHouse greenHouse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GreenHouse createEntity(EntityManager em) {
        GreenHouse greenHouse = new GreenHouse().nameG(DEFAULT_NAME_G).latitude(DEFAULT_LATITUDE).longitude(DEFAULT_LONGITUDE);
        return greenHouse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GreenHouse createUpdatedEntity(EntityManager em) {
        GreenHouse greenHouse = new GreenHouse().nameG(UPDATED_NAME_G).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);
        return greenHouse;
    }

    @BeforeEach
    public void initTest() {
        greenHouse = createEntity(em);
    }

    @Test
    @Transactional
    void createGreenHouse() throws Exception {
        int databaseSizeBeforeCreate = greenHouseRepository.findAll().size();
        // Create the GreenHouse
        GreenHouseDTO greenHouseDTO = greenHouseMapper.toDto(greenHouse);
        restGreenHouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(greenHouseDTO)))
            .andExpect(status().isCreated());

        // Validate the GreenHouse in the database
        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeCreate + 1);
        GreenHouse testGreenHouse = greenHouseList.get(greenHouseList.size() - 1);
        assertThat(testGreenHouse.getNameG()).isEqualTo(DEFAULT_NAME_G);
        assertThat(testGreenHouse.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testGreenHouse.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void createGreenHouseWithExistingId() throws Exception {
        // Create the GreenHouse with an existing ID
        greenHouse.setId(1L);
        GreenHouseDTO greenHouseDTO = greenHouseMapper.toDto(greenHouse);

        int databaseSizeBeforeCreate = greenHouseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGreenHouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(greenHouseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GreenHouse in the database
        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameGIsRequired() throws Exception {
        int databaseSizeBeforeTest = greenHouseRepository.findAll().size();
        // set the field null
        greenHouse.setNameG(null);

        // Create the GreenHouse, which fails.
        GreenHouseDTO greenHouseDTO = greenHouseMapper.toDto(greenHouse);

        restGreenHouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(greenHouseDTO)))
            .andExpect(status().isBadRequest());

        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGreenHouses() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList
        restGreenHouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(greenHouse.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameG").value(hasItem(DEFAULT_NAME_G)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())));
    }

    @Test
    @Transactional
    void getGreenHouse() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get the greenHouse
        restGreenHouseMockMvc
            .perform(get(ENTITY_API_URL_ID, greenHouse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(greenHouse.getId().intValue()))
            .andExpect(jsonPath("$.nameG").value(DEFAULT_NAME_G))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()));
    }

    @Test
    @Transactional
    void getGreenHousesByIdFiltering() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        Long id = greenHouse.getId();

        defaultGreenHouseShouldBeFound("id.equals=" + id);
        defaultGreenHouseShouldNotBeFound("id.notEquals=" + id);

        defaultGreenHouseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGreenHouseShouldNotBeFound("id.greaterThan=" + id);

        defaultGreenHouseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGreenHouseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGreenHousesByNameGIsEqualToSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where nameG equals to DEFAULT_NAME_G
        defaultGreenHouseShouldBeFound("nameG.equals=" + DEFAULT_NAME_G);

        // Get all the greenHouseList where nameG equals to UPDATED_NAME_G
        defaultGreenHouseShouldNotBeFound("nameG.equals=" + UPDATED_NAME_G);
    }

    @Test
    @Transactional
    void getAllGreenHousesByNameGIsNotEqualToSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where nameG not equals to DEFAULT_NAME_G
        defaultGreenHouseShouldNotBeFound("nameG.notEquals=" + DEFAULT_NAME_G);

        // Get all the greenHouseList where nameG not equals to UPDATED_NAME_G
        defaultGreenHouseShouldBeFound("nameG.notEquals=" + UPDATED_NAME_G);
    }

    @Test
    @Transactional
    void getAllGreenHousesByNameGIsInShouldWork() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where nameG in DEFAULT_NAME_G or UPDATED_NAME_G
        defaultGreenHouseShouldBeFound("nameG.in=" + DEFAULT_NAME_G + "," + UPDATED_NAME_G);

        // Get all the greenHouseList where nameG equals to UPDATED_NAME_G
        defaultGreenHouseShouldNotBeFound("nameG.in=" + UPDATED_NAME_G);
    }

    @Test
    @Transactional
    void getAllGreenHousesByNameGIsNullOrNotNull() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where nameG is not null
        defaultGreenHouseShouldBeFound("nameG.specified=true");

        // Get all the greenHouseList where nameG is null
        defaultGreenHouseShouldNotBeFound("nameG.specified=false");
    }

    @Test
    @Transactional
    void getAllGreenHousesByNameGContainsSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where nameG contains DEFAULT_NAME_G
        defaultGreenHouseShouldBeFound("nameG.contains=" + DEFAULT_NAME_G);

        // Get all the greenHouseList where nameG contains UPDATED_NAME_G
        defaultGreenHouseShouldNotBeFound("nameG.contains=" + UPDATED_NAME_G);
    }

    @Test
    @Transactional
    void getAllGreenHousesByNameGNotContainsSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where nameG does not contain DEFAULT_NAME_G
        defaultGreenHouseShouldNotBeFound("nameG.doesNotContain=" + DEFAULT_NAME_G);

        // Get all the greenHouseList where nameG does not contain UPDATED_NAME_G
        defaultGreenHouseShouldBeFound("nameG.doesNotContain=" + UPDATED_NAME_G);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where latitude equals to DEFAULT_LATITUDE
        defaultGreenHouseShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the greenHouseList where latitude equals to UPDATED_LATITUDE
        defaultGreenHouseShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where latitude not equals to DEFAULT_LATITUDE
        defaultGreenHouseShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the greenHouseList where latitude not equals to UPDATED_LATITUDE
        defaultGreenHouseShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultGreenHouseShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the greenHouseList where latitude equals to UPDATED_LATITUDE
        defaultGreenHouseShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where latitude is not null
        defaultGreenHouseShouldBeFound("latitude.specified=true");

        // Get all the greenHouseList where latitude is null
        defaultGreenHouseShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllGreenHousesByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultGreenHouseShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the greenHouseList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultGreenHouseShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultGreenHouseShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the greenHouseList where latitude is less than or equal to SMALLER_LATITUDE
        defaultGreenHouseShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where latitude is less than DEFAULT_LATITUDE
        defaultGreenHouseShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the greenHouseList where latitude is less than UPDATED_LATITUDE
        defaultGreenHouseShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where latitude is greater than DEFAULT_LATITUDE
        defaultGreenHouseShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the greenHouseList where latitude is greater than SMALLER_LATITUDE
        defaultGreenHouseShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where longitude equals to DEFAULT_LONGITUDE
        defaultGreenHouseShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the greenHouseList where longitude equals to UPDATED_LONGITUDE
        defaultGreenHouseShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where longitude not equals to DEFAULT_LONGITUDE
        defaultGreenHouseShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the greenHouseList where longitude not equals to UPDATED_LONGITUDE
        defaultGreenHouseShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultGreenHouseShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the greenHouseList where longitude equals to UPDATED_LONGITUDE
        defaultGreenHouseShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where longitude is not null
        defaultGreenHouseShouldBeFound("longitude.specified=true");

        // Get all the greenHouseList where longitude is null
        defaultGreenHouseShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllGreenHousesByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultGreenHouseShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the greenHouseList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultGreenHouseShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultGreenHouseShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the greenHouseList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultGreenHouseShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where longitude is less than DEFAULT_LONGITUDE
        defaultGreenHouseShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the greenHouseList where longitude is less than UPDATED_LONGITUDE
        defaultGreenHouseShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        // Get all the greenHouseList where longitude is greater than DEFAULT_LONGITUDE
        defaultGreenHouseShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the greenHouseList where longitude is greater than SMALLER_LONGITUDE
        defaultGreenHouseShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllGreenHousesByReportIsEqualToSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);
        Report report = ReportResourceIT.createEntity(em);
        em.persist(report);
        em.flush();
        greenHouse.addReport(report);
        greenHouseRepository.saveAndFlush(greenHouse);
        Long reportId = report.getId();

        // Get all the greenHouseList where report equals to reportId
        defaultGreenHouseShouldBeFound("reportId.equals=" + reportId);

        // Get all the greenHouseList where report equals to (reportId + 1)
        defaultGreenHouseShouldNotBeFound("reportId.equals=" + (reportId + 1));
    }

    @Test
    @Transactional
    void getAllGreenHousesByObservateurIsEqualToSomething() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);
        Profile observateur = ProfileResourceIT.createEntity(em);
        em.persist(observateur);
        em.flush();
        greenHouse.setObservateur(observateur);
        greenHouseRepository.saveAndFlush(greenHouse);
        Long observateurId = observateur.getId();

        // Get all the greenHouseList where observateur equals to observateurId
        defaultGreenHouseShouldBeFound("observateurId.equals=" + observateurId);

        // Get all the greenHouseList where observateur equals to (observateurId + 1)
        defaultGreenHouseShouldNotBeFound("observateurId.equals=" + (observateurId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGreenHouseShouldBeFound(String filter) throws Exception {
        restGreenHouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(greenHouse.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameG").value(hasItem(DEFAULT_NAME_G)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())));

        // Check, that the count call also returns 1
        restGreenHouseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGreenHouseShouldNotBeFound(String filter) throws Exception {
        restGreenHouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGreenHouseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGreenHouse() throws Exception {
        // Get the greenHouse
        restGreenHouseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGreenHouse() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        int databaseSizeBeforeUpdate = greenHouseRepository.findAll().size();

        // Update the greenHouse
        GreenHouse updatedGreenHouse = greenHouseRepository.findById(greenHouse.getId()).get();
        // Disconnect from session so that the updates on updatedGreenHouse are not directly saved in db
        em.detach(updatedGreenHouse);
        updatedGreenHouse.nameG(UPDATED_NAME_G).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);
        GreenHouseDTO greenHouseDTO = greenHouseMapper.toDto(updatedGreenHouse);

        restGreenHouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, greenHouseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(greenHouseDTO))
            )
            .andExpect(status().isOk());

        // Validate the GreenHouse in the database
        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeUpdate);
        GreenHouse testGreenHouse = greenHouseList.get(greenHouseList.size() - 1);
        assertThat(testGreenHouse.getNameG()).isEqualTo(UPDATED_NAME_G);
        assertThat(testGreenHouse.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testGreenHouse.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void putNonExistingGreenHouse() throws Exception {
        int databaseSizeBeforeUpdate = greenHouseRepository.findAll().size();
        greenHouse.setId(count.incrementAndGet());

        // Create the GreenHouse
        GreenHouseDTO greenHouseDTO = greenHouseMapper.toDto(greenHouse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGreenHouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, greenHouseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(greenHouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GreenHouse in the database
        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGreenHouse() throws Exception {
        int databaseSizeBeforeUpdate = greenHouseRepository.findAll().size();
        greenHouse.setId(count.incrementAndGet());

        // Create the GreenHouse
        GreenHouseDTO greenHouseDTO = greenHouseMapper.toDto(greenHouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGreenHouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(greenHouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GreenHouse in the database
        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGreenHouse() throws Exception {
        int databaseSizeBeforeUpdate = greenHouseRepository.findAll().size();
        greenHouse.setId(count.incrementAndGet());

        // Create the GreenHouse
        GreenHouseDTO greenHouseDTO = greenHouseMapper.toDto(greenHouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGreenHouseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(greenHouseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GreenHouse in the database
        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGreenHouseWithPatch() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        int databaseSizeBeforeUpdate = greenHouseRepository.findAll().size();

        // Update the greenHouse using partial update
        GreenHouse partialUpdatedGreenHouse = new GreenHouse();
        partialUpdatedGreenHouse.setId(greenHouse.getId());

        partialUpdatedGreenHouse.nameG(UPDATED_NAME_G).longitude(UPDATED_LONGITUDE);

        restGreenHouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGreenHouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGreenHouse))
            )
            .andExpect(status().isOk());

        // Validate the GreenHouse in the database
        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeUpdate);
        GreenHouse testGreenHouse = greenHouseList.get(greenHouseList.size() - 1);
        assertThat(testGreenHouse.getNameG()).isEqualTo(UPDATED_NAME_G);
        assertThat(testGreenHouse.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testGreenHouse.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void fullUpdateGreenHouseWithPatch() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        int databaseSizeBeforeUpdate = greenHouseRepository.findAll().size();

        // Update the greenHouse using partial update
        GreenHouse partialUpdatedGreenHouse = new GreenHouse();
        partialUpdatedGreenHouse.setId(greenHouse.getId());

        partialUpdatedGreenHouse.nameG(UPDATED_NAME_G).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);

        restGreenHouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGreenHouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGreenHouse))
            )
            .andExpect(status().isOk());

        // Validate the GreenHouse in the database
        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeUpdate);
        GreenHouse testGreenHouse = greenHouseList.get(greenHouseList.size() - 1);
        assertThat(testGreenHouse.getNameG()).isEqualTo(UPDATED_NAME_G);
        assertThat(testGreenHouse.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testGreenHouse.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void patchNonExistingGreenHouse() throws Exception {
        int databaseSizeBeforeUpdate = greenHouseRepository.findAll().size();
        greenHouse.setId(count.incrementAndGet());

        // Create the GreenHouse
        GreenHouseDTO greenHouseDTO = greenHouseMapper.toDto(greenHouse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGreenHouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, greenHouseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(greenHouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GreenHouse in the database
        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGreenHouse() throws Exception {
        int databaseSizeBeforeUpdate = greenHouseRepository.findAll().size();
        greenHouse.setId(count.incrementAndGet());

        // Create the GreenHouse
        GreenHouseDTO greenHouseDTO = greenHouseMapper.toDto(greenHouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGreenHouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(greenHouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GreenHouse in the database
        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGreenHouse() throws Exception {
        int databaseSizeBeforeUpdate = greenHouseRepository.findAll().size();
        greenHouse.setId(count.incrementAndGet());

        // Create the GreenHouse
        GreenHouseDTO greenHouseDTO = greenHouseMapper.toDto(greenHouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGreenHouseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(greenHouseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GreenHouse in the database
        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGreenHouse() throws Exception {
        // Initialize the database
        greenHouseRepository.saveAndFlush(greenHouse);

        int databaseSizeBeforeDelete = greenHouseRepository.findAll().size();

        // Delete the greenHouse
        restGreenHouseMockMvc
            .perform(delete(ENTITY_API_URL_ID, greenHouse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GreenHouse> greenHouseList = greenHouseRepository.findAll();
        assertThat(greenHouseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
