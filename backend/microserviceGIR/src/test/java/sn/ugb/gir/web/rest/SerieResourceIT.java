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
import sn.ugb.gir.domain.Serie;
import sn.ugb.gir.repository.SerieRepository;
import sn.ugb.gir.service.dto.SerieDTO;
import sn.ugb.gir.service.mapper.SerieMapper;

/**
 * Integration tests for the {@link SerieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SerieResourceIT {

    private static final String DEFAULT_CODE_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_SERIE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_SERIE = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLE_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE_SERIE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/series";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SerieRepository serieRepository;

    @Autowired
    private SerieMapper serieMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSerieMockMvc;

    private Serie serie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Serie createEntity(EntityManager em) {
        Serie serie = new Serie().codeSerie(DEFAULT_CODE_SERIE).libelleSerie(DEFAULT_LIBELLE_SERIE).sigleSerie(DEFAULT_SIGLE_SERIE);
        return serie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Serie createUpdatedEntity(EntityManager em) {
        Serie serie = new Serie().codeSerie(UPDATED_CODE_SERIE).libelleSerie(UPDATED_LIBELLE_SERIE).sigleSerie(UPDATED_SIGLE_SERIE);
        return serie;
    }

    @BeforeEach
    public void initTest() {
        serie = createEntity(em);
    }

    @Test
    @Transactional
    void createSerie() throws Exception {
        int databaseSizeBeforeCreate = serieRepository.findAll().size();
        // Create the Serie
        SerieDTO serieDTO = serieMapper.toDto(serie);
        restSerieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serieDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Serie in the database
        List<Serie> serieList = serieRepository.findAll();
        assertThat(serieList).hasSize(databaseSizeBeforeCreate + 1);
        Serie testSerie = serieList.get(serieList.size() - 1);
        assertThat(testSerie.getCodeSerie()).isEqualTo(DEFAULT_CODE_SERIE);
        assertThat(testSerie.getLibelleSerie()).isEqualTo(DEFAULT_LIBELLE_SERIE);
        assertThat(testSerie.getSigleSerie()).isEqualTo(DEFAULT_SIGLE_SERIE);
    }

    @Test
    @Transactional
    void createSerieWithExistingId() throws Exception {
        // Create the Serie with an existing ID
        serie.setId(1L);
        SerieDTO serieDTO = serieMapper.toDto(serie);

        int databaseSizeBeforeCreate = serieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSerieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Serie in the database
        List<Serie> serieList = serieRepository.findAll();
        assertThat(serieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSeries() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        // Get all the serieList
        restSerieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serie.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeSerie").value(hasItem(DEFAULT_CODE_SERIE)))
            .andExpect(jsonPath("$.[*].libelleSerie").value(hasItem(DEFAULT_LIBELLE_SERIE)))
            .andExpect(jsonPath("$.[*].sigleSerie").value(hasItem(DEFAULT_SIGLE_SERIE)));
    }

    @Test
    @Transactional
    void getSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        // Get the serie
        restSerieMockMvc
            .perform(get(ENTITY_API_URL_ID, serie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serie.getId().intValue()))
            .andExpect(jsonPath("$.codeSerie").value(DEFAULT_CODE_SERIE))
            .andExpect(jsonPath("$.libelleSerie").value(DEFAULT_LIBELLE_SERIE))
            .andExpect(jsonPath("$.sigleSerie").value(DEFAULT_SIGLE_SERIE));
    }

    @Test
    @Transactional
    void getNonExistingSerie() throws Exception {
        // Get the serie
        restSerieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        int databaseSizeBeforeUpdate = serieRepository.findAll().size();

        // Update the serie
        Serie updatedSerie = serieRepository.findById(serie.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSerie are not directly saved in db
        em.detach(updatedSerie);
        updatedSerie.codeSerie(UPDATED_CODE_SERIE).libelleSerie(UPDATED_LIBELLE_SERIE).sigleSerie(UPDATED_SIGLE_SERIE);
        SerieDTO serieDTO = serieMapper.toDto(updatedSerie);

        restSerieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serieDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serieDTO))
            )
            .andExpect(status().isOk());

        // Validate the Serie in the database
        List<Serie> serieList = serieRepository.findAll();
        assertThat(serieList).hasSize(databaseSizeBeforeUpdate);
        Serie testSerie = serieList.get(serieList.size() - 1);
        assertThat(testSerie.getCodeSerie()).isEqualTo(UPDATED_CODE_SERIE);
        assertThat(testSerie.getLibelleSerie()).isEqualTo(UPDATED_LIBELLE_SERIE);
        assertThat(testSerie.getSigleSerie()).isEqualTo(UPDATED_SIGLE_SERIE);
    }

    @Test
    @Transactional
    void putNonExistingSerie() throws Exception {
        int databaseSizeBeforeUpdate = serieRepository.findAll().size();
        serie.setId(longCount.incrementAndGet());

        // Create the Serie
        SerieDTO serieDTO = serieMapper.toDto(serie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSerieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serieDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Serie in the database
        List<Serie> serieList = serieRepository.findAll();
        assertThat(serieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSerie() throws Exception {
        int databaseSizeBeforeUpdate = serieRepository.findAll().size();
        serie.setId(longCount.incrementAndGet());

        // Create the Serie
        SerieDTO serieDTO = serieMapper.toDto(serie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSerieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Serie in the database
        List<Serie> serieList = serieRepository.findAll();
        assertThat(serieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSerie() throws Exception {
        int databaseSizeBeforeUpdate = serieRepository.findAll().size();
        serie.setId(longCount.incrementAndGet());

        // Create the Serie
        SerieDTO serieDTO = serieMapper.toDto(serie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSerieMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Serie in the database
        List<Serie> serieList = serieRepository.findAll();
        assertThat(serieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSerieWithPatch() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        int databaseSizeBeforeUpdate = serieRepository.findAll().size();

        // Update the serie using partial update
        Serie partialUpdatedSerie = new Serie();
        partialUpdatedSerie.setId(serie.getId());

        partialUpdatedSerie.codeSerie(UPDATED_CODE_SERIE).libelleSerie(UPDATED_LIBELLE_SERIE);

        restSerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSerie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSerie))
            )
            .andExpect(status().isOk());

        // Validate the Serie in the database
        List<Serie> serieList = serieRepository.findAll();
        assertThat(serieList).hasSize(databaseSizeBeforeUpdate);
        Serie testSerie = serieList.get(serieList.size() - 1);
        assertThat(testSerie.getCodeSerie()).isEqualTo(UPDATED_CODE_SERIE);
        assertThat(testSerie.getLibelleSerie()).isEqualTo(UPDATED_LIBELLE_SERIE);
        assertThat(testSerie.getSigleSerie()).isEqualTo(DEFAULT_SIGLE_SERIE);
    }

    @Test
    @Transactional
    void fullUpdateSerieWithPatch() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        int databaseSizeBeforeUpdate = serieRepository.findAll().size();

        // Update the serie using partial update
        Serie partialUpdatedSerie = new Serie();
        partialUpdatedSerie.setId(serie.getId());

        partialUpdatedSerie.codeSerie(UPDATED_CODE_SERIE).libelleSerie(UPDATED_LIBELLE_SERIE).sigleSerie(UPDATED_SIGLE_SERIE);

        restSerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSerie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSerie))
            )
            .andExpect(status().isOk());

        // Validate the Serie in the database
        List<Serie> serieList = serieRepository.findAll();
        assertThat(serieList).hasSize(databaseSizeBeforeUpdate);
        Serie testSerie = serieList.get(serieList.size() - 1);
        assertThat(testSerie.getCodeSerie()).isEqualTo(UPDATED_CODE_SERIE);
        assertThat(testSerie.getLibelleSerie()).isEqualTo(UPDATED_LIBELLE_SERIE);
        assertThat(testSerie.getSigleSerie()).isEqualTo(UPDATED_SIGLE_SERIE);
    }

    @Test
    @Transactional
    void patchNonExistingSerie() throws Exception {
        int databaseSizeBeforeUpdate = serieRepository.findAll().size();
        serie.setId(longCount.incrementAndGet());

        // Create the Serie
        SerieDTO serieDTO = serieMapper.toDto(serie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serieDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Serie in the database
        List<Serie> serieList = serieRepository.findAll();
        assertThat(serieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSerie() throws Exception {
        int databaseSizeBeforeUpdate = serieRepository.findAll().size();
        serie.setId(longCount.incrementAndGet());

        // Create the Serie
        SerieDTO serieDTO = serieMapper.toDto(serie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Serie in the database
        List<Serie> serieList = serieRepository.findAll();
        assertThat(serieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSerie() throws Exception {
        int databaseSizeBeforeUpdate = serieRepository.findAll().size();
        serie.setId(longCount.incrementAndGet());

        // Create the Serie
        SerieDTO serieDTO = serieMapper.toDto(serie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSerieMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Serie in the database
        List<Serie> serieList = serieRepository.findAll();
        assertThat(serieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        int databaseSizeBeforeDelete = serieRepository.findAll().size();

        // Delete the serie
        restSerieMockMvc
            .perform(delete(ENTITY_API_URL_ID, serie.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Serie> serieList = serieRepository.findAll();
        assertThat(serieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
