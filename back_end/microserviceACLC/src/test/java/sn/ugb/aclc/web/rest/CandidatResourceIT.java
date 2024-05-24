package sn.ugb.aclc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.ugb.aclc.IntegrationTest;
import sn.ugb.aclc.domain.Candidat;
import sn.ugb.aclc.repository.CandidatRepository;
import sn.ugb.aclc.service.dto.CandidatDTO;
import sn.ugb.aclc.service.mapper.CandidatMapper;

/**
 * Integration tests for the {@link CandidatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CandidatResourceIT {

    private static final String DEFAULT_NOM_CANDITAT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CANDITAT = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_CANDIDAT = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_CANDIDAT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE_CANDIDAT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE_CANDIDAT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EMAIL_CANDIDAT = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_CANDIDAT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/candidats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CandidatRepository candidatRepository;

    @Autowired
    private CandidatMapper candidatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidatMockMvc;

    private Candidat candidat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidat createEntity(EntityManager em) {
        Candidat candidat = new Candidat()
            .nomCanditat(DEFAULT_NOM_CANDITAT)
            .prenomCandidat(DEFAULT_PRENOM_CANDIDAT)
            .dateNaissanceCandidat(DEFAULT_DATE_NAISSANCE_CANDIDAT)
            .emailCandidat(DEFAULT_EMAIL_CANDIDAT);
        return candidat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidat createUpdatedEntity(EntityManager em) {
        Candidat candidat = new Candidat()
            .nomCanditat(UPDATED_NOM_CANDITAT)
            .prenomCandidat(UPDATED_PRENOM_CANDIDAT)
            .dateNaissanceCandidat(UPDATED_DATE_NAISSANCE_CANDIDAT)
            .emailCandidat(UPDATED_EMAIL_CANDIDAT);
        return candidat;
    }

    @BeforeEach
    public void initTest() {
        candidat = createEntity(em);
    }

    @Test
    @Transactional
    void createCandidat() throws Exception {
        int databaseSizeBeforeCreate = candidatRepository.findAll().size();
        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);
        restCandidatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Candidat in the database
        List<Candidat> candidatList = candidatRepository.findAll();
        assertThat(candidatList).hasSize(databaseSizeBeforeCreate + 1);
        Candidat testCandidat = candidatList.get(candidatList.size() - 1);
        assertThat(testCandidat.getNomCanditat()).isEqualTo(DEFAULT_NOM_CANDITAT);
        assertThat(testCandidat.getPrenomCandidat()).isEqualTo(DEFAULT_PRENOM_CANDIDAT);
        assertThat(testCandidat.getDateNaissanceCandidat()).isEqualTo(DEFAULT_DATE_NAISSANCE_CANDIDAT);
        assertThat(testCandidat.getEmailCandidat()).isEqualTo(DEFAULT_EMAIL_CANDIDAT);
    }

    @Test
    @Transactional
    void createCandidatWithExistingId() throws Exception {
        // Create the Candidat with an existing ID
        candidat.setId(1L);
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        int databaseSizeBeforeCreate = candidatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidat in the database
        List<Candidat> candidatList = candidatRepository.findAll();
        assertThat(candidatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCandidats() throws Exception {
        // Initialize the database
        candidatRepository.saveAndFlush(candidat);

        // Get all the candidatList
        restCandidatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidat.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCanditat").value(hasItem(DEFAULT_NOM_CANDITAT)))
            .andExpect(jsonPath("$.[*].prenomCandidat").value(hasItem(DEFAULT_PRENOM_CANDIDAT)))
            .andExpect(jsonPath("$.[*].dateNaissanceCandidat").value(hasItem(DEFAULT_DATE_NAISSANCE_CANDIDAT.toString())))
            .andExpect(jsonPath("$.[*].emailCandidat").value(hasItem(DEFAULT_EMAIL_CANDIDAT)));
    }

    @Test
    @Transactional
    void getCandidat() throws Exception {
        // Initialize the database
        candidatRepository.saveAndFlush(candidat);

        // Get the candidat
        restCandidatMockMvc
            .perform(get(ENTITY_API_URL_ID, candidat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidat.getId().intValue()))
            .andExpect(jsonPath("$.nomCanditat").value(DEFAULT_NOM_CANDITAT))
            .andExpect(jsonPath("$.prenomCandidat").value(DEFAULT_PRENOM_CANDIDAT))
            .andExpect(jsonPath("$.dateNaissanceCandidat").value(DEFAULT_DATE_NAISSANCE_CANDIDAT.toString()))
            .andExpect(jsonPath("$.emailCandidat").value(DEFAULT_EMAIL_CANDIDAT));
    }

    @Test
    @Transactional
    void getNonExistingCandidat() throws Exception {
        // Get the candidat
        restCandidatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCandidat() throws Exception {
        // Initialize the database
        candidatRepository.saveAndFlush(candidat);

        int databaseSizeBeforeUpdate = candidatRepository.findAll().size();

        // Update the candidat
        Candidat updatedCandidat = candidatRepository.findById(candidat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCandidat are not directly saved in db
        em.detach(updatedCandidat);
        updatedCandidat
            .nomCanditat(UPDATED_NOM_CANDITAT)
            .prenomCandidat(UPDATED_PRENOM_CANDIDAT)
            .dateNaissanceCandidat(UPDATED_DATE_NAISSANCE_CANDIDAT)
            .emailCandidat(UPDATED_EMAIL_CANDIDAT);
        CandidatDTO candidatDTO = candidatMapper.toDto(updatedCandidat);

        restCandidatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidatDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatDTO))
            )
            .andExpect(status().isOk());

        // Validate the Candidat in the database
        List<Candidat> candidatList = candidatRepository.findAll();
        assertThat(candidatList).hasSize(databaseSizeBeforeUpdate);
        Candidat testCandidat = candidatList.get(candidatList.size() - 1);
        assertThat(testCandidat.getNomCanditat()).isEqualTo(UPDATED_NOM_CANDITAT);
        assertThat(testCandidat.getPrenomCandidat()).isEqualTo(UPDATED_PRENOM_CANDIDAT);
        assertThat(testCandidat.getDateNaissanceCandidat()).isEqualTo(UPDATED_DATE_NAISSANCE_CANDIDAT);
        assertThat(testCandidat.getEmailCandidat()).isEqualTo(UPDATED_EMAIL_CANDIDAT);
    }

    @Test
    @Transactional
    void putNonExistingCandidat() throws Exception {
        int databaseSizeBeforeUpdate = candidatRepository.findAll().size();
        candidat.setId(longCount.incrementAndGet());

        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidatDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidat in the database
        List<Candidat> candidatList = candidatRepository.findAll();
        assertThat(candidatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCandidat() throws Exception {
        int databaseSizeBeforeUpdate = candidatRepository.findAll().size();
        candidat.setId(longCount.incrementAndGet());

        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidat in the database
        List<Candidat> candidatList = candidatRepository.findAll();
        assertThat(candidatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCandidat() throws Exception {
        int databaseSizeBeforeUpdate = candidatRepository.findAll().size();
        candidat.setId(longCount.incrementAndGet());

        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Candidat in the database
        List<Candidat> candidatList = candidatRepository.findAll();
        assertThat(candidatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCandidatWithPatch() throws Exception {
        // Initialize the database
        candidatRepository.saveAndFlush(candidat);

        int databaseSizeBeforeUpdate = candidatRepository.findAll().size();

        // Update the candidat using partial update
        Candidat partialUpdatedCandidat = new Candidat();
        partialUpdatedCandidat.setId(candidat.getId());

        partialUpdatedCandidat.emailCandidat(UPDATED_EMAIL_CANDIDAT);

        restCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidat))
            )
            .andExpect(status().isOk());

        // Validate the Candidat in the database
        List<Candidat> candidatList = candidatRepository.findAll();
        assertThat(candidatList).hasSize(databaseSizeBeforeUpdate);
        Candidat testCandidat = candidatList.get(candidatList.size() - 1);
        assertThat(testCandidat.getNomCanditat()).isEqualTo(DEFAULT_NOM_CANDITAT);
        assertThat(testCandidat.getPrenomCandidat()).isEqualTo(DEFAULT_PRENOM_CANDIDAT);
        assertThat(testCandidat.getDateNaissanceCandidat()).isEqualTo(DEFAULT_DATE_NAISSANCE_CANDIDAT);
        assertThat(testCandidat.getEmailCandidat()).isEqualTo(UPDATED_EMAIL_CANDIDAT);
    }

    @Test
    @Transactional
    void fullUpdateCandidatWithPatch() throws Exception {
        // Initialize the database
        candidatRepository.saveAndFlush(candidat);

        int databaseSizeBeforeUpdate = candidatRepository.findAll().size();

        // Update the candidat using partial update
        Candidat partialUpdatedCandidat = new Candidat();
        partialUpdatedCandidat.setId(candidat.getId());

        partialUpdatedCandidat
            .nomCanditat(UPDATED_NOM_CANDITAT)
            .prenomCandidat(UPDATED_PRENOM_CANDIDAT)
            .dateNaissanceCandidat(UPDATED_DATE_NAISSANCE_CANDIDAT)
            .emailCandidat(UPDATED_EMAIL_CANDIDAT);

        restCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidat))
            )
            .andExpect(status().isOk());

        // Validate the Candidat in the database
        List<Candidat> candidatList = candidatRepository.findAll();
        assertThat(candidatList).hasSize(databaseSizeBeforeUpdate);
        Candidat testCandidat = candidatList.get(candidatList.size() - 1);
        assertThat(testCandidat.getNomCanditat()).isEqualTo(UPDATED_NOM_CANDITAT);
        assertThat(testCandidat.getPrenomCandidat()).isEqualTo(UPDATED_PRENOM_CANDIDAT);
        assertThat(testCandidat.getDateNaissanceCandidat()).isEqualTo(UPDATED_DATE_NAISSANCE_CANDIDAT);
        assertThat(testCandidat.getEmailCandidat()).isEqualTo(UPDATED_EMAIL_CANDIDAT);
    }

    @Test
    @Transactional
    void patchNonExistingCandidat() throws Exception {
        int databaseSizeBeforeUpdate = candidatRepository.findAll().size();
        candidat.setId(longCount.incrementAndGet());

        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, candidatDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidat in the database
        List<Candidat> candidatList = candidatRepository.findAll();
        assertThat(candidatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCandidat() throws Exception {
        int databaseSizeBeforeUpdate = candidatRepository.findAll().size();
        candidat.setId(longCount.incrementAndGet());

        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidat in the database
        List<Candidat> candidatList = candidatRepository.findAll();
        assertThat(candidatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCandidat() throws Exception {
        int databaseSizeBeforeUpdate = candidatRepository.findAll().size();
        candidat.setId(longCount.incrementAndGet());

        // Create the Candidat
        CandidatDTO candidatDTO = candidatMapper.toDto(candidat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Candidat in the database
        List<Candidat> candidatList = candidatRepository.findAll();
        assertThat(candidatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCandidat() throws Exception {
        // Initialize the database
        candidatRepository.saveAndFlush(candidat);

        int databaseSizeBeforeDelete = candidatRepository.findAll().size();

        // Delete the candidat
        restCandidatMockMvc
            .perform(delete(ENTITY_API_URL_ID, candidat.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Candidat> candidatList = candidatRepository.findAll();
        assertThat(candidatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
