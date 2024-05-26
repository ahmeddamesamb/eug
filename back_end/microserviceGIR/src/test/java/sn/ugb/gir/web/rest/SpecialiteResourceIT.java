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
import sn.ugb.gir.domain.Specialite;
import sn.ugb.gir.repository.SpecialiteRepository;
import sn.ugb.gir.service.dto.SpecialiteDTO;
import sn.ugb.gir.service.mapper.SpecialiteMapper;

/**
 * Integration tests for the {@link SpecialiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialiteResourceIT {

    private static final String DEFAULT_NOM_SPECIALITES = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SPECIALITES = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLE_SPECIALITES = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE_SPECIALITES = "BBBBBBBBBB";

    private static final Integer DEFAULT_SPECIALITE_PARTICULIER_YN = 1;
    private static final Integer UPDATED_SPECIALITE_PARTICULIER_YN = 2;

    private static final Integer DEFAULT_SPECIALITES_PAYANTE_YN = 1;
    private static final Integer UPDATED_SPECIALITES_PAYANTE_YN = 2;

    private static final String ENTITY_API_URL = "/api/specialites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialiteRepository specialiteRepository;

    @Autowired
    private SpecialiteMapper specialiteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialiteMockMvc;

    private Specialite specialite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialite createEntity(EntityManager em) {
        Specialite specialite = new Specialite()
            .nomSpecialites(DEFAULT_NOM_SPECIALITES)
            .sigleSpecialites(DEFAULT_SIGLE_SPECIALITES)
            .specialiteParticulierYN(DEFAULT_SPECIALITE_PARTICULIER_YN)
            .specialitesPayanteYN(DEFAULT_SPECIALITES_PAYANTE_YN);
        return specialite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialite createUpdatedEntity(EntityManager em) {
        Specialite specialite = new Specialite()
            .nomSpecialites(UPDATED_NOM_SPECIALITES)
            .sigleSpecialites(UPDATED_SIGLE_SPECIALITES)
            .specialiteParticulierYN(UPDATED_SPECIALITE_PARTICULIER_YN)
            .specialitesPayanteYN(UPDATED_SPECIALITES_PAYANTE_YN);
        return specialite;
    }

    @BeforeEach
    public void initTest() {
        specialite = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecialite() throws Exception {
        int databaseSizeBeforeCreate = specialiteRepository.findAll().size();
        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);
        restSpecialiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeCreate + 1);
        Specialite testSpecialite = specialiteList.get(specialiteList.size() - 1);
        assertThat(testSpecialite.getNomSpecialites()).isEqualTo(DEFAULT_NOM_SPECIALITES);
        assertThat(testSpecialite.getSigleSpecialites()).isEqualTo(DEFAULT_SIGLE_SPECIALITES);
        assertThat(testSpecialite.getSpecialiteParticulierYN()).isEqualTo(DEFAULT_SPECIALITE_PARTICULIER_YN);
        assertThat(testSpecialite.getSpecialitesPayanteYN()).isEqualTo(DEFAULT_SPECIALITES_PAYANTE_YN);
    }

    @Test
    @Transactional
    void createSpecialiteWithExistingId() throws Exception {
        // Create the Specialite with an existing ID
        specialite.setId(1L);
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        int databaseSizeBeforeCreate = specialiteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomSpecialitesIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialiteRepository.findAll().size();
        // set the field null
        specialite.setNomSpecialites(null);

        // Create the Specialite, which fails.
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        restSpecialiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSigleSpecialitesIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialiteRepository.findAll().size();
        // set the field null
        specialite.setSigleSpecialites(null);

        // Create the Specialite, which fails.
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        restSpecialiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpecialitesPayanteYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialiteRepository.findAll().size();
        // set the field null
        specialite.setSpecialitesPayanteYN(null);

        // Create the Specialite, which fails.
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        restSpecialiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialites() throws Exception {
        // Initialize the database
        specialiteRepository.saveAndFlush(specialite);

        // Get all the specialiteList
        restSpecialiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomSpecialites").value(hasItem(DEFAULT_NOM_SPECIALITES)))
            .andExpect(jsonPath("$.[*].sigleSpecialites").value(hasItem(DEFAULT_SIGLE_SPECIALITES)))
            .andExpect(jsonPath("$.[*].specialiteParticulierYN").value(hasItem(DEFAULT_SPECIALITE_PARTICULIER_YN)))
            .andExpect(jsonPath("$.[*].specialitesPayanteYN").value(hasItem(DEFAULT_SPECIALITES_PAYANTE_YN)));
    }

    @Test
    @Transactional
    void getSpecialite() throws Exception {
        // Initialize the database
        specialiteRepository.saveAndFlush(specialite);

        // Get the specialite
        restSpecialiteMockMvc
            .perform(get(ENTITY_API_URL_ID, specialite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialite.getId().intValue()))
            .andExpect(jsonPath("$.nomSpecialites").value(DEFAULT_NOM_SPECIALITES))
            .andExpect(jsonPath("$.sigleSpecialites").value(DEFAULT_SIGLE_SPECIALITES))
            .andExpect(jsonPath("$.specialiteParticulierYN").value(DEFAULT_SPECIALITE_PARTICULIER_YN))
            .andExpect(jsonPath("$.specialitesPayanteYN").value(DEFAULT_SPECIALITES_PAYANTE_YN));
    }

    @Test
    @Transactional
    void getNonExistingSpecialite() throws Exception {
        // Get the specialite
        restSpecialiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialite() throws Exception {
        // Initialize the database
        specialiteRepository.saveAndFlush(specialite);

        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();

        // Update the specialite
        Specialite updatedSpecialite = specialiteRepository.findById(specialite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecialite are not directly saved in db
        em.detach(updatedSpecialite);
        updatedSpecialite
            .nomSpecialites(UPDATED_NOM_SPECIALITES)
            .sigleSpecialites(UPDATED_SIGLE_SPECIALITES)
            .specialiteParticulierYN(UPDATED_SPECIALITE_PARTICULIER_YN)
            .specialitesPayanteYN(UPDATED_SPECIALITES_PAYANTE_YN);
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(updatedSpecialite);

        restSpecialiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialiteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
        Specialite testSpecialite = specialiteList.get(specialiteList.size() - 1);
        assertThat(testSpecialite.getNomSpecialites()).isEqualTo(UPDATED_NOM_SPECIALITES);
        assertThat(testSpecialite.getSigleSpecialites()).isEqualTo(UPDATED_SIGLE_SPECIALITES);
        assertThat(testSpecialite.getSpecialiteParticulierYN()).isEqualTo(UPDATED_SPECIALITE_PARTICULIER_YN);
        assertThat(testSpecialite.getSpecialitesPayanteYN()).isEqualTo(UPDATED_SPECIALITES_PAYANTE_YN);
    }

    @Test
    @Transactional
    void putNonExistingSpecialite() throws Exception {
        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        specialite.setId(longCount.incrementAndGet());

        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialiteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialite() throws Exception {
        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        specialite.setId(longCount.incrementAndGet());

        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialite() throws Exception {
        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        specialite.setId(longCount.incrementAndGet());

        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialiteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialiteWithPatch() throws Exception {
        // Initialize the database
        specialiteRepository.saveAndFlush(specialite);

        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();

        // Update the specialite using partial update
        Specialite partialUpdatedSpecialite = new Specialite();
        partialUpdatedSpecialite.setId(specialite.getId());

        partialUpdatedSpecialite.nomSpecialites(UPDATED_NOM_SPECIALITES).specialitesPayanteYN(UPDATED_SPECIALITES_PAYANTE_YN);

        restSpecialiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialite.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialite))
            )
            .andExpect(status().isOk());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
        Specialite testSpecialite = specialiteList.get(specialiteList.size() - 1);
        assertThat(testSpecialite.getNomSpecialites()).isEqualTo(UPDATED_NOM_SPECIALITES);
        assertThat(testSpecialite.getSigleSpecialites()).isEqualTo(DEFAULT_SIGLE_SPECIALITES);
        assertThat(testSpecialite.getSpecialiteParticulierYN()).isEqualTo(DEFAULT_SPECIALITE_PARTICULIER_YN);
        assertThat(testSpecialite.getSpecialitesPayanteYN()).isEqualTo(UPDATED_SPECIALITES_PAYANTE_YN);
    }

    @Test
    @Transactional
    void fullUpdateSpecialiteWithPatch() throws Exception {
        // Initialize the database
        specialiteRepository.saveAndFlush(specialite);

        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();

        // Update the specialite using partial update
        Specialite partialUpdatedSpecialite = new Specialite();
        partialUpdatedSpecialite.setId(specialite.getId());

        partialUpdatedSpecialite
            .nomSpecialites(UPDATED_NOM_SPECIALITES)
            .sigleSpecialites(UPDATED_SIGLE_SPECIALITES)
            .specialiteParticulierYN(UPDATED_SPECIALITE_PARTICULIER_YN)
            .specialitesPayanteYN(UPDATED_SPECIALITES_PAYANTE_YN);

        restSpecialiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialite.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialite))
            )
            .andExpect(status().isOk());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
        Specialite testSpecialite = specialiteList.get(specialiteList.size() - 1);
        assertThat(testSpecialite.getNomSpecialites()).isEqualTo(UPDATED_NOM_SPECIALITES);
        assertThat(testSpecialite.getSigleSpecialites()).isEqualTo(UPDATED_SIGLE_SPECIALITES);
        assertThat(testSpecialite.getSpecialiteParticulierYN()).isEqualTo(UPDATED_SPECIALITE_PARTICULIER_YN);
        assertThat(testSpecialite.getSpecialitesPayanteYN()).isEqualTo(UPDATED_SPECIALITES_PAYANTE_YN);
    }

    @Test
    @Transactional
    void patchNonExistingSpecialite() throws Exception {
        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        specialite.setId(longCount.incrementAndGet());

        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialiteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialite() throws Exception {
        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        specialite.setId(longCount.incrementAndGet());

        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialite() throws Exception {
        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        specialite.setId(longCount.incrementAndGet());

        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialiteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialite() throws Exception {
        // Initialize the database
        specialiteRepository.saveAndFlush(specialite);

        int databaseSizeBeforeDelete = specialiteRepository.findAll().size();

        // Delete the specialite
        restSpecialiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialite.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
