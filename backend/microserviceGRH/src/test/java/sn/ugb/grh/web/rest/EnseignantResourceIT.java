package sn.ugb.grh.web.rest;

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
import sn.ugb.grh.IntegrationTest;
import sn.ugb.grh.domain.Enseignant;
import sn.ugb.grh.repository.EnseignantRepository;
import sn.ugb.grh.repository.search.EnseignantSearchRepository;
import sn.ugb.grh.service.dto.EnseignantDTO;
import sn.ugb.grh.service.mapper.EnseignantMapper;

/**
 * Integration tests for the {@link EnseignantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnseignantResourceIT {

    private static final String DEFAULT_TITRE_CO_ENCADREUR = "AAAAAAAAAA";
    private static final String UPDATED_TITRE_CO_ENCADREUR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final Long DEFAULT_TITRES_ID = 1L;
    private static final Long UPDATED_TITRES_ID = 2L;

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_POSTE = 1;
    private static final Integer UPDATED_NUMERO_POSTE = 2;

    private static final String DEFAULT_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enseignants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/enseignants/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private EnseignantMapper enseignantMapper;

    @Autowired
    private EnseignantSearchRepository enseignantSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnseignantMockMvc;

    private Enseignant enseignant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignant createEntity(EntityManager em) {
        Enseignant enseignant = new Enseignant()
            .titreCoEncadreur(DEFAULT_TITRE_CO_ENCADREUR)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .titresId(DEFAULT_TITRES_ID)
            .adresse(DEFAULT_ADRESSE)
            .numeroPoste(DEFAULT_NUMERO_POSTE)
            .photo(DEFAULT_PHOTO);
        return enseignant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignant createUpdatedEntity(EntityManager em) {
        Enseignant enseignant = new Enseignant()
            .titreCoEncadreur(UPDATED_TITRE_CO_ENCADREUR)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .titresId(UPDATED_TITRES_ID)
            .adresse(UPDATED_ADRESSE)
            .numeroPoste(UPDATED_NUMERO_POSTE)
            .photo(UPDATED_PHOTO);
        return enseignant;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        enseignantSearchRepository.deleteAll();
        assertThat(enseignantSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        enseignant = createEntity(em);
    }

    @Test
    @Transactional
    void createEnseignant() throws Exception {
        int databaseSizeBeforeCreate = enseignantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);
        restEnseignantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignantDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Enseignant testEnseignant = enseignantList.get(enseignantList.size() - 1);
        assertThat(testEnseignant.getTitreCoEncadreur()).isEqualTo(DEFAULT_TITRE_CO_ENCADREUR);
        assertThat(testEnseignant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEnseignant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEnseignant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEnseignant.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testEnseignant.getTitresId()).isEqualTo(DEFAULT_TITRES_ID);
        assertThat(testEnseignant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testEnseignant.getNumeroPoste()).isEqualTo(DEFAULT_NUMERO_POSTE);
        assertThat(testEnseignant.getPhoto()).isEqualTo(DEFAULT_PHOTO);
    }

    @Test
    @Transactional
    void createEnseignantWithExistingId() throws Exception {
        // Create the Enseignant with an existing ID
        enseignant.setId(1L);
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        int databaseSizeBeforeCreate = enseignantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enseignantSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnseignantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        // set the field null
        enseignant.setNom(null);

        // Create the Enseignant, which fails.
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        restEnseignantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        // set the field null
        enseignant.setPrenom(null);

        // Create the Enseignant, which fails.
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        restEnseignantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEnseignants() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].titreCoEncadreur").value(hasItem(DEFAULT_TITRE_CO_ENCADREUR)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].titresId").value(hasItem(DEFAULT_TITRES_ID.intValue())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].numeroPoste").value(hasItem(DEFAULT_NUMERO_POSTE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO)));
    }

    @Test
    @Transactional
    void getEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get the enseignant
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL_ID, enseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enseignant.getId().intValue()))
            .andExpect(jsonPath("$.titreCoEncadreur").value(DEFAULT_TITRE_CO_ENCADREUR))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.titresId").value(DEFAULT_TITRES_ID.intValue()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.numeroPoste").value(DEFAULT_NUMERO_POSTE))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO));
    }

    @Test
    @Transactional
    void getNonExistingEnseignant() throws Exception {
        // Get the enseignant
        restEnseignantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        enseignantSearchRepository.save(enseignant);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enseignantSearchRepository.findAll());

        // Update the enseignant
        Enseignant updatedEnseignant = enseignantRepository.findById(enseignant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnseignant are not directly saved in db
        em.detach(updatedEnseignant);
        updatedEnseignant
            .titreCoEncadreur(UPDATED_TITRE_CO_ENCADREUR)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .titresId(UPDATED_TITRES_ID)
            .adresse(UPDATED_ADRESSE)
            .numeroPoste(UPDATED_NUMERO_POSTE)
            .photo(UPDATED_PHOTO);
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(updatedEnseignant);

        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enseignantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        Enseignant testEnseignant = enseignantList.get(enseignantList.size() - 1);
        assertThat(testEnseignant.getTitreCoEncadreur()).isEqualTo(UPDATED_TITRE_CO_ENCADREUR);
        assertThat(testEnseignant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEnseignant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnseignant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEnseignant.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testEnseignant.getTitresId()).isEqualTo(UPDATED_TITRES_ID);
        assertThat(testEnseignant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testEnseignant.getNumeroPoste()).isEqualTo(UPDATED_NUMERO_POSTE);
        assertThat(testEnseignant.getPhoto()).isEqualTo(UPDATED_PHOTO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Enseignant> enseignantSearchList = IterableUtils.toList(enseignantSearchRepository.findAll());
                Enseignant testEnseignantSearch = enseignantSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEnseignantSearch.getTitreCoEncadreur()).isEqualTo(UPDATED_TITRE_CO_ENCADREUR);
                assertThat(testEnseignantSearch.getNom()).isEqualTo(UPDATED_NOM);
                assertThat(testEnseignantSearch.getPrenom()).isEqualTo(UPDATED_PRENOM);
                assertThat(testEnseignantSearch.getEmail()).isEqualTo(UPDATED_EMAIL);
                assertThat(testEnseignantSearch.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
                assertThat(testEnseignantSearch.getTitresId()).isEqualTo(UPDATED_TITRES_ID);
                assertThat(testEnseignantSearch.getAdresse()).isEqualTo(UPDATED_ADRESSE);
                assertThat(testEnseignantSearch.getNumeroPoste()).isEqualTo(UPDATED_NUMERO_POSTE);
                assertThat(testEnseignantSearch.getPhoto()).isEqualTo(UPDATED_PHOTO);
            });
    }

    @Test
    @Transactional
    void putNonExistingEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enseignantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEnseignantWithPatch() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();

        // Update the enseignant using partial update
        Enseignant partialUpdatedEnseignant = new Enseignant();
        partialUpdatedEnseignant.setId(enseignant.getId());

        partialUpdatedEnseignant
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .numeroPoste(UPDATED_NUMERO_POSTE)
            .photo(UPDATED_PHOTO);

        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnseignant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnseignant))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        Enseignant testEnseignant = enseignantList.get(enseignantList.size() - 1);
        assertThat(testEnseignant.getTitreCoEncadreur()).isEqualTo(DEFAULT_TITRE_CO_ENCADREUR);
        assertThat(testEnseignant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEnseignant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnseignant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEnseignant.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testEnseignant.getTitresId()).isEqualTo(DEFAULT_TITRES_ID);
        assertThat(testEnseignant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testEnseignant.getNumeroPoste()).isEqualTo(UPDATED_NUMERO_POSTE);
        assertThat(testEnseignant.getPhoto()).isEqualTo(UPDATED_PHOTO);
    }

    @Test
    @Transactional
    void fullUpdateEnseignantWithPatch() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();

        // Update the enseignant using partial update
        Enseignant partialUpdatedEnseignant = new Enseignant();
        partialUpdatedEnseignant.setId(enseignant.getId());

        partialUpdatedEnseignant
            .titreCoEncadreur(UPDATED_TITRE_CO_ENCADREUR)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .titresId(UPDATED_TITRES_ID)
            .adresse(UPDATED_ADRESSE)
            .numeroPoste(UPDATED_NUMERO_POSTE)
            .photo(UPDATED_PHOTO);

        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnseignant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnseignant))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        Enseignant testEnseignant = enseignantList.get(enseignantList.size() - 1);
        assertThat(testEnseignant.getTitreCoEncadreur()).isEqualTo(UPDATED_TITRE_CO_ENCADREUR);
        assertThat(testEnseignant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEnseignant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnseignant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEnseignant.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testEnseignant.getTitresId()).isEqualTo(UPDATED_TITRES_ID);
        assertThat(testEnseignant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testEnseignant.getNumeroPoste()).isEqualTo(UPDATED_NUMERO_POSTE);
        assertThat(testEnseignant.getPhoto()).isEqualTo(UPDATED_PHOTO);
    }

    @Test
    @Transactional
    void patchNonExistingEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enseignantDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enseignantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);
        enseignantRepository.save(enseignant);
        enseignantSearchRepository.save(enseignant);

        int databaseSizeBeforeDelete = enseignantRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the enseignant
        restEnseignantMockMvc
            .perform(delete(ENTITY_API_URL_ID, enseignant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(enseignantSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEnseignant() throws Exception {
        // Initialize the database
        enseignant = enseignantRepository.saveAndFlush(enseignant);
        enseignantSearchRepository.save(enseignant);

        // Search the enseignant
        restEnseignantMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + enseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].titreCoEncadreur").value(hasItem(DEFAULT_TITRE_CO_ENCADREUR)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].titresId").value(hasItem(DEFAULT_TITRES_ID.intValue())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].numeroPoste").value(hasItem(DEFAULT_NUMERO_POSTE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO)));
    }
}
