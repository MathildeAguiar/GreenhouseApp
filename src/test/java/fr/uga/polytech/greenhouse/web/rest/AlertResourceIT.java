package fr.uga.polytech.greenhouse.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.uga.polytech.greenhouse.IntegrationTest;
import fr.uga.polytech.greenhouse.domain.Alert;
import fr.uga.polytech.greenhouse.domain.enumeration.AlertLevel;
import fr.uga.polytech.greenhouse.repository.AlertRepository;
import fr.uga.polytech.greenhouse.service.criteria.AlertCriteria;
import fr.uga.polytech.greenhouse.service.dto.AlertDTO;
import fr.uga.polytech.greenhouse.service.mapper.AlertMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AlertResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlertResourceIT {

    private static final AlertLevel DEFAULT_LEVEL = AlertLevel.INFO;
    private static final AlertLevel UPDATED_LEVEL = AlertLevel.WARNING;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/alerts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private AlertMapper alertMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlertMockMvc;

    private Alert alert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alert createEntity(EntityManager em) {
        Alert alert = new Alert().level(DEFAULT_LEVEL).createdAt(DEFAULT_CREATED_AT).modifiedAt(DEFAULT_MODIFIED_AT);
        return alert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alert createUpdatedEntity(EntityManager em) {
        Alert alert = new Alert().level(UPDATED_LEVEL).createdAt(UPDATED_CREATED_AT).modifiedAt(UPDATED_MODIFIED_AT);
        return alert;
    }

    @BeforeEach
    public void initTest() {
        alert = createEntity(em);
    }

    @Test
    @Transactional
    void createAlert() throws Exception {
        int databaseSizeBeforeCreate = alertRepository.findAll().size();
        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);
        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isCreated());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeCreate + 1);
        Alert testAlert = alertList.get(alertList.size() - 1);
        assertThat(testAlert.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testAlert.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAlert.getModifiedAt()).isEqualTo(DEFAULT_MODIFIED_AT);
    }

    @Test
    @Transactional
    void createAlertWithExistingId() throws Exception {
        // Create the Alert with an existing ID
        alert.setId(1L);
        AlertDTO alertDTO = alertMapper.toDto(alert);

        int databaseSizeBeforeCreate = alertRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = alertRepository.findAll().size();
        // set the field null
        alert.setLevel(null);

        // Create the Alert, which fails.
        AlertDTO alertDTO = alertMapper.toDto(alert);

        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = alertRepository.findAll().size();
        // set the field null
        alert.setCreatedAt(null);

        // Create the Alert, which fails.
        AlertDTO alertDTO = alertMapper.toDto(alert);

        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = alertRepository.findAll().size();
        // set the field null
        alert.setModifiedAt(null);

        // Create the Alert, which fails.
        AlertDTO alertDTO = alertMapper.toDto(alert);

        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlerts() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alert.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(DEFAULT_MODIFIED_AT.toString())));
    }

    @Test
    @Transactional
    void getAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get the alert
        restAlertMockMvc
            .perform(get(ENTITY_API_URL_ID, alert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alert.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.modifiedAt").value(DEFAULT_MODIFIED_AT.toString()));
    }

    @Test
    @Transactional
    void getAlertsByIdFiltering() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        Long id = alert.getId();

        defaultAlertShouldBeFound("id.equals=" + id);
        defaultAlertShouldNotBeFound("id.notEquals=" + id);

        defaultAlertShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAlertShouldNotBeFound("id.greaterThan=" + id);

        defaultAlertShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAlertShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlertsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where level equals to DEFAULT_LEVEL
        defaultAlertShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the alertList where level equals to UPDATED_LEVEL
        defaultAlertShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAlertsByLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where level not equals to DEFAULT_LEVEL
        defaultAlertShouldNotBeFound("level.notEquals=" + DEFAULT_LEVEL);

        // Get all the alertList where level not equals to UPDATED_LEVEL
        defaultAlertShouldBeFound("level.notEquals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAlertsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultAlertShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the alertList where level equals to UPDATED_LEVEL
        defaultAlertShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllAlertsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where level is not null
        defaultAlertShouldBeFound("level.specified=true");

        // Get all the alertList where level is null
        defaultAlertShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where createdAt equals to DEFAULT_CREATED_AT
        defaultAlertShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the alertList where createdAt equals to UPDATED_CREATED_AT
        defaultAlertShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAlertsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where createdAt not equals to DEFAULT_CREATED_AT
        defaultAlertShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the alertList where createdAt not equals to UPDATED_CREATED_AT
        defaultAlertShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAlertsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultAlertShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the alertList where createdAt equals to UPDATED_CREATED_AT
        defaultAlertShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAlertsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where createdAt is not null
        defaultAlertShouldBeFound("createdAt.specified=true");

        // Get all the alertList where createdAt is null
        defaultAlertShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByModifiedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where modifiedAt equals to DEFAULT_MODIFIED_AT
        defaultAlertShouldBeFound("modifiedAt.equals=" + DEFAULT_MODIFIED_AT);

        // Get all the alertList where modifiedAt equals to UPDATED_MODIFIED_AT
        defaultAlertShouldNotBeFound("modifiedAt.equals=" + UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllAlertsByModifiedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where modifiedAt not equals to DEFAULT_MODIFIED_AT
        defaultAlertShouldNotBeFound("modifiedAt.notEquals=" + DEFAULT_MODIFIED_AT);

        // Get all the alertList where modifiedAt not equals to UPDATED_MODIFIED_AT
        defaultAlertShouldBeFound("modifiedAt.notEquals=" + UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllAlertsByModifiedAtIsInShouldWork() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where modifiedAt in DEFAULT_MODIFIED_AT or UPDATED_MODIFIED_AT
        defaultAlertShouldBeFound("modifiedAt.in=" + DEFAULT_MODIFIED_AT + "," + UPDATED_MODIFIED_AT);

        // Get all the alertList where modifiedAt equals to UPDATED_MODIFIED_AT
        defaultAlertShouldNotBeFound("modifiedAt.in=" + UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllAlertsByModifiedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        // Get all the alertList where modifiedAt is not null
        defaultAlertShouldBeFound("modifiedAt.specified=true");

        // Get all the alertList where modifiedAt is null
        defaultAlertShouldNotBeFound("modifiedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlertShouldBeFound(String filter) throws Exception {
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alert.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(DEFAULT_MODIFIED_AT.toString())));

        // Check, that the count call also returns 1
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlertShouldNotBeFound(String filter) throws Exception {
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlert() throws Exception {
        // Get the alert
        restAlertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        int databaseSizeBeforeUpdate = alertRepository.findAll().size();

        // Update the alert
        Alert updatedAlert = alertRepository.findById(alert.getId()).get();
        // Disconnect from session so that the updates on updatedAlert are not directly saved in db
        em.detach(updatedAlert);
        updatedAlert.level(UPDATED_LEVEL).createdAt(UPDATED_CREATED_AT).modifiedAt(UPDATED_MODIFIED_AT);
        AlertDTO alertDTO = alertMapper.toDto(updatedAlert);

        restAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alertDTO))
            )
            .andExpect(status().isOk());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
        Alert testAlert = alertList.get(alertList.size() - 1);
        assertThat(testAlert.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testAlert.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAlert.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void putNonExistingAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();
        alert.setId(count.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();
        alert.setId(count.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();
        alert.setId(count.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlertWithPatch() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        int databaseSizeBeforeUpdate = alertRepository.findAll().size();

        // Update the alert using partial update
        Alert partialUpdatedAlert = new Alert();
        partialUpdatedAlert.setId(alert.getId());

        partialUpdatedAlert.modifiedAt(UPDATED_MODIFIED_AT);

        restAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlert))
            )
            .andExpect(status().isOk());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
        Alert testAlert = alertList.get(alertList.size() - 1);
        assertThat(testAlert.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testAlert.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAlert.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void fullUpdateAlertWithPatch() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        int databaseSizeBeforeUpdate = alertRepository.findAll().size();

        // Update the alert using partial update
        Alert partialUpdatedAlert = new Alert();
        partialUpdatedAlert.setId(alert.getId());

        partialUpdatedAlert.level(UPDATED_LEVEL).createdAt(UPDATED_CREATED_AT).modifiedAt(UPDATED_MODIFIED_AT);

        restAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlert))
            )
            .andExpect(status().isOk());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
        Alert testAlert = alertList.get(alertList.size() - 1);
        assertThat(testAlert.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testAlert.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAlert.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();
        alert.setId(count.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alertDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();
        alert.setId(count.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlert() throws Exception {
        int databaseSizeBeforeUpdate = alertRepository.findAll().size();
        alert.setId(count.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(alertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alert in the database
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlert() throws Exception {
        // Initialize the database
        alertRepository.saveAndFlush(alert);

        int databaseSizeBeforeDelete = alertRepository.findAll().size();

        // Delete the alert
        restAlertMockMvc
            .perform(delete(ENTITY_API_URL_ID, alert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alert> alertList = alertRepository.findAll();
        assertThat(alertList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
