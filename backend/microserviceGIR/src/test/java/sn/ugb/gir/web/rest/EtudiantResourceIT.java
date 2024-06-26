package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.IntegrationTest;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.repository.EtudiantRepository;
import sn.ugb.gir.repository.search.EtudiantSearchRepository;
import sn.ugb.gir.service.dto.EtudiantDTO;
import sn.ugb.gir.service.mapper.EtudiantMapper;

/**
 * Integration tests for the {@link EtudiantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EtudiantResourceIT {

    private static final String DEFAULT_CODE_ETU = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ETU = "BBBBBBBBBB";

    private static final String DEFAULT_INE = "AAAAAAAAAA";
    private static final String UPDATED_INE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_BU = "AAAAAAAAAA";
    private static final String UPDATED_CODE_BU = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_UGB = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_UGB = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISS_ETU = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISS_ETU = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIEU_NAISS_ETU = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAISS_ETU = "BBBBBBBBBB";

    private static final String DEFAULT_SEXE = "AAAAAAAAAA";
    private static final String UPDATED_SEXE = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_DOC_IDENTITE = "AAAAAAAAAA";
    private static final String UPDATED_NUM_DOC_IDENTITE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ASSIMILE_YN = false;
    private static final Boolean UPDATED_ASSIMILE_YN = true;

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/etudiants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/etudiants/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EtudiantMapper etudiantMapper;

    @Autowired
    private EtudiantSearchRepository etudiantSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtudiantMockMvc;

    private Etudiant etudiant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createEntity(EntityManager em) {
        Etudiant etudiant = new Etudiant()
            .codeEtu(DEFAULT_CODE_ETU)
            .ine(DEFAULT_INE)
            .codeBU(DEFAULT_CODE_BU)
            .emailUGB(DEFAULT_EMAIL_UGB)
            .dateNaissEtu(DEFAULT_DATE_NAISS_ETU)
            .lieuNaissEtu(DEFAULT_LIEU_NAISS_ETU)
            .sexe(DEFAULT_SEXE)
            .numDocIdentite(DEFAULT_NUM_DOC_IDENTITE)
            .assimileYN(DEFAULT_ASSIMILE_YN)
            .actifYN(DEFAULT_ACTIF_YN);
        return etudiant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createUpdatedEntity(EntityManager em) {
        Etudiant etudiant = new Etudiant()
            .codeEtu(UPDATED_CODE_ETU)
            .ine(UPDATED_INE)
            .codeBU(UPDATED_CODE_BU)
            .emailUGB(UPDATED_EMAIL_UGB)
            .dateNaissEtu(UPDATED_DATE_NAISS_ETU)
            .lieuNaissEtu(UPDATED_LIEU_NAISS_ETU)
            .sexe(UPDATED_SEXE)
            .numDocIdentite(UPDATED_NUM_DOC_IDENTITE)
            .assimileYN(UPDATED_ASSIMILE_YN)
            .actifYN(UPDATED_ACTIF_YN);
        return etudiant;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        etudiantSearchRepository.deleteAll();
        assertThat(etudiantSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        etudiant = createEntity(em);
    }

    @Test
    @Transactional
    void createEtudiant() throws Exception {
        int databaseSizeBeforeCreate = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);
        restEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Etudiant testEtudiant = etudiantList.get(etudiantList.size() - 1);
        assertThat(testEtudiant.getCodeEtu()).isEqualTo(DEFAULT_CODE_ETU);
        assertThat(testEtudiant.getIne()).isEqualTo(DEFAULT_INE);
        assertThat(testEtudiant.getCodeBU()).isEqualTo(DEFAULT_CODE_BU);
        assertThat(testEtudiant.getEmailUGB()).isEqualTo(DEFAULT_EMAIL_UGB);
        assertThat(testEtudiant.getDateNaissEtu()).isEqualTo(DEFAULT_DATE_NAISS_ETU);
        assertThat(testEtudiant.getLieuNaissEtu()).isEqualTo(DEFAULT_LIEU_NAISS_ETU);
        assertThat(testEtudiant.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testEtudiant.getNumDocIdentite()).isEqualTo(DEFAULT_NUM_DOC_IDENTITE);
        assertThat(testEtudiant.getAssimileYN()).isEqualTo(DEFAULT_ASSIMILE_YN);
        assertThat(testEtudiant.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createEtudiantWithExistingId() throws Exception {
        // Create the Etudiant with an existing ID
        etudiant.setId(1L);
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        int databaseSizeBeforeCreate = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCodeEtuIsRequired() throws Exception {
        int databaseSizeBeforeTest = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        // set the field null
        etudiant.setCodeEtu(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDateNaissEtuIsRequired() throws Exception {
        int databaseSizeBeforeTest = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        // set the field null
        etudiant.setDateNaissEtu(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLieuNaissEtuIsRequired() throws Exception {
        int databaseSizeBeforeTest = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        // set the field null
        etudiant.setLieuNaissEtu(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        // set the field null
        etudiant.setSexe(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAssimileYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        // set the field null
        etudiant.setAssimileYN(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEtudiants() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeEtu").value(hasItem(DEFAULT_CODE_ETU)))
            .andExpect(jsonPath("$.[*].ine").value(hasItem(DEFAULT_INE)))
            .andExpect(jsonPath("$.[*].codeBU").value(hasItem(DEFAULT_CODE_BU)))
            .andExpect(jsonPath("$.[*].emailUGB").value(hasItem(DEFAULT_EMAIL_UGB)))
            .andExpect(jsonPath("$.[*].dateNaissEtu").value(hasItem(DEFAULT_DATE_NAISS_ETU.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissEtu").value(hasItem(DEFAULT_LIEU_NAISS_ETU)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE)))
            .andExpect(jsonPath("$.[*].numDocIdentite").value(hasItem(DEFAULT_NUM_DOC_IDENTITE)))
            .andExpect(jsonPath("$.[*].assimileYN").value(hasItem(DEFAULT_ASSIMILE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @Test
    @Transactional
    void getEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get the etudiant
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL_ID, etudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etudiant.getId().intValue()))
            .andExpect(jsonPath("$.codeEtu").value(DEFAULT_CODE_ETU))
            .andExpect(jsonPath("$.ine").value(DEFAULT_INE))
            .andExpect(jsonPath("$.codeBU").value(DEFAULT_CODE_BU))
            .andExpect(jsonPath("$.emailUGB").value(DEFAULT_EMAIL_UGB))
            .andExpect(jsonPath("$.dateNaissEtu").value(DEFAULT_DATE_NAISS_ETU.toString()))
            .andExpect(jsonPath("$.lieuNaissEtu").value(DEFAULT_LIEU_NAISS_ETU))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE))
            .andExpect(jsonPath("$.numDocIdentite").value(DEFAULT_NUM_DOC_IDENTITE))
            .andExpect(jsonPath("$.assimileYN").value(DEFAULT_ASSIMILE_YN.booleanValue()))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEtudiant() throws Exception {
        // Get the etudiant
        restEtudiantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        etudiantSearchRepository.save(etudiant);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());

        // Update the etudiant
        Etudiant updatedEtudiant = etudiantRepository.findById(etudiant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEtudiant are not directly saved in db
        em.detach(updatedEtudiant);
        updatedEtudiant
            .codeEtu(UPDATED_CODE_ETU)
            .ine(UPDATED_INE)
            .codeBU(UPDATED_CODE_BU)
            .emailUGB(UPDATED_EMAIL_UGB)
            .dateNaissEtu(UPDATED_DATE_NAISS_ETU)
            .lieuNaissEtu(UPDATED_LIEU_NAISS_ETU)
            .sexe(UPDATED_SEXE)
            .numDocIdentite(UPDATED_NUM_DOC_IDENTITE)
            .assimileYN(UPDATED_ASSIMILE_YN)
            .actifYN(UPDATED_ACTIF_YN);
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(updatedEtudiant);

        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etudiantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        Etudiant testEtudiant = etudiantList.get(etudiantList.size() - 1);
        assertThat(testEtudiant.getCodeEtu()).isEqualTo(UPDATED_CODE_ETU);
        assertThat(testEtudiant.getIne()).isEqualTo(UPDATED_INE);
        assertThat(testEtudiant.getCodeBU()).isEqualTo(UPDATED_CODE_BU);
        assertThat(testEtudiant.getEmailUGB()).isEqualTo(UPDATED_EMAIL_UGB);
        assertThat(testEtudiant.getDateNaissEtu()).isEqualTo(UPDATED_DATE_NAISS_ETU);
        assertThat(testEtudiant.getLieuNaissEtu()).isEqualTo(UPDATED_LIEU_NAISS_ETU);
        assertThat(testEtudiant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEtudiant.getNumDocIdentite()).isEqualTo(UPDATED_NUM_DOC_IDENTITE);
        assertThat(testEtudiant.getAssimileYN()).isEqualTo(UPDATED_ASSIMILE_YN);
        assertThat(testEtudiant.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Etudiant> etudiantSearchList = IterableUtils.toList(etudiantSearchRepository.findAll());
                Etudiant testEtudiantSearch = etudiantSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEtudiantSearch.getCodeEtu()).isEqualTo(UPDATED_CODE_ETU);
                assertThat(testEtudiantSearch.getIne()).isEqualTo(UPDATED_INE);
                assertThat(testEtudiantSearch.getCodeBU()).isEqualTo(UPDATED_CODE_BU);
                assertThat(testEtudiantSearch.getEmailUGB()).isEqualTo(UPDATED_EMAIL_UGB);
                assertThat(testEtudiantSearch.getDateNaissEtu()).isEqualTo(UPDATED_DATE_NAISS_ETU);
                assertThat(testEtudiantSearch.getLieuNaissEtu()).isEqualTo(UPDATED_LIEU_NAISS_ETU);
                assertThat(testEtudiantSearch.getSexe()).isEqualTo(UPDATED_SEXE);
                assertThat(testEtudiantSearch.getNumDocIdentite()).isEqualTo(UPDATED_NUM_DOC_IDENTITE);
                assertThat(testEtudiantSearch.getAssimileYN()).isEqualTo(UPDATED_ASSIMILE_YN);
                assertThat(testEtudiantSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etudiantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEtudiantWithPatch() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();

        // Update the etudiant using partial update
        Etudiant partialUpdatedEtudiant = new Etudiant();
        partialUpdatedEtudiant.setId(etudiant.getId());

        partialUpdatedEtudiant.codeBU(UPDATED_CODE_BU);

        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        Etudiant testEtudiant = etudiantList.get(etudiantList.size() - 1);
        assertThat(testEtudiant.getCodeEtu()).isEqualTo(DEFAULT_CODE_ETU);
        assertThat(testEtudiant.getIne()).isEqualTo(DEFAULT_INE);
        assertThat(testEtudiant.getCodeBU()).isEqualTo(UPDATED_CODE_BU);
        assertThat(testEtudiant.getEmailUGB()).isEqualTo(DEFAULT_EMAIL_UGB);
        assertThat(testEtudiant.getDateNaissEtu()).isEqualTo(DEFAULT_DATE_NAISS_ETU);
        assertThat(testEtudiant.getLieuNaissEtu()).isEqualTo(DEFAULT_LIEU_NAISS_ETU);
        assertThat(testEtudiant.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testEtudiant.getNumDocIdentite()).isEqualTo(DEFAULT_NUM_DOC_IDENTITE);
        assertThat(testEtudiant.getAssimileYN()).isEqualTo(DEFAULT_ASSIMILE_YN);
        assertThat(testEtudiant.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateEtudiantWithPatch() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();

        // Update the etudiant using partial update
        Etudiant partialUpdatedEtudiant = new Etudiant();
        partialUpdatedEtudiant.setId(etudiant.getId());

        partialUpdatedEtudiant
            .codeEtu(UPDATED_CODE_ETU)
            .ine(UPDATED_INE)
            .codeBU(UPDATED_CODE_BU)
            .emailUGB(UPDATED_EMAIL_UGB)
            .dateNaissEtu(UPDATED_DATE_NAISS_ETU)
            .lieuNaissEtu(UPDATED_LIEU_NAISS_ETU)
            .sexe(UPDATED_SEXE)
            .numDocIdentite(UPDATED_NUM_DOC_IDENTITE)
            .assimileYN(UPDATED_ASSIMILE_YN)
            .actifYN(UPDATED_ACTIF_YN);

        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        Etudiant testEtudiant = etudiantList.get(etudiantList.size() - 1);
        assertThat(testEtudiant.getCodeEtu()).isEqualTo(UPDATED_CODE_ETU);
        assertThat(testEtudiant.getIne()).isEqualTo(UPDATED_INE);
        assertThat(testEtudiant.getCodeBU()).isEqualTo(UPDATED_CODE_BU);
        assertThat(testEtudiant.getEmailUGB()).isEqualTo(UPDATED_EMAIL_UGB);
        assertThat(testEtudiant.getDateNaissEtu()).isEqualTo(UPDATED_DATE_NAISS_ETU);
        assertThat(testEtudiant.getLieuNaissEtu()).isEqualTo(UPDATED_LIEU_NAISS_ETU);
        assertThat(testEtudiant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEtudiant.getNumDocIdentite()).isEqualTo(UPDATED_NUM_DOC_IDENTITE);
        assertThat(testEtudiant.getAssimileYN()).isEqualTo(UPDATED_ASSIMILE_YN);
        assertThat(testEtudiant.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etudiantDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etudiantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);
        etudiantRepository.save(etudiant);
        etudiantSearchRepository.save(etudiant);

        int databaseSizeBeforeDelete = etudiantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the etudiant
        restEtudiantMockMvc
            .perform(delete(ENTITY_API_URL_ID, etudiant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etudiantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEtudiant() throws Exception {
        // Initialize the database
        etudiant = etudiantRepository.saveAndFlush(etudiant);
        etudiantSearchRepository.save(etudiant);

        // Search the etudiant
        restEtudiantMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + etudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeEtu").value(hasItem(DEFAULT_CODE_ETU)))
            .andExpect(jsonPath("$.[*].ine").value(hasItem(DEFAULT_INE)))
            .andExpect(jsonPath("$.[*].codeBU").value(hasItem(DEFAULT_CODE_BU)))
            .andExpect(jsonPath("$.[*].emailUGB").value(hasItem(DEFAULT_EMAIL_UGB)))
            .andExpect(jsonPath("$.[*].dateNaissEtu").value(hasItem(DEFAULT_DATE_NAISS_ETU.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissEtu").value(hasItem(DEFAULT_LIEU_NAISS_ETU)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE)))
            .andExpect(jsonPath("$.[*].numDocIdentite").value(hasItem(DEFAULT_NUM_DOC_IDENTITE)))
            .andExpect(jsonPath("$.[*].assimileYN").value(hasItem(DEFAULT_ASSIMILE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
