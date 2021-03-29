package fr.uga.polytech.greenhouse.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.uga.polytech.greenhouse.IntegrationTest;
import fr.uga.polytech.greenhouse.domain.GreenHouse;
import fr.uga.polytech.greenhouse.domain.Profile;
import fr.uga.polytech.greenhouse.domain.Report;
import fr.uga.polytech.greenhouse.domain.Task;
import fr.uga.polytech.greenhouse.domain.enumeration.Language;
import fr.uga.polytech.greenhouse.repository.ReportRepository;
import fr.uga.polytech.greenhouse.service.criteria.ReportCriteria;
import fr.uga.polytech.greenhouse.service.dto.ReportDTO;
import fr.uga.polytech.greenhouse.service.mapper.ReportMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportResourceIT {

    private static final String DEFAULT_TITLE_R = "AAAAAAAAAA";
    private static final String UPDATED_TITLE_R = "BBBBBBBBBB";

    private static final String DEFAULT_ALERTS = "AAAAAAAAAA";
    private static final String UPDATED_ALERTS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPT = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Language DEFAULT_LANGUE = Language.FRENCH;
    private static final Language UPDATED_LANGUE = Language.ENGLISH;

    private static final String ENTITY_API_URL = "/api/reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportMockMvc;

    private Report report;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createEntity(EntityManager em) {
        Report report = new Report()
            .titleR(DEFAULT_TITLE_R)
            .alerts(DEFAULT_ALERTS)
            .descript(DEFAULT_DESCRIPT)
            .createdAt(DEFAULT_CREATED_AT)
            .modifiedAt(DEFAULT_MODIFIED_AT)
            .langue(DEFAULT_LANGUE);
        return report;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createUpdatedEntity(EntityManager em) {
        Report report = new Report()
            .titleR(UPDATED_TITLE_R)
            .alerts(UPDATED_ALERTS)
            .descript(UPDATED_DESCRIPT)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .langue(UPDATED_LANGUE);
        return report;
    }

    @BeforeEach
    public void initTest() {
        report = createEntity(em);
    }

    @Test
    @Transactional
    void createReport() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();
        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);
        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isCreated());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate + 1);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getTitleR()).isEqualTo(DEFAULT_TITLE_R);
        assertThat(testReport.getAlerts()).isEqualTo(DEFAULT_ALERTS);
        assertThat(testReport.getDescript()).isEqualTo(DEFAULT_DESCRIPT);
        assertThat(testReport.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReport.getModifiedAt()).isEqualTo(DEFAULT_MODIFIED_AT);
        assertThat(testReport.getLangue()).isEqualTo(DEFAULT_LANGUE);
    }

    @Test
    @Transactional
    void createReportWithExistingId() throws Exception {
        // Create the Report with an existing ID
        report.setId(1L);
        ReportDTO reportDTO = reportMapper.toDto(report);

        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setCreatedAt(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setModifiedAt(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReports() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].titleR").value(hasItem(DEFAULT_TITLE_R)))
            .andExpect(jsonPath("$.[*].alerts").value(hasItem(DEFAULT_ALERTS)))
            .andExpect(jsonPath("$.[*].descript").value(hasItem(DEFAULT_DESCRIPT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(DEFAULT_MODIFIED_AT.toString())))
            .andExpect(jsonPath("$.[*].langue").value(hasItem(DEFAULT_LANGUE.toString())));
    }

    @Test
    @Transactional
    void getReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get the report
        restReportMockMvc
            .perform(get(ENTITY_API_URL_ID, report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(report.getId().intValue()))
            .andExpect(jsonPath("$.titleR").value(DEFAULT_TITLE_R))
            .andExpect(jsonPath("$.alerts").value(DEFAULT_ALERTS))
            .andExpect(jsonPath("$.descript").value(DEFAULT_DESCRIPT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.modifiedAt").value(DEFAULT_MODIFIED_AT.toString()))
            .andExpect(jsonPath("$.langue").value(DEFAULT_LANGUE.toString()));
    }

    @Test
    @Transactional
    void getReportsByIdFiltering() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        Long id = report.getId();

        defaultReportShouldBeFound("id.equals=" + id);
        defaultReportShouldNotBeFound("id.notEquals=" + id);

        defaultReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReportShouldNotBeFound("id.greaterThan=" + id);

        defaultReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportsByTitleRIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where titleR equals to DEFAULT_TITLE_R
        defaultReportShouldBeFound("titleR.equals=" + DEFAULT_TITLE_R);

        // Get all the reportList where titleR equals to UPDATED_TITLE_R
        defaultReportShouldNotBeFound("titleR.equals=" + UPDATED_TITLE_R);
    }

    @Test
    @Transactional
    void getAllReportsByTitleRIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where titleR not equals to DEFAULT_TITLE_R
        defaultReportShouldNotBeFound("titleR.notEquals=" + DEFAULT_TITLE_R);

        // Get all the reportList where titleR not equals to UPDATED_TITLE_R
        defaultReportShouldBeFound("titleR.notEquals=" + UPDATED_TITLE_R);
    }

    @Test
    @Transactional
    void getAllReportsByTitleRIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where titleR in DEFAULT_TITLE_R or UPDATED_TITLE_R
        defaultReportShouldBeFound("titleR.in=" + DEFAULT_TITLE_R + "," + UPDATED_TITLE_R);

        // Get all the reportList where titleR equals to UPDATED_TITLE_R
        defaultReportShouldNotBeFound("titleR.in=" + UPDATED_TITLE_R);
    }

    @Test
    @Transactional
    void getAllReportsByTitleRIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where titleR is not null
        defaultReportShouldBeFound("titleR.specified=true");

        // Get all the reportList where titleR is null
        defaultReportShouldNotBeFound("titleR.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByTitleRContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where titleR contains DEFAULT_TITLE_R
        defaultReportShouldBeFound("titleR.contains=" + DEFAULT_TITLE_R);

        // Get all the reportList where titleR contains UPDATED_TITLE_R
        defaultReportShouldNotBeFound("titleR.contains=" + UPDATED_TITLE_R);
    }

    @Test
    @Transactional
    void getAllReportsByTitleRNotContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where titleR does not contain DEFAULT_TITLE_R
        defaultReportShouldNotBeFound("titleR.doesNotContain=" + DEFAULT_TITLE_R);

        // Get all the reportList where titleR does not contain UPDATED_TITLE_R
        defaultReportShouldBeFound("titleR.doesNotContain=" + UPDATED_TITLE_R);
    }

    @Test
    @Transactional
    void getAllReportsByAlertsIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where alerts equals to DEFAULT_ALERTS
        defaultReportShouldBeFound("alerts.equals=" + DEFAULT_ALERTS);

        // Get all the reportList where alerts equals to UPDATED_ALERTS
        defaultReportShouldNotBeFound("alerts.equals=" + UPDATED_ALERTS);
    }

    @Test
    @Transactional
    void getAllReportsByAlertsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where alerts not equals to DEFAULT_ALERTS
        defaultReportShouldNotBeFound("alerts.notEquals=" + DEFAULT_ALERTS);

        // Get all the reportList where alerts not equals to UPDATED_ALERTS
        defaultReportShouldBeFound("alerts.notEquals=" + UPDATED_ALERTS);
    }

    @Test
    @Transactional
    void getAllReportsByAlertsIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where alerts in DEFAULT_ALERTS or UPDATED_ALERTS
        defaultReportShouldBeFound("alerts.in=" + DEFAULT_ALERTS + "," + UPDATED_ALERTS);

        // Get all the reportList where alerts equals to UPDATED_ALERTS
        defaultReportShouldNotBeFound("alerts.in=" + UPDATED_ALERTS);
    }

    @Test
    @Transactional
    void getAllReportsByAlertsIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where alerts is not null
        defaultReportShouldBeFound("alerts.specified=true");

        // Get all the reportList where alerts is null
        defaultReportShouldNotBeFound("alerts.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByAlertsContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where alerts contains DEFAULT_ALERTS
        defaultReportShouldBeFound("alerts.contains=" + DEFAULT_ALERTS);

        // Get all the reportList where alerts contains UPDATED_ALERTS
        defaultReportShouldNotBeFound("alerts.contains=" + UPDATED_ALERTS);
    }

    @Test
    @Transactional
    void getAllReportsByAlertsNotContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where alerts does not contain DEFAULT_ALERTS
        defaultReportShouldNotBeFound("alerts.doesNotContain=" + DEFAULT_ALERTS);

        // Get all the reportList where alerts does not contain UPDATED_ALERTS
        defaultReportShouldBeFound("alerts.doesNotContain=" + UPDATED_ALERTS);
    }

    @Test
    @Transactional
    void getAllReportsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where createdAt equals to DEFAULT_CREATED_AT
        defaultReportShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the reportList where createdAt equals to UPDATED_CREATED_AT
        defaultReportShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllReportsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where createdAt not equals to DEFAULT_CREATED_AT
        defaultReportShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the reportList where createdAt not equals to UPDATED_CREATED_AT
        defaultReportShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllReportsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultReportShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the reportList where createdAt equals to UPDATED_CREATED_AT
        defaultReportShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllReportsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where createdAt is not null
        defaultReportShouldBeFound("createdAt.specified=true");

        // Get all the reportList where createdAt is null
        defaultReportShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByModifiedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where modifiedAt equals to DEFAULT_MODIFIED_AT
        defaultReportShouldBeFound("modifiedAt.equals=" + DEFAULT_MODIFIED_AT);

        // Get all the reportList where modifiedAt equals to UPDATED_MODIFIED_AT
        defaultReportShouldNotBeFound("modifiedAt.equals=" + UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllReportsByModifiedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where modifiedAt not equals to DEFAULT_MODIFIED_AT
        defaultReportShouldNotBeFound("modifiedAt.notEquals=" + DEFAULT_MODIFIED_AT);

        // Get all the reportList where modifiedAt not equals to UPDATED_MODIFIED_AT
        defaultReportShouldBeFound("modifiedAt.notEquals=" + UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllReportsByModifiedAtIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where modifiedAt in DEFAULT_MODIFIED_AT or UPDATED_MODIFIED_AT
        defaultReportShouldBeFound("modifiedAt.in=" + DEFAULT_MODIFIED_AT + "," + UPDATED_MODIFIED_AT);

        // Get all the reportList where modifiedAt equals to UPDATED_MODIFIED_AT
        defaultReportShouldNotBeFound("modifiedAt.in=" + UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllReportsByModifiedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where modifiedAt is not null
        defaultReportShouldBeFound("modifiedAt.specified=true");

        // Get all the reportList where modifiedAt is null
        defaultReportShouldNotBeFound("modifiedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByLangueIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where langue equals to DEFAULT_LANGUE
        defaultReportShouldBeFound("langue.equals=" + DEFAULT_LANGUE);

        // Get all the reportList where langue equals to UPDATED_LANGUE
        defaultReportShouldNotBeFound("langue.equals=" + UPDATED_LANGUE);
    }

    @Test
    @Transactional
    void getAllReportsByLangueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where langue not equals to DEFAULT_LANGUE
        defaultReportShouldNotBeFound("langue.notEquals=" + DEFAULT_LANGUE);

        // Get all the reportList where langue not equals to UPDATED_LANGUE
        defaultReportShouldBeFound("langue.notEquals=" + UPDATED_LANGUE);
    }

    @Test
    @Transactional
    void getAllReportsByLangueIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where langue in DEFAULT_LANGUE or UPDATED_LANGUE
        defaultReportShouldBeFound("langue.in=" + DEFAULT_LANGUE + "," + UPDATED_LANGUE);

        // Get all the reportList where langue equals to UPDATED_LANGUE
        defaultReportShouldNotBeFound("langue.in=" + UPDATED_LANGUE);
    }

    @Test
    @Transactional
    void getAllReportsByLangueIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where langue is not null
        defaultReportShouldBeFound("langue.specified=true");

        // Get all the reportList where langue is null
        defaultReportShouldNotBeFound("langue.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);
        Task task = TaskResourceIT.createEntity(em);
        em.persist(task);
        em.flush();
        report.addTask(task);
        reportRepository.saveAndFlush(report);
        Long taskId = task.getId();

        // Get all the reportList where task equals to taskId
        defaultReportShouldBeFound("taskId.equals=" + taskId);

        // Get all the reportList where task equals to (taskId + 1)
        defaultReportShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    @Test
    @Transactional
    void getAllReportsByAuthorIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);
        Profile author = ProfileResourceIT.createEntity(em);
        em.persist(author);
        em.flush();
        report.setAuthor(author);
        reportRepository.saveAndFlush(report);
        Long authorId = author.getId();

        // Get all the reportList where author equals to authorId
        defaultReportShouldBeFound("authorId.equals=" + authorId);

        // Get all the reportList where author equals to (authorId + 1)
        defaultReportShouldNotBeFound("authorId.equals=" + (authorId + 1));
    }

    @Test
    @Transactional
    void getAllReportsByHouseIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);
        GreenHouse house = GreenHouseResourceIT.createEntity(em);
        em.persist(house);
        em.flush();
        report.setHouse(house);
        reportRepository.saveAndFlush(report);
        Long houseId = house.getId();

        // Get all the reportList where house equals to houseId
        defaultReportShouldBeFound("houseId.equals=" + houseId);

        // Get all the reportList where house equals to (houseId + 1)
        defaultReportShouldNotBeFound("houseId.equals=" + (houseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportShouldBeFound(String filter) throws Exception {
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].titleR").value(hasItem(DEFAULT_TITLE_R)))
            .andExpect(jsonPath("$.[*].alerts").value(hasItem(DEFAULT_ALERTS)))
            .andExpect(jsonPath("$.[*].descript").value(hasItem(DEFAULT_DESCRIPT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(DEFAULT_MODIFIED_AT.toString())))
            .andExpect(jsonPath("$.[*].langue").value(hasItem(DEFAULT_LANGUE.toString())));

        // Check, that the count call also returns 1
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportShouldNotBeFound(String filter) throws Exception {
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReport() throws Exception {
        // Get the report
        restReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report
        Report updatedReport = reportRepository.findById(report.getId()).get();
        // Disconnect from session so that the updates on updatedReport are not directly saved in db
        em.detach(updatedReport);
        updatedReport
            .titleR(UPDATED_TITLE_R)
            .alerts(UPDATED_ALERTS)
            .descript(UPDATED_DESCRIPT)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .langue(UPDATED_LANGUE);
        ReportDTO reportDTO = reportMapper.toDto(updatedReport);

        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getTitleR()).isEqualTo(UPDATED_TITLE_R);
        assertThat(testReport.getAlerts()).isEqualTo(UPDATED_ALERTS);
        assertThat(testReport.getDescript()).isEqualTo(UPDATED_DESCRIPT);
        assertThat(testReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReport.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
        assertThat(testReport.getLangue()).isEqualTo(UPDATED_LANGUE);
    }

    @Test
    @Transactional
    void putNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport.titleR(UPDATED_TITLE_R).createdAt(UPDATED_CREATED_AT);

        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReport))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getTitleR()).isEqualTo(UPDATED_TITLE_R);
        assertThat(testReport.getAlerts()).isEqualTo(DEFAULT_ALERTS);
        assertThat(testReport.getDescript()).isEqualTo(DEFAULT_DESCRIPT);
        assertThat(testReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReport.getModifiedAt()).isEqualTo(DEFAULT_MODIFIED_AT);
        assertThat(testReport.getLangue()).isEqualTo(DEFAULT_LANGUE);
    }

    @Test
    @Transactional
    void fullUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport
            .titleR(UPDATED_TITLE_R)
            .alerts(UPDATED_ALERTS)
            .descript(UPDATED_DESCRIPT)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .langue(UPDATED_LANGUE);

        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReport))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getTitleR()).isEqualTo(UPDATED_TITLE_R);
        assertThat(testReport.getAlerts()).isEqualTo(UPDATED_ALERTS);
        assertThat(testReport.getDescript()).isEqualTo(UPDATED_DESCRIPT);
        assertThat(testReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReport.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
        assertThat(testReport.getLangue()).isEqualTo(UPDATED_LANGUE);
    }

    @Test
    @Transactional
    void patchNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeDelete = reportRepository.findAll().size();

        // Delete the report
        restReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, report.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
