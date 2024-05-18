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
import sn.ugb.gir.domain.Domaine;
import sn.ugb.gir.repository.DomaineRepository;
import sn.ugb.gir.service.dto.DomaineDTO;
import sn.ugb.gir.service.mapper.DomaineMapper;

/**
 * Integration tests for the {@link DomaineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DomaineResourceIT {

    private static final String DEFAULT_LIBELLE_DOMAINE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_DOMAINE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/domaines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DomaineRepository domaineRepository;

    @Autowired
    private DomaineMapper domaineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDomaineMockMvc;

    private Domaine domaine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domaine createEntity(EntityManager em) {
        Domaine domaine = new Domaine().libelleDomaine(DEFAULT_LIBELLE_DOMAINE);
        return domaine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domaine createUpdatedEntity(EntityManager em) {
        Domaine domaine = new Domaine().libelleDomaine(UPDATED_LIBELLE_DOMAINE);
        return domaine;
    }

    @BeforeEach
    public void initTest() {
        domaine = createEntity(em);
    }

    @Test
    @Transactional
    void createDomaine() throws Exception {
        int databaseSizeBeforeCreate = domaineRepository.findAll().size();
        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);
        restDomaineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domaineDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeCreate + 1);
        Domaine testDomaine = domaineList.get(domaineList.size() - 1);
        assertThat(testDomaine.getLibelleDomaine()).isEqualTo(DEFAULT_LIBELLE_DOMAINE);
    }

    @Test
    @Transactional
    void createDomaineWithExistingId() throws Exception {
        // Create the Domaine with an existing ID
        domaine.setId(1L);
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        int databaseSizeBeforeCreate = domaineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomaineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domaineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDomaines() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList
        restDomaineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domaine.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleDomaine").value(hasItem(DEFAULT_LIBELLE_DOMAINE)));
    }

    @Test
    @Transactional
    void getDomaine() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get the domaine
        restDomaineMockMvc
            .perform(get(ENTITY_API_URL_ID, domaine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(domaine.getId().intValue()))
            .andExpect(jsonPath("$.libelleDomaine").value(DEFAULT_LIBELLE_DOMAINE));
    }

    @Test
    @Transactional
    void getNonExistingDomaine() throws Exception {
        // Get the domaine
        restDomaineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDomaine() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();

        // Update the domaine
        Domaine updatedDomaine = domaineRepository.findById(domaine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDomaine are not directly saved in db
        em.detach(updatedDomaine);
        updatedDomaine.libelleDomaine(UPDATED_LIBELLE_DOMAINE);
        DomaineDTO domaineDTO = domaineMapper.toDto(updatedDomaine);

        restDomaineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, domaineDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domaineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
        Domaine testDomaine = domaineList.get(domaineList.size() - 1);
        assertThat(testDomaine.getLibelleDomaine()).isEqualTo(UPDATED_LIBELLE_DOMAINE);
    }

    @Test
    @Transactional
    void putNonExistingDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();
        domaine.setId(longCount.incrementAndGet());

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, domaineDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domaineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();
        domaine.setId(longCount.incrementAndGet());

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domaineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();
        domaine.setId(longCount.incrementAndGet());

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domaineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDomaineWithPatch() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();

        // Update the domaine using partial update
        Domaine partialUpdatedDomaine = new Domaine();
        partialUpdatedDomaine.setId(domaine.getId());

        partialUpdatedDomaine.libelleDomaine(UPDATED_LIBELLE_DOMAINE);

        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomaine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomaine))
            )
            .andExpect(status().isOk());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
        Domaine testDomaine = domaineList.get(domaineList.size() - 1);
        assertThat(testDomaine.getLibelleDomaine()).isEqualTo(UPDATED_LIBELLE_DOMAINE);
    }

    @Test
    @Transactional
    void fullUpdateDomaineWithPatch() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();

        // Update the domaine using partial update
        Domaine partialUpdatedDomaine = new Domaine();
        partialUpdatedDomaine.setId(domaine.getId());

        partialUpdatedDomaine.libelleDomaine(UPDATED_LIBELLE_DOMAINE);

        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomaine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomaine))
            )
            .andExpect(status().isOk());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
        Domaine testDomaine = domaineList.get(domaineList.size() - 1);
        assertThat(testDomaine.getLibelleDomaine()).isEqualTo(UPDATED_LIBELLE_DOMAINE);
    }

    @Test
    @Transactional
    void patchNonExistingDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();
        domaine.setId(longCount.incrementAndGet());

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, domaineDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domaineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();
        domaine.setId(longCount.incrementAndGet());

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domaineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();
        domaine.setId(longCount.incrementAndGet());

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domaineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDomaine() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        int databaseSizeBeforeDelete = domaineRepository.findAll().size();

        // Delete the domaine
        restDomaineMockMvc
            .perform(delete(ENTITY_API_URL_ID, domaine.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
