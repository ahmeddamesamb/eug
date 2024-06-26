package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
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
import sn.ugb.gir.domain.AnneeAcademique;
import sn.ugb.gir.repository.AnneeAcademiqueRepository;
import sn.ugb.gir.repository.search.AnneeAcademiqueSearchRepository;
import sn.ugb.gir.service.dto.AnneeAcademiqueDTO;
import sn.ugb.gir.service.mapper.AnneeAcademiqueMapper;

/**
 * Integration tests for the {@link AnneeAcademiqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnneeAcademiqueResourceIT {

    private static final String DEFAULT_LIBELLE_ANNEE_ACADEMIQUE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_ANNEE_ACADEMIQUE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANNEE_AC = 1990;
    private static final Integer UPDATED_ANNEE_AC = 1991;

    private static final String DEFAULT_SEPARATEUR = "A";
    private static final String UPDATED_SEPARATEUR = "B";

    private static final Boolean DEFAULT_ANNEE_COURANTE_YN = false;
    private static final Boolean UPDATED_ANNEE_COURANTE_YN = true;

    private static final String ENTITY_API_URL = "/api/annee-academiques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/annee-academiques/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnneeAcademiqueRepository anneeAcademiqueRepository;

    @Autowired
    private AnneeAcademiqueMapper anneeAcademiqueMapper;

    @Autowired
    private AnneeAcademiqueSearchRepository anneeAcademiqueSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnneeAcademiqueMockMvc;

    private AnneeAcademique anneeAcademique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnneeAcademique createEntity(EntityManager em) {
        AnneeAcademique anneeAcademique = new AnneeAcademique()
            .libelleAnneeAcademique(DEFAULT_LIBELLE_ANNEE_ACADEMIQUE)
            .anneeAc(DEFAULT_ANNEE_AC)
            .separateur(DEFAULT_SEPARATEUR)
            .anneeCouranteYN(DEFAULT_ANNEE_COURANTE_YN);
        return anneeAcademique;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnneeAcademique createUpdatedEntity(EntityManager em) {
        AnneeAcademique anneeAcademique = new AnneeAcademique()
            .libelleAnneeAcademique(UPDATED_LIBELLE_ANNEE_ACADEMIQUE)
            .anneeAc(UPDATED_ANNEE_AC)
            .separateur(UPDATED_SEPARATEUR)
            .anneeCouranteYN(UPDATED_ANNEE_COURANTE_YN);
        return anneeAcademique;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        anneeAcademiqueSearchRepository.deleteAll();
        assertThat(anneeAcademiqueSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        anneeAcademique = createEntity(em);
    }

    @Test
    @Transactional
    void createAnneeAcademique() throws Exception {
        int databaseSizeBeforeCreate = anneeAcademiqueRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        // Create the AnneeAcademique
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(anneeAcademique);
        restAnneeAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademiqueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        AnneeAcademique testAnneeAcademique = anneeAcademiqueList.get(anneeAcademiqueList.size() - 1);
        assertThat(testAnneeAcademique.getLibelleAnneeAcademique()).isEqualTo(DEFAULT_LIBELLE_ANNEE_ACADEMIQUE);
        assertThat(testAnneeAcademique.getAnneeAc()).isEqualTo(DEFAULT_ANNEE_AC);
        assertThat(testAnneeAcademique.getSeparateur()).isEqualTo(DEFAULT_SEPARATEUR);
        assertThat(testAnneeAcademique.getAnneeCouranteYN()).isEqualTo(DEFAULT_ANNEE_COURANTE_YN);
    }

    @Test
    @Transactional
    void createAnneeAcademiqueWithExistingId() throws Exception {
        // Create the AnneeAcademique with an existing ID
        anneeAcademique.setId(1L);
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(anneeAcademique);

        int databaseSizeBeforeCreate = anneeAcademiqueRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnneeAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLibelleAnneeAcademiqueIsRequired() throws Exception {
        int databaseSizeBeforeTest = anneeAcademiqueRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        // set the field null
        anneeAcademique.setLibelleAnneeAcademique(null);

        // Create the AnneeAcademique, which fails.
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(anneeAcademique);

        restAnneeAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAnneeAcIsRequired() throws Exception {
        int databaseSizeBeforeTest = anneeAcademiqueRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        // set the field null
        anneeAcademique.setAnneeAc(null);

        // Create the AnneeAcademique, which fails.
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(anneeAcademique);

        restAnneeAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSeparateurIsRequired() throws Exception {
        int databaseSizeBeforeTest = anneeAcademiqueRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        // set the field null
        anneeAcademique.setSeparateur(null);

        // Create the AnneeAcademique, which fails.
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(anneeAcademique);

        restAnneeAcademiqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAnneeAcademiques() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        // Get all the anneeAcademiqueList
        restAnneeAcademiqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anneeAcademique.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleAnneeAcademique").value(hasItem(DEFAULT_LIBELLE_ANNEE_ACADEMIQUE)))
            .andExpect(jsonPath("$.[*].anneeAc").value(hasItem(DEFAULT_ANNEE_AC)))
            .andExpect(jsonPath("$.[*].separateur").value(hasItem(DEFAULT_SEPARATEUR)))
            .andExpect(jsonPath("$.[*].anneeCouranteYN").value(hasItem(DEFAULT_ANNEE_COURANTE_YN.booleanValue())));
    }

    @Test
    @Transactional
    void getAnneeAcademique() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        // Get the anneeAcademique
        restAnneeAcademiqueMockMvc
            .perform(get(ENTITY_API_URL_ID, anneeAcademique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(anneeAcademique.getId().intValue()))
            .andExpect(jsonPath("$.libelleAnneeAcademique").value(DEFAULT_LIBELLE_ANNEE_ACADEMIQUE))
            .andExpect(jsonPath("$.anneeAc").value(DEFAULT_ANNEE_AC))
            .andExpect(jsonPath("$.separateur").value(DEFAULT_SEPARATEUR))
            .andExpect(jsonPath("$.anneeCouranteYN").value(DEFAULT_ANNEE_COURANTE_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAnneeAcademique() throws Exception {
        // Get the anneeAcademique
        restAnneeAcademiqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnneeAcademique() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        anneeAcademiqueSearchRepository.save(anneeAcademique);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());

        // Update the anneeAcademique
        AnneeAcademique updatedAnneeAcademique = anneeAcademiqueRepository.findById(anneeAcademique.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAnneeAcademique are not directly saved in db
        em.detach(updatedAnneeAcademique);
        updatedAnneeAcademique
            .libelleAnneeAcademique(UPDATED_LIBELLE_ANNEE_ACADEMIQUE)
            .anneeAc(UPDATED_ANNEE_AC)
            .separateur(UPDATED_SEPARATEUR)
            .anneeCouranteYN(UPDATED_ANNEE_COURANTE_YN);
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(updatedAnneeAcademique);

        restAnneeAcademiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, anneeAcademiqueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademiqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
        AnneeAcademique testAnneeAcademique = anneeAcademiqueList.get(anneeAcademiqueList.size() - 1);
        assertThat(testAnneeAcademique.getLibelleAnneeAcademique()).isEqualTo(UPDATED_LIBELLE_ANNEE_ACADEMIQUE);
        assertThat(testAnneeAcademique.getAnneeAc()).isEqualTo(UPDATED_ANNEE_AC);
        assertThat(testAnneeAcademique.getSeparateur()).isEqualTo(UPDATED_SEPARATEUR);
        assertThat(testAnneeAcademique.getAnneeCouranteYN()).isEqualTo(UPDATED_ANNEE_COURANTE_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AnneeAcademique> anneeAcademiqueSearchList = IterableUtils.toList(anneeAcademiqueSearchRepository.findAll());
                AnneeAcademique testAnneeAcademiqueSearch = anneeAcademiqueSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testAnneeAcademiqueSearch.getLibelleAnneeAcademique()).isEqualTo(UPDATED_LIBELLE_ANNEE_ACADEMIQUE);
                assertThat(testAnneeAcademiqueSearch.getAnneeAc()).isEqualTo(UPDATED_ANNEE_AC);
                assertThat(testAnneeAcademiqueSearch.getSeparateur()).isEqualTo(UPDATED_SEPARATEUR);
                assertThat(testAnneeAcademiqueSearch.getAnneeCouranteYN()).isEqualTo(UPDATED_ANNEE_COURANTE_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        anneeAcademique.setId(longCount.incrementAndGet());

        // Create the AnneeAcademique
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(anneeAcademique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnneeAcademiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, anneeAcademiqueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        anneeAcademique.setId(longCount.incrementAndGet());

        // Create the AnneeAcademique
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(anneeAcademique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeAcademiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        anneeAcademique.setId(longCount.incrementAndGet());

        // Create the AnneeAcademique
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(anneeAcademique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeAcademiqueMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademiqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAnneeAcademiqueWithPatch() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();

        // Update the anneeAcademique using partial update
        AnneeAcademique partialUpdatedAnneeAcademique = new AnneeAcademique();
        partialUpdatedAnneeAcademique.setId(anneeAcademique.getId());

        partialUpdatedAnneeAcademique.libelleAnneeAcademique(UPDATED_LIBELLE_ANNEE_ACADEMIQUE).separateur(UPDATED_SEPARATEUR);

        restAnneeAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnneeAcademique.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnneeAcademique))
            )
            .andExpect(status().isOk());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
        AnneeAcademique testAnneeAcademique = anneeAcademiqueList.get(anneeAcademiqueList.size() - 1);
        assertThat(testAnneeAcademique.getLibelleAnneeAcademique()).isEqualTo(UPDATED_LIBELLE_ANNEE_ACADEMIQUE);
        assertThat(testAnneeAcademique.getAnneeAc()).isEqualTo(DEFAULT_ANNEE_AC);
        assertThat(testAnneeAcademique.getSeparateur()).isEqualTo(UPDATED_SEPARATEUR);
        assertThat(testAnneeAcademique.getAnneeCouranteYN()).isEqualTo(DEFAULT_ANNEE_COURANTE_YN);
    }

    @Test
    @Transactional
    void fullUpdateAnneeAcademiqueWithPatch() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();

        // Update the anneeAcademique using partial update
        AnneeAcademique partialUpdatedAnneeAcademique = new AnneeAcademique();
        partialUpdatedAnneeAcademique.setId(anneeAcademique.getId());

        partialUpdatedAnneeAcademique
            .libelleAnneeAcademique(UPDATED_LIBELLE_ANNEE_ACADEMIQUE)
            .anneeAc(UPDATED_ANNEE_AC)
            .separateur(UPDATED_SEPARATEUR)
            .anneeCouranteYN(UPDATED_ANNEE_COURANTE_YN);

        restAnneeAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnneeAcademique.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnneeAcademique))
            )
            .andExpect(status().isOk());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
        AnneeAcademique testAnneeAcademique = anneeAcademiqueList.get(anneeAcademiqueList.size() - 1);
        assertThat(testAnneeAcademique.getLibelleAnneeAcademique()).isEqualTo(UPDATED_LIBELLE_ANNEE_ACADEMIQUE);
        assertThat(testAnneeAcademique.getAnneeAc()).isEqualTo(UPDATED_ANNEE_AC);
        assertThat(testAnneeAcademique.getSeparateur()).isEqualTo(UPDATED_SEPARATEUR);
        assertThat(testAnneeAcademique.getAnneeCouranteYN()).isEqualTo(UPDATED_ANNEE_COURANTE_YN);
    }

    @Test
    @Transactional
    void patchNonExistingAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        anneeAcademique.setId(longCount.incrementAndGet());

        // Create the AnneeAcademique
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(anneeAcademique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnneeAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, anneeAcademiqueDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        anneeAcademique.setId(longCount.incrementAndGet());

        // Create the AnneeAcademique
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(anneeAcademique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        anneeAcademique.setId(longCount.incrementAndGet());

        // Create the AnneeAcademique
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(anneeAcademique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeAcademiqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anneeAcademiqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnneeAcademique in the database
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAnneeAcademique() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);
        anneeAcademiqueRepository.save(anneeAcademique);
        anneeAcademiqueSearchRepository.save(anneeAcademique);

        int databaseSizeBeforeDelete = anneeAcademiqueRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the anneeAcademique
        restAnneeAcademiqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, anneeAcademique.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(anneeAcademiqueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAnneeAcademique() throws Exception {
        // Initialize the database
        anneeAcademique = anneeAcademiqueRepository.saveAndFlush(anneeAcademique);
        anneeAcademiqueSearchRepository.save(anneeAcademique);

        // Search the anneeAcademique
        restAnneeAcademiqueMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + anneeAcademique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anneeAcademique.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleAnneeAcademique").value(hasItem(DEFAULT_LIBELLE_ANNEE_ACADEMIQUE)))
            .andExpect(jsonPath("$.[*].anneeAc").value(hasItem(DEFAULT_ANNEE_AC)))
            .andExpect(jsonPath("$.[*].separateur").value(hasItem(DEFAULT_SEPARATEUR)))
            .andExpect(jsonPath("$.[*].anneeCouranteYN").value(hasItem(DEFAULT_ANNEE_COURANTE_YN.booleanValue())));
    }
}
