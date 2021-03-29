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
import fr.uga.polytech.greenhouse.domain.User;
import fr.uga.polytech.greenhouse.domain.enumeration.Category;
import fr.uga.polytech.greenhouse.domain.enumeration.Filiere;
import fr.uga.polytech.greenhouse.repository.ProfileRepository;
import fr.uga.polytech.greenhouse.service.criteria.ProfileCriteria;
import fr.uga.polytech.greenhouse.service.dto.ProfileDTO;
import fr.uga.polytech.greenhouse.service.mapper.ProfileMapper;
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
 * Integration tests for the {@link ProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Category DEFAULT_STATUS = Category.ENSEIGNANT;
    private static final Category UPDATED_STATUS = Category.ETUDIANT;

    private static final Filiere DEFAULT_SPECIALITE = Filiere.INFO;
    private static final Filiere UPDATED_SPECIALITE = Filiere.PRI;

    private static final String DEFAULT_ADDRESS = "Qnntez2";
    private static final String UPDATED_ADDRESS = "Bjddgu9";

    private static final String DEFAULT_CODE_P = "AAAAAAA";
    private static final String UPDATED_CODE_P = "BBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "45MPj@univ-grenoble-alpessssss.fr";
    private static final String UPDATED_EMAIL = "Iy@univ-grenoble-alpessssss.fr";

    private static final byte[] DEFAULT_INDICATIF = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_INDICATIF = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_INDICATIF_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_INDICATIF_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfileMockMvc;

    private Profile profile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS)
            .specialite(DEFAULT_SPECIALITE)
            .address(DEFAULT_ADDRESS)
            .codeP(DEFAULT_CODE_P)
            .ville(DEFAULT_VILLE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .indicatif(DEFAULT_INDICATIF)
            .indicatifContentType(DEFAULT_INDICATIF_CONTENT_TYPE);
        return profile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createUpdatedEntity(EntityManager em) {
        Profile profile = new Profile()
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .specialite(UPDATED_SPECIALITE)
            .address(UPDATED_ADDRESS)
            .codeP(UPDATED_CODE_P)
            .ville(UPDATED_VILLE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .indicatif(UPDATED_INDICATIF)
            .indicatifContentType(UPDATED_INDICATIF_CONTENT_TYPE);
        return profile;
    }

    @BeforeEach
    public void initTest() {
        profile = createEntity(em);
    }

    @Test
    @Transactional
    void createProfile() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();
        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        restProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isCreated());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate + 1);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProfile.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProfile.getSpecialite()).isEqualTo(DEFAULT_SPECIALITE);
        assertThat(testProfile.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testProfile.getCodeP()).isEqualTo(DEFAULT_CODE_P);
        assertThat(testProfile.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testProfile.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testProfile.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfile.getIndicatif()).isEqualTo(DEFAULT_INDICATIF);
        assertThat(testProfile.getIndicatifContentType()).isEqualTo(DEFAULT_INDICATIF_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createProfileWithExistingId() throws Exception {
        // Create the Profile with an existing ID
        profile.setId(1L);
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setName(null);

        // Create the Profile, which fails.
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        restProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setStatus(null);

        // Create the Profile, which fails.
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        restProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList
        restProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].specialite").value(hasItem(DEFAULT_SPECIALITE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].codeP").value(hasItem(DEFAULT_CODE_P)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].indicatifContentType").value(hasItem(DEFAULT_INDICATIF_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].indicatif").value(hasItem(Base64Utils.encodeToString(DEFAULT_INDICATIF))));
    }

    @Test
    @Transactional
    void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.specialite").value(DEFAULT_SPECIALITE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.codeP").value(DEFAULT_CODE_P))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.indicatifContentType").value(DEFAULT_INDICATIF_CONTENT_TYPE))
            .andExpect(jsonPath("$.indicatif").value(Base64Utils.encodeToString(DEFAULT_INDICATIF)));
    }

    @Test
    @Transactional
    void getProfilesByIdFiltering() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        Long id = profile.getId();

        defaultProfileShouldBeFound("id.equals=" + id);
        defaultProfileShouldNotBeFound("id.notEquals=" + id);

        defaultProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProfileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProfilesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where name equals to DEFAULT_NAME
        defaultProfileShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the profileList where name equals to UPDATED_NAME
        defaultProfileShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProfilesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where name not equals to DEFAULT_NAME
        defaultProfileShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the profileList where name not equals to UPDATED_NAME
        defaultProfileShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProfilesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProfileShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the profileList where name equals to UPDATED_NAME
        defaultProfileShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProfilesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where name is not null
        defaultProfileShouldBeFound("name.specified=true");

        // Get all the profileList where name is null
        defaultProfileShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProfilesByNameContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where name contains DEFAULT_NAME
        defaultProfileShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the profileList where name contains UPDATED_NAME
        defaultProfileShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProfilesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where name does not contain DEFAULT_NAME
        defaultProfileShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the profileList where name does not contain UPDATED_NAME
        defaultProfileShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProfilesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where status equals to DEFAULT_STATUS
        defaultProfileShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the profileList where status equals to UPDATED_STATUS
        defaultProfileShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProfilesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where status not equals to DEFAULT_STATUS
        defaultProfileShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the profileList where status not equals to UPDATED_STATUS
        defaultProfileShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProfilesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultProfileShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the profileList where status equals to UPDATED_STATUS
        defaultProfileShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProfilesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where status is not null
        defaultProfileShouldBeFound("status.specified=true");

        // Get all the profileList where status is null
        defaultProfileShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllProfilesBySpecialiteIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where specialite equals to DEFAULT_SPECIALITE
        defaultProfileShouldBeFound("specialite.equals=" + DEFAULT_SPECIALITE);

        // Get all the profileList where specialite equals to UPDATED_SPECIALITE
        defaultProfileShouldNotBeFound("specialite.equals=" + UPDATED_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllProfilesBySpecialiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where specialite not equals to DEFAULT_SPECIALITE
        defaultProfileShouldNotBeFound("specialite.notEquals=" + DEFAULT_SPECIALITE);

        // Get all the profileList where specialite not equals to UPDATED_SPECIALITE
        defaultProfileShouldBeFound("specialite.notEquals=" + UPDATED_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllProfilesBySpecialiteIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where specialite in DEFAULT_SPECIALITE or UPDATED_SPECIALITE
        defaultProfileShouldBeFound("specialite.in=" + DEFAULT_SPECIALITE + "," + UPDATED_SPECIALITE);

        // Get all the profileList where specialite equals to UPDATED_SPECIALITE
        defaultProfileShouldNotBeFound("specialite.in=" + UPDATED_SPECIALITE);
    }

    @Test
    @Transactional
    void getAllProfilesBySpecialiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where specialite is not null
        defaultProfileShouldBeFound("specialite.specified=true");

        // Get all the profileList where specialite is null
        defaultProfileShouldNotBeFound("specialite.specified=false");
    }

    @Test
    @Transactional
    void getAllProfilesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where address equals to DEFAULT_ADDRESS
        defaultProfileShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the profileList where address equals to UPDATED_ADDRESS
        defaultProfileShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProfilesByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where address not equals to DEFAULT_ADDRESS
        defaultProfileShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the profileList where address not equals to UPDATED_ADDRESS
        defaultProfileShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProfilesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultProfileShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the profileList where address equals to UPDATED_ADDRESS
        defaultProfileShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProfilesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where address is not null
        defaultProfileShouldBeFound("address.specified=true");

        // Get all the profileList where address is null
        defaultProfileShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllProfilesByAddressContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where address contains DEFAULT_ADDRESS
        defaultProfileShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the profileList where address contains UPDATED_ADDRESS
        defaultProfileShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProfilesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where address does not contain DEFAULT_ADDRESS
        defaultProfileShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the profileList where address does not contain UPDATED_ADDRESS
        defaultProfileShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProfilesByCodePIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where codeP equals to DEFAULT_CODE_P
        defaultProfileShouldBeFound("codeP.equals=" + DEFAULT_CODE_P);

        // Get all the profileList where codeP equals to UPDATED_CODE_P
        defaultProfileShouldNotBeFound("codeP.equals=" + UPDATED_CODE_P);
    }

    @Test
    @Transactional
    void getAllProfilesByCodePIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where codeP not equals to DEFAULT_CODE_P
        defaultProfileShouldNotBeFound("codeP.notEquals=" + DEFAULT_CODE_P);

        // Get all the profileList where codeP not equals to UPDATED_CODE_P
        defaultProfileShouldBeFound("codeP.notEquals=" + UPDATED_CODE_P);
    }

    @Test
    @Transactional
    void getAllProfilesByCodePIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where codeP in DEFAULT_CODE_P or UPDATED_CODE_P
        defaultProfileShouldBeFound("codeP.in=" + DEFAULT_CODE_P + "," + UPDATED_CODE_P);

        // Get all the profileList where codeP equals to UPDATED_CODE_P
        defaultProfileShouldNotBeFound("codeP.in=" + UPDATED_CODE_P);
    }

    @Test
    @Transactional
    void getAllProfilesByCodePIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where codeP is not null
        defaultProfileShouldBeFound("codeP.specified=true");

        // Get all the profileList where codeP is null
        defaultProfileShouldNotBeFound("codeP.specified=false");
    }

    @Test
    @Transactional
    void getAllProfilesByCodePContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where codeP contains DEFAULT_CODE_P
        defaultProfileShouldBeFound("codeP.contains=" + DEFAULT_CODE_P);

        // Get all the profileList where codeP contains UPDATED_CODE_P
        defaultProfileShouldNotBeFound("codeP.contains=" + UPDATED_CODE_P);
    }

    @Test
    @Transactional
    void getAllProfilesByCodePNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where codeP does not contain DEFAULT_CODE_P
        defaultProfileShouldNotBeFound("codeP.doesNotContain=" + DEFAULT_CODE_P);

        // Get all the profileList where codeP does not contain UPDATED_CODE_P
        defaultProfileShouldBeFound("codeP.doesNotContain=" + UPDATED_CODE_P);
    }

    @Test
    @Transactional
    void getAllProfilesByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ville equals to DEFAULT_VILLE
        defaultProfileShouldBeFound("ville.equals=" + DEFAULT_VILLE);

        // Get all the profileList where ville equals to UPDATED_VILLE
        defaultProfileShouldNotBeFound("ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllProfilesByVilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ville not equals to DEFAULT_VILLE
        defaultProfileShouldNotBeFound("ville.notEquals=" + DEFAULT_VILLE);

        // Get all the profileList where ville not equals to UPDATED_VILLE
        defaultProfileShouldBeFound("ville.notEquals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllProfilesByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ville in DEFAULT_VILLE or UPDATED_VILLE
        defaultProfileShouldBeFound("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE);

        // Get all the profileList where ville equals to UPDATED_VILLE
        defaultProfileShouldNotBeFound("ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllProfilesByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ville is not null
        defaultProfileShouldBeFound("ville.specified=true");

        // Get all the profileList where ville is null
        defaultProfileShouldNotBeFound("ville.specified=false");
    }

    @Test
    @Transactional
    void getAllProfilesByVilleContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ville contains DEFAULT_VILLE
        defaultProfileShouldBeFound("ville.contains=" + DEFAULT_VILLE);

        // Get all the profileList where ville contains UPDATED_VILLE
        defaultProfileShouldNotBeFound("ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllProfilesByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ville does not contain DEFAULT_VILLE
        defaultProfileShouldNotBeFound("ville.doesNotContain=" + DEFAULT_VILLE);

        // Get all the profileList where ville does not contain UPDATED_VILLE
        defaultProfileShouldBeFound("ville.doesNotContain=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllProfilesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultProfileShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the profileList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultProfileShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllProfilesByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultProfileShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the profileList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultProfileShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllProfilesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultProfileShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the profileList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultProfileShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllProfilesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phoneNumber is not null
        defaultProfileShouldBeFound("phoneNumber.specified=true");

        // Get all the profileList where phoneNumber is null
        defaultProfileShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllProfilesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultProfileShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the profileList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultProfileShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllProfilesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultProfileShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the profileList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultProfileShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllProfilesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where email equals to DEFAULT_EMAIL
        defaultProfileShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the profileList where email equals to UPDATED_EMAIL
        defaultProfileShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProfilesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where email not equals to DEFAULT_EMAIL
        defaultProfileShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the profileList where email not equals to UPDATED_EMAIL
        defaultProfileShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProfilesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultProfileShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the profileList where email equals to UPDATED_EMAIL
        defaultProfileShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProfilesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where email is not null
        defaultProfileShouldBeFound("email.specified=true");

        // Get all the profileList where email is null
        defaultProfileShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllProfilesByEmailContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where email contains DEFAULT_EMAIL
        defaultProfileShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the profileList where email contains UPDATED_EMAIL
        defaultProfileShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProfilesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where email does not contain DEFAULT_EMAIL
        defaultProfileShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the profileList where email does not contain UPDATED_EMAIL
        defaultProfileShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllProfilesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        profile.setUser(user);
        profileRepository.saveAndFlush(profile);
        Long userId = user.getId();

        // Get all the profileList where user equals to userId
        defaultProfileShouldBeFound("userId.equals=" + userId);

        // Get all the profileList where user equals to (userId + 1)
        defaultProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllProfilesByWorkIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        Task work = TaskResourceIT.createEntity(em);
        em.persist(work);
        em.flush();
        profile.addWork(work);
        profileRepository.saveAndFlush(profile);
        Long workId = work.getId();

        // Get all the profileList where work equals to workId
        defaultProfileShouldBeFound("workId.equals=" + workId);

        // Get all the profileList where work equals to (workId + 1)
        defaultProfileShouldNotBeFound("workId.equals=" + (workId + 1));
    }

    @Test
    @Transactional
    void getAllProfilesByDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        Report document = ReportResourceIT.createEntity(em);
        em.persist(document);
        em.flush();
        profile.addDocument(document);
        profileRepository.saveAndFlush(profile);
        Long documentId = document.getId();

        // Get all the profileList where document equals to documentId
        defaultProfileShouldBeFound("documentId.equals=" + documentId);

        // Get all the profileList where document equals to (documentId + 1)
        defaultProfileShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }

    @Test
    @Transactional
    void getAllProfilesByHouseIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        GreenHouse house = GreenHouseResourceIT.createEntity(em);
        em.persist(house);
        em.flush();
        profile.addHouse(house);
        profileRepository.saveAndFlush(profile);
        Long houseId = house.getId();

        // Get all the profileList where house equals to houseId
        defaultProfileShouldBeFound("houseId.equals=" + houseId);

        // Get all the profileList where house equals to (houseId + 1)
        defaultProfileShouldNotBeFound("houseId.equals=" + (houseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProfileShouldBeFound(String filter) throws Exception {
        restProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].specialite").value(hasItem(DEFAULT_SPECIALITE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].codeP").value(hasItem(DEFAULT_CODE_P)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].indicatifContentType").value(hasItem(DEFAULT_INDICATIF_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].indicatif").value(hasItem(Base64Utils.encodeToString(DEFAULT_INDICATIF))));

        // Check, that the count call also returns 1
        restProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProfileShouldNotBeFound(String filter) throws Exception {
        restProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).get();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);
        updatedProfile
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .specialite(UPDATED_SPECIALITE)
            .address(UPDATED_ADDRESS)
            .codeP(UPDATED_CODE_P)
            .ville(UPDATED_VILLE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .indicatif(UPDATED_INDICATIF)
            .indicatifContentType(UPDATED_INDICATIF_CONTENT_TYPE);
        ProfileDTO profileDTO = profileMapper.toDto(updatedProfile);

        restProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(profileDTO))
            )
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfile.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProfile.getSpecialite()).isEqualTo(UPDATED_SPECIALITE);
        assertThat(testProfile.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProfile.getCodeP()).isEqualTo(UPDATED_CODE_P);
        assertThat(testProfile.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testProfile.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testProfile.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfile.getIndicatif()).isEqualTo(UPDATED_INDICATIF);
        assertThat(testProfile.getIndicatifContentType()).isEqualTo(UPDATED_INDICATIF_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();
        profile.setId(count.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(profileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();
        profile.setId(count.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(profileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();
        profile.setId(count.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfileWithPatch() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile using partial update
        Profile partialUpdatedProfile = new Profile();
        partialUpdatedProfile.setId(profile.getId());

        partialUpdatedProfile.name(UPDATED_NAME).address(UPDATED_ADDRESS).codeP(UPDATED_CODE_P).ville(UPDATED_VILLE);

        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfile))
            )
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfile.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProfile.getSpecialite()).isEqualTo(DEFAULT_SPECIALITE);
        assertThat(testProfile.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProfile.getCodeP()).isEqualTo(UPDATED_CODE_P);
        assertThat(testProfile.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testProfile.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testProfile.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfile.getIndicatif()).isEqualTo(DEFAULT_INDICATIF);
        assertThat(testProfile.getIndicatifContentType()).isEqualTo(DEFAULT_INDICATIF_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateProfileWithPatch() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile using partial update
        Profile partialUpdatedProfile = new Profile();
        partialUpdatedProfile.setId(profile.getId());

        partialUpdatedProfile
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .specialite(UPDATED_SPECIALITE)
            .address(UPDATED_ADDRESS)
            .codeP(UPDATED_CODE_P)
            .ville(UPDATED_VILLE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .indicatif(UPDATED_INDICATIF)
            .indicatifContentType(UPDATED_INDICATIF_CONTENT_TYPE);

        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfile))
            )
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfile.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProfile.getSpecialite()).isEqualTo(UPDATED_SPECIALITE);
        assertThat(testProfile.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProfile.getCodeP()).isEqualTo(UPDATED_CODE_P);
        assertThat(testProfile.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testProfile.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testProfile.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfile.getIndicatif()).isEqualTo(UPDATED_INDICATIF);
        assertThat(testProfile.getIndicatifContentType()).isEqualTo(UPDATED_INDICATIF_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();
        profile.setId(count.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(profileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();
        profile.setId(count.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(profileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();
        profile.setId(count.incrementAndGet());

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(profileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeDelete = profileRepository.findAll().size();

        // Delete the profile
        restProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, profile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
