package sn.ugb.gd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.ugb.gd.IntegrationTest;
import sn.ugb.gd.domain.Rapport;
import sn.ugb.gd.repository.RapportRepository;
import sn.ugb.gd.service.dto.RapportDTO;
import sn.ugb.gd.service.mapper.RapportMapper;

/**
 * Integration tests for the {@link RapportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RapportResourceIT {

    private static final String DEFAULT_LIBELLE_RAPPORT = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_RAPPORT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_RAPPORT = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_RAPPORT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENU_RAPPORT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU_RAPPORT = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_REDACTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_REDACTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/rapports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RapportRepository rapportRepository;

    @Autowired
    private RapportMapper rapportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRapportMockMvc;

    private Rapport rapport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rapport createEntity(EntityManager em) {
        Rapport rapport = new Rapport()
            .libelleRapport(DEFAULT_LIBELLE_RAPPORT)
            .descriptionRapport(DEFAULT_DESCRIPTION_RAPPORT)
            .contenuRapport(DEFAULT_CONTENU_RAPPORT)
            .dateRedaction(DEFAULT_DATE_REDACTION);
        return rapport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rapport createUpdatedEntity(EntityManager em) {
        Rapport rapport = new Rapport()
            .libelleRapport(UPDATED_LIBELLE_RAPPORT)
            .descriptionRapport(UPDATED_DESCRIPTION_RAPPORT)
            .contenuRapport(UPDATED_CONTENU_RAPPORT)
            .dateRedaction(UPDATED_DATE_REDACTION);
        return rapport;
    }

    @BeforeEach
    public void initTest() {
        rapport = createEntity(em);
    }

    @Test
    @Transactional
    void createRapport() throws Exception {
        int databaseSizeBeforeCreate = rapportRepository.findAll().size();
        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);
        restRapportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeCreate + 1);
        Rapport testRapport = rapportList.get(rapportList.size() - 1);
        assertThat(testRapport.getLibelleRapport()).isEqualTo(DEFAULT_LIBELLE_RAPPORT);
        assertThat(testRapport.getDescriptionRapport()).isEqualTo(DEFAULT_DESCRIPTION_RAPPORT);
        assertThat(testRapport.getContenuRapport()).isEqualTo(DEFAULT_CONTENU_RAPPORT);
        assertThat(testRapport.getDateRedaction()).isEqualTo(DEFAULT_DATE_REDACTION);
    }

    @Test
    @Transactional
    void createRapportWithExistingId() throws Exception {
        // Create the Rapport with an existing ID
        rapport.setId(1L);
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        int databaseSizeBeforeCreate = rapportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRapportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRapports() throws Exception {
        // Initialize the database
        rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList
        restRapportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rapport.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleRapport").value(hasItem(DEFAULT_LIBELLE_RAPPORT)))
            .andExpect(jsonPath("$.[*].descriptionRapport").value(hasItem(DEFAULT_DESCRIPTION_RAPPORT)))
            .andExpect(jsonPath("$.[*].contenuRapport").value(hasItem(DEFAULT_CONTENU_RAPPORT)))
            .andExpect(jsonPath("$.[*].dateRedaction").value(hasItem(DEFAULT_DATE_REDACTION.toString())));
    }

    @Test
    @Transactional
    void getRapport() throws Exception {
        // Initialize the database
        rapportRepository.saveAndFlush(rapport);

        // Get the rapport
        restRapportMockMvc
            .perform(get(ENTITY_API_URL_ID, rapport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rapport.getId().intValue()))
            .andExpect(jsonPath("$.libelleRapport").value(DEFAULT_LIBELLE_RAPPORT))
            .andExpect(jsonPath("$.descriptionRapport").value(DEFAULT_DESCRIPTION_RAPPORT))
            .andExpect(jsonPath("$.contenuRapport").value(DEFAULT_CONTENU_RAPPORT))
            .andExpect(jsonPath("$.dateRedaction").value(DEFAULT_DATE_REDACTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRapport() throws Exception {
        // Get the rapport
        restRapportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRapport() throws Exception {
        // Initialize the database
        rapportRepository.saveAndFlush(rapport);

        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();

        // Update the rapport
        Rapport updatedRapport = rapportRepository.findById(rapport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRapport are not directly saved in db
        em.detach(updatedRapport);
        updatedRapport
            .libelleRapport(UPDATED_LIBELLE_RAPPORT)
            .descriptionRapport(UPDATED_DESCRIPTION_RAPPORT)
            .contenuRapport(UPDATED_CONTENU_RAPPORT)
            .dateRedaction(UPDATED_DATE_REDACTION);
        RapportDTO rapportDTO = rapportMapper.toDto(updatedRapport);

        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rapportDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapportDTO))
            )
            .andExpect(status().isOk());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
        Rapport testRapport = rapportList.get(rapportList.size() - 1);
        assertThat(testRapport.getLibelleRapport()).isEqualTo(UPDATED_LIBELLE_RAPPORT);
        assertThat(testRapport.getDescriptionRapport()).isEqualTo(UPDATED_DESCRIPTION_RAPPORT);
        assertThat(testRapport.getContenuRapport()).isEqualTo(UPDATED_CONTENU_RAPPORT);
        assertThat(testRapport.getDateRedaction()).isEqualTo(UPDATED_DATE_REDACTION);
    }

    @Test
    @Transactional
    void putNonExistingRapport() throws Exception {
        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rapportDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRapport() throws Exception {
        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRapport() throws Exception {
        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRapportWithPatch() throws Exception {
        // Initialize the database
        rapportRepository.saveAndFlush(rapport);

        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();

        // Update the rapport using partial update
        Rapport partialUpdatedRapport = new Rapport();
        partialUpdatedRapport.setId(rapport.getId());

        partialUpdatedRapport
            .libelleRapport(UPDATED_LIBELLE_RAPPORT)
            .descriptionRapport(UPDATED_DESCRIPTION_RAPPORT)
            .contenuRapport(UPDATED_CONTENU_RAPPORT)
            .dateRedaction(UPDATED_DATE_REDACTION);

        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRapport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRapport))
            )
            .andExpect(status().isOk());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
        Rapport testRapport = rapportList.get(rapportList.size() - 1);
        assertThat(testRapport.getLibelleRapport()).isEqualTo(UPDATED_LIBELLE_RAPPORT);
        assertThat(testRapport.getDescriptionRapport()).isEqualTo(UPDATED_DESCRIPTION_RAPPORT);
        assertThat(testRapport.getContenuRapport()).isEqualTo(UPDATED_CONTENU_RAPPORT);
        assertThat(testRapport.getDateRedaction()).isEqualTo(UPDATED_DATE_REDACTION);
    }

    @Test
    @Transactional
    void fullUpdateRapportWithPatch() throws Exception {
        // Initialize the database
        rapportRepository.saveAndFlush(rapport);

        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();

        // Update the rapport using partial update
        Rapport partialUpdatedRapport = new Rapport();
        partialUpdatedRapport.setId(rapport.getId());

        partialUpdatedRapport
            .libelleRapport(UPDATED_LIBELLE_RAPPORT)
            .descriptionRapport(UPDATED_DESCRIPTION_RAPPORT)
            .contenuRapport(UPDATED_CONTENU_RAPPORT)
            .dateRedaction(UPDATED_DATE_REDACTION);

        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRapport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRapport))
            )
            .andExpect(status().isOk());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
        Rapport testRapport = rapportList.get(rapportList.size() - 1);
        assertThat(testRapport.getLibelleRapport()).isEqualTo(UPDATED_LIBELLE_RAPPORT);
        assertThat(testRapport.getDescriptionRapport()).isEqualTo(UPDATED_DESCRIPTION_RAPPORT);
        assertThat(testRapport.getContenuRapport()).isEqualTo(UPDATED_CONTENU_RAPPORT);
        assertThat(testRapport.getDateRedaction()).isEqualTo(UPDATED_DATE_REDACTION);
    }

    @Test
    @Transactional
    void patchNonExistingRapport() throws Exception {
        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rapportDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRapport() throws Exception {
        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRapport() throws Exception {
        int databaseSizeBeforeUpdate = rapportRepository.findAll().size();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rapportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rapport in the database
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRapport() throws Exception {
        // Initialize the database
        rapportRepository.saveAndFlush(rapport);

        int databaseSizeBeforeDelete = rapportRepository.findAll().size();

        // Delete the rapport
        restRapportMockMvc
            .perform(delete(ENTITY_API_URL_ID, rapport.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rapport> rapportList = rapportRepository.findAll();
        assertThat(rapportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
