package sn.ugb.grh.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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

    private static final String DEFAULT_NOM_ENSEIGNANT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ENSEIGNANT = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_ENSEIGNANT = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_ENSEIGNANT = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ENSEIGNANT = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ENSEIGNANT = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE_ENSEIGNANT = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE_ENSEIGNANT = "BBBBBBBBBB";

    private static final Integer DEFAULT_TITRES_ID = 1;
    private static final Integer UPDATED_TITRES_ID = 2;

    private static final Integer DEFAULT_UTILISATEURS_ID = 1;
    private static final Integer UPDATED_UTILISATEURS_ID = 2;

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_POSTE = 1;
    private static final Integer UPDATED_NUMERO_POSTE = 2;

    private static final byte[] DEFAULT_PHOTO_ENSEIGNANT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO_ENSEIGNANT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_ENSEIGNANT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_ENSEIGNANT_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/enseignants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private EnseignantMapper enseignantMapper;

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
            .nomEnseignant(DEFAULT_NOM_ENSEIGNANT)
            .prenomEnseignant(DEFAULT_PRENOM_ENSEIGNANT)
            .emailEnseignant(DEFAULT_EMAIL_ENSEIGNANT)
            .telephoneEnseignant(DEFAULT_TELEPHONE_ENSEIGNANT)
            .titresId(DEFAULT_TITRES_ID)
            .utilisateursId(DEFAULT_UTILISATEURS_ID)
            .adresse(DEFAULT_ADRESSE)
            .numeroPoste(DEFAULT_NUMERO_POSTE)
            .photoEnseignant(DEFAULT_PHOTO_ENSEIGNANT)
            .photoEnseignantContentType(DEFAULT_PHOTO_ENSEIGNANT_CONTENT_TYPE);
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
            .nomEnseignant(UPDATED_NOM_ENSEIGNANT)
            .prenomEnseignant(UPDATED_PRENOM_ENSEIGNANT)
            .emailEnseignant(UPDATED_EMAIL_ENSEIGNANT)
            .telephoneEnseignant(UPDATED_TELEPHONE_ENSEIGNANT)
            .titresId(UPDATED_TITRES_ID)
            .utilisateursId(UPDATED_UTILISATEURS_ID)
            .adresse(UPDATED_ADRESSE)
            .numeroPoste(UPDATED_NUMERO_POSTE)
            .photoEnseignant(UPDATED_PHOTO_ENSEIGNANT)
            .photoEnseignantContentType(UPDATED_PHOTO_ENSEIGNANT_CONTENT_TYPE);
        return enseignant;
    }

    @BeforeEach
    public void initTest() {
        enseignant = createEntity(em);
    }

    @Test
    @Transactional
    void createEnseignant() throws Exception {
        int databaseSizeBeforeCreate = enseignantRepository.findAll().size();
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
        Enseignant testEnseignant = enseignantList.get(enseignantList.size() - 1);
        assertThat(testEnseignant.getTitreCoEncadreur()).isEqualTo(DEFAULT_TITRE_CO_ENCADREUR);
        assertThat(testEnseignant.getNomEnseignant()).isEqualTo(DEFAULT_NOM_ENSEIGNANT);
        assertThat(testEnseignant.getPrenomEnseignant()).isEqualTo(DEFAULT_PRENOM_ENSEIGNANT);
        assertThat(testEnseignant.getEmailEnseignant()).isEqualTo(DEFAULT_EMAIL_ENSEIGNANT);
        assertThat(testEnseignant.getTelephoneEnseignant()).isEqualTo(DEFAULT_TELEPHONE_ENSEIGNANT);
        assertThat(testEnseignant.getTitresId()).isEqualTo(DEFAULT_TITRES_ID);
        assertThat(testEnseignant.getUtilisateursId()).isEqualTo(DEFAULT_UTILISATEURS_ID);
        assertThat(testEnseignant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testEnseignant.getNumeroPoste()).isEqualTo(DEFAULT_NUMERO_POSTE);
        assertThat(testEnseignant.getPhotoEnseignant()).isEqualTo(DEFAULT_PHOTO_ENSEIGNANT);
        assertThat(testEnseignant.getPhotoEnseignantContentType()).isEqualTo(DEFAULT_PHOTO_ENSEIGNANT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createEnseignantWithExistingId() throws Exception {
        // Create the Enseignant with an existing ID
        enseignant.setId(1L);
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        int databaseSizeBeforeCreate = enseignantRepository.findAll().size();

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
    }

    @Test
    @Transactional
    void checkNomEnseignantIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setNomEnseignant(null);

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
    }

    @Test
    @Transactional
    void checkPrenomEnseignantIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setPrenomEnseignant(null);

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
            .andExpect(jsonPath("$.[*].nomEnseignant").value(hasItem(DEFAULT_NOM_ENSEIGNANT)))
            .andExpect(jsonPath("$.[*].prenomEnseignant").value(hasItem(DEFAULT_PRENOM_ENSEIGNANT)))
            .andExpect(jsonPath("$.[*].emailEnseignant").value(hasItem(DEFAULT_EMAIL_ENSEIGNANT)))
            .andExpect(jsonPath("$.[*].telephoneEnseignant").value(hasItem(DEFAULT_TELEPHONE_ENSEIGNANT)))
            .andExpect(jsonPath("$.[*].titresId").value(hasItem(DEFAULT_TITRES_ID)))
            .andExpect(jsonPath("$.[*].utilisateursId").value(hasItem(DEFAULT_UTILISATEURS_ID)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].numeroPoste").value(hasItem(DEFAULT_NUMERO_POSTE)))
            .andExpect(jsonPath("$.[*].photoEnseignantContentType").value(hasItem(DEFAULT_PHOTO_ENSEIGNANT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoEnseignant").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO_ENSEIGNANT))));
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
            .andExpect(jsonPath("$.nomEnseignant").value(DEFAULT_NOM_ENSEIGNANT))
            .andExpect(jsonPath("$.prenomEnseignant").value(DEFAULT_PRENOM_ENSEIGNANT))
            .andExpect(jsonPath("$.emailEnseignant").value(DEFAULT_EMAIL_ENSEIGNANT))
            .andExpect(jsonPath("$.telephoneEnseignant").value(DEFAULT_TELEPHONE_ENSEIGNANT))
            .andExpect(jsonPath("$.titresId").value(DEFAULT_TITRES_ID))
            .andExpect(jsonPath("$.utilisateursId").value(DEFAULT_UTILISATEURS_ID))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.numeroPoste").value(DEFAULT_NUMERO_POSTE))
            .andExpect(jsonPath("$.photoEnseignantContentType").value(DEFAULT_PHOTO_ENSEIGNANT_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoEnseignant").value(Base64.getEncoder().encodeToString(DEFAULT_PHOTO_ENSEIGNANT)));
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

        // Update the enseignant
        Enseignant updatedEnseignant = enseignantRepository.findById(enseignant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnseignant are not directly saved in db
        em.detach(updatedEnseignant);
        updatedEnseignant
            .titreCoEncadreur(UPDATED_TITRE_CO_ENCADREUR)
            .nomEnseignant(UPDATED_NOM_ENSEIGNANT)
            .prenomEnseignant(UPDATED_PRENOM_ENSEIGNANT)
            .emailEnseignant(UPDATED_EMAIL_ENSEIGNANT)
            .telephoneEnseignant(UPDATED_TELEPHONE_ENSEIGNANT)
            .titresId(UPDATED_TITRES_ID)
            .utilisateursId(UPDATED_UTILISATEURS_ID)
            .adresse(UPDATED_ADRESSE)
            .numeroPoste(UPDATED_NUMERO_POSTE)
            .photoEnseignant(UPDATED_PHOTO_ENSEIGNANT)
            .photoEnseignantContentType(UPDATED_PHOTO_ENSEIGNANT_CONTENT_TYPE);
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
        assertThat(testEnseignant.getNomEnseignant()).isEqualTo(UPDATED_NOM_ENSEIGNANT);
        assertThat(testEnseignant.getPrenomEnseignant()).isEqualTo(UPDATED_PRENOM_ENSEIGNANT);
        assertThat(testEnseignant.getEmailEnseignant()).isEqualTo(UPDATED_EMAIL_ENSEIGNANT);
        assertThat(testEnseignant.getTelephoneEnseignant()).isEqualTo(UPDATED_TELEPHONE_ENSEIGNANT);
        assertThat(testEnseignant.getTitresId()).isEqualTo(UPDATED_TITRES_ID);
        assertThat(testEnseignant.getUtilisateursId()).isEqualTo(UPDATED_UTILISATEURS_ID);
        assertThat(testEnseignant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testEnseignant.getNumeroPoste()).isEqualTo(UPDATED_NUMERO_POSTE);
        assertThat(testEnseignant.getPhotoEnseignant()).isEqualTo(UPDATED_PHOTO_ENSEIGNANT);
        assertThat(testEnseignant.getPhotoEnseignantContentType()).isEqualTo(UPDATED_PHOTO_ENSEIGNANT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void putWithIdMismatchEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
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
            .prenomEnseignant(UPDATED_PRENOM_ENSEIGNANT)
            .emailEnseignant(UPDATED_EMAIL_ENSEIGNANT)
            .utilisateursId(UPDATED_UTILISATEURS_ID)
            .adresse(UPDATED_ADRESSE)
            .numeroPoste(UPDATED_NUMERO_POSTE)
            .photoEnseignant(UPDATED_PHOTO_ENSEIGNANT)
            .photoEnseignantContentType(UPDATED_PHOTO_ENSEIGNANT_CONTENT_TYPE);

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
        assertThat(testEnseignant.getNomEnseignant()).isEqualTo(DEFAULT_NOM_ENSEIGNANT);
        assertThat(testEnseignant.getPrenomEnseignant()).isEqualTo(UPDATED_PRENOM_ENSEIGNANT);
        assertThat(testEnseignant.getEmailEnseignant()).isEqualTo(UPDATED_EMAIL_ENSEIGNANT);
        assertThat(testEnseignant.getTelephoneEnseignant()).isEqualTo(DEFAULT_TELEPHONE_ENSEIGNANT);
        assertThat(testEnseignant.getTitresId()).isEqualTo(DEFAULT_TITRES_ID);
        assertThat(testEnseignant.getUtilisateursId()).isEqualTo(UPDATED_UTILISATEURS_ID);
        assertThat(testEnseignant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testEnseignant.getNumeroPoste()).isEqualTo(UPDATED_NUMERO_POSTE);
        assertThat(testEnseignant.getPhotoEnseignant()).isEqualTo(UPDATED_PHOTO_ENSEIGNANT);
        assertThat(testEnseignant.getPhotoEnseignantContentType()).isEqualTo(UPDATED_PHOTO_ENSEIGNANT_CONTENT_TYPE);
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
            .nomEnseignant(UPDATED_NOM_ENSEIGNANT)
            .prenomEnseignant(UPDATED_PRENOM_ENSEIGNANT)
            .emailEnseignant(UPDATED_EMAIL_ENSEIGNANT)
            .telephoneEnseignant(UPDATED_TELEPHONE_ENSEIGNANT)
            .titresId(UPDATED_TITRES_ID)
            .utilisateursId(UPDATED_UTILISATEURS_ID)
            .adresse(UPDATED_ADRESSE)
            .numeroPoste(UPDATED_NUMERO_POSTE)
            .photoEnseignant(UPDATED_PHOTO_ENSEIGNANT)
            .photoEnseignantContentType(UPDATED_PHOTO_ENSEIGNANT_CONTENT_TYPE);

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
        assertThat(testEnseignant.getNomEnseignant()).isEqualTo(UPDATED_NOM_ENSEIGNANT);
        assertThat(testEnseignant.getPrenomEnseignant()).isEqualTo(UPDATED_PRENOM_ENSEIGNANT);
        assertThat(testEnseignant.getEmailEnseignant()).isEqualTo(UPDATED_EMAIL_ENSEIGNANT);
        assertThat(testEnseignant.getTelephoneEnseignant()).isEqualTo(UPDATED_TELEPHONE_ENSEIGNANT);
        assertThat(testEnseignant.getTitresId()).isEqualTo(UPDATED_TITRES_ID);
        assertThat(testEnseignant.getUtilisateursId()).isEqualTo(UPDATED_UTILISATEURS_ID);
        assertThat(testEnseignant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testEnseignant.getNumeroPoste()).isEqualTo(UPDATED_NUMERO_POSTE);
        assertThat(testEnseignant.getPhotoEnseignant()).isEqualTo(UPDATED_PHOTO_ENSEIGNANT);
        assertThat(testEnseignant.getPhotoEnseignantContentType()).isEqualTo(UPDATED_PHOTO_ENSEIGNANT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void deleteEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        int databaseSizeBeforeDelete = enseignantRepository.findAll().size();

        // Delete the enseignant
        restEnseignantMockMvc
            .perform(delete(ENTITY_API_URL_ID, enseignant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
