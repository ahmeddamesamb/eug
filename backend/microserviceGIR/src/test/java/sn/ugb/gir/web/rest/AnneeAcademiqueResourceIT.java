package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
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
import sn.ugb.gir.IntegrationTest;
import sn.ugb.gir.domain.AnneeAcademique;
import sn.ugb.gir.repository.AnneeAcademiqueRepository;
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

    private static final Integer DEFAULT_ANNEE_COURANTE = 1;
    private static final Integer UPDATED_ANNEE_COURANTE = 2;

    private static final String ENTITY_API_URL = "/api/annee-academiques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnneeAcademiqueRepository anneeAcademiqueRepository;

    @Autowired
    private AnneeAcademiqueMapper anneeAcademiqueMapper;

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
            .anneeCourante(DEFAULT_ANNEE_COURANTE);
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
            .anneeCourante(UPDATED_ANNEE_COURANTE);
        return anneeAcademique;
    }

    @BeforeEach
    public void initTest() {
        anneeAcademique = createEntity(em);
    }

    @Test
    @Transactional
    void createAnneeAcademique() throws Exception {
        int databaseSizeBeforeCreate = anneeAcademiqueRepository.findAll().size();
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
        AnneeAcademique testAnneeAcademique = anneeAcademiqueList.get(anneeAcademiqueList.size() - 1);
        assertThat(testAnneeAcademique.getLibelleAnneeAcademique()).isEqualTo(DEFAULT_LIBELLE_ANNEE_ACADEMIQUE);
        assertThat(testAnneeAcademique.getAnneeCourante()).isEqualTo(DEFAULT_ANNEE_COURANTE);
    }

    @Test
    @Transactional
    void createAnneeAcademiqueWithExistingId() throws Exception {
        // Create the AnneeAcademique with an existing ID
        anneeAcademique.setId(1L);
        AnneeAcademiqueDTO anneeAcademiqueDTO = anneeAcademiqueMapper.toDto(anneeAcademique);

        int databaseSizeBeforeCreate = anneeAcademiqueRepository.findAll().size();

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
            .andExpect(jsonPath("$.[*].anneeCourante").value(hasItem(DEFAULT_ANNEE_COURANTE)));
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
            .andExpect(jsonPath("$.anneeCourante").value(DEFAULT_ANNEE_COURANTE));
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

        // Update the anneeAcademique
        AnneeAcademique updatedAnneeAcademique = anneeAcademiqueRepository.findById(anneeAcademique.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAnneeAcademique are not directly saved in db
        em.detach(updatedAnneeAcademique);
        updatedAnneeAcademique.libelleAnneeAcademique(UPDATED_LIBELLE_ANNEE_ACADEMIQUE).anneeCourante(UPDATED_ANNEE_COURANTE);
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
        assertThat(testAnneeAcademique.getAnneeCourante()).isEqualTo(UPDATED_ANNEE_COURANTE);
    }

    @Test
    @Transactional
    void putNonExistingAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void putWithIdMismatchAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
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

        partialUpdatedAnneeAcademique.libelleAnneeAcademique(UPDATED_LIBELLE_ANNEE_ACADEMIQUE);

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
        assertThat(testAnneeAcademique.getAnneeCourante()).isEqualTo(DEFAULT_ANNEE_COURANTE);
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

        partialUpdatedAnneeAcademique.libelleAnneeAcademique(UPDATED_LIBELLE_ANNEE_ACADEMIQUE).anneeCourante(UPDATED_ANNEE_COURANTE);

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
        assertThat(testAnneeAcademique.getAnneeCourante()).isEqualTo(UPDATED_ANNEE_COURANTE);
    }

    @Test
    @Transactional
    void patchNonExistingAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnneeAcademique() throws Exception {
        int databaseSizeBeforeUpdate = anneeAcademiqueRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void deleteAnneeAcademique() throws Exception {
        // Initialize the database
        anneeAcademiqueRepository.saveAndFlush(anneeAcademique);

        int databaseSizeBeforeDelete = anneeAcademiqueRepository.findAll().size();

        // Delete the anneeAcademique
        restAnneeAcademiqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, anneeAcademique.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnneeAcademique> anneeAcademiqueList = anneeAcademiqueRepository.findAll();
        assertThat(anneeAcademiqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
