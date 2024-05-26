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
import sn.ugb.gir.domain.Ufr;
import sn.ugb.gir.repository.UfrRepository;
import sn.ugb.gir.service.dto.UfrDTO;
import sn.ugb.gir.service.mapper.UfrMapper;

/**
 * Integration tests for the {@link UfrResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UfrResourceIT {

    private static final String DEFAULT_LIBELE_UFR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE_UFR = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLE_UFR = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE_UFR = "BBBBBBBBBB";

    private static final Integer DEFAULT_SYSTEME_LMDYN = 1;
    private static final Integer UPDATED_SYSTEME_LMDYN = 2;

    private static final Integer DEFAULT_ORDRE_STAT = 1;
    private static final Integer UPDATED_ORDRE_STAT = 2;

    private static final String ENTITY_API_URL = "/api/ufrs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UfrRepository ufrRepository;

    @Autowired
    private UfrMapper ufrMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUfrMockMvc;

    private Ufr ufr;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ufr createEntity(EntityManager em) {
        Ufr ufr = new Ufr()
            .libeleUfr(DEFAULT_LIBELE_UFR)
            .sigleUfr(DEFAULT_SIGLE_UFR)
            .systemeLMDYN(DEFAULT_SYSTEME_LMDYN)
            .ordreStat(DEFAULT_ORDRE_STAT);
        return ufr;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ufr createUpdatedEntity(EntityManager em) {
        Ufr ufr = new Ufr()
            .libeleUfr(UPDATED_LIBELE_UFR)
            .sigleUfr(UPDATED_SIGLE_UFR)
            .systemeLMDYN(UPDATED_SYSTEME_LMDYN)
            .ordreStat(UPDATED_ORDRE_STAT);
        return ufr;
    }

    @BeforeEach
    public void initTest() {
        ufr = createEntity(em);
    }

    @Test
    @Transactional
    void createUfr() throws Exception {
        int databaseSizeBeforeCreate = ufrRepository.findAll().size();
        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);
        restUfrMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeCreate + 1);
        Ufr testUfr = ufrList.get(ufrList.size() - 1);
        assertThat(testUfr.getLibeleUfr()).isEqualTo(DEFAULT_LIBELE_UFR);
        assertThat(testUfr.getSigleUfr()).isEqualTo(DEFAULT_SIGLE_UFR);
        assertThat(testUfr.getSystemeLMDYN()).isEqualTo(DEFAULT_SYSTEME_LMDYN);
        assertThat(testUfr.getOrdreStat()).isEqualTo(DEFAULT_ORDRE_STAT);
    }

    @Test
    @Transactional
    void createUfrWithExistingId() throws Exception {
        // Create the Ufr with an existing ID
        ufr.setId(1L);
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        int databaseSizeBeforeCreate = ufrRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUfrMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibeleUfrIsRequired() throws Exception {
        int databaseSizeBeforeTest = ufrRepository.findAll().size();
        // set the field null
        ufr.setLibeleUfr(null);

        // Create the Ufr, which fails.
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        restUfrMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSigleUfrIsRequired() throws Exception {
        int databaseSizeBeforeTest = ufrRepository.findAll().size();
        // set the field null
        ufr.setSigleUfr(null);

        // Create the Ufr, which fails.
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        restUfrMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSystemeLMDYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = ufrRepository.findAll().size();
        // set the field null
        ufr.setSystemeLMDYN(null);

        // Create the Ufr, which fails.
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        restUfrMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUfrs() throws Exception {
        // Initialize the database
        ufrRepository.saveAndFlush(ufr);

        // Get all the ufrList
        restUfrMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ufr.getId().intValue())))
            .andExpect(jsonPath("$.[*].libeleUfr").value(hasItem(DEFAULT_LIBELE_UFR)))
            .andExpect(jsonPath("$.[*].sigleUfr").value(hasItem(DEFAULT_SIGLE_UFR)))
            .andExpect(jsonPath("$.[*].systemeLMDYN").value(hasItem(DEFAULT_SYSTEME_LMDYN)))
            .andExpect(jsonPath("$.[*].ordreStat").value(hasItem(DEFAULT_ORDRE_STAT)));
    }

    @Test
    @Transactional
    void getUfr() throws Exception {
        // Initialize the database
        ufrRepository.saveAndFlush(ufr);

        // Get the ufr
        restUfrMockMvc
            .perform(get(ENTITY_API_URL_ID, ufr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ufr.getId().intValue()))
            .andExpect(jsonPath("$.libeleUfr").value(DEFAULT_LIBELE_UFR))
            .andExpect(jsonPath("$.sigleUfr").value(DEFAULT_SIGLE_UFR))
            .andExpect(jsonPath("$.systemeLMDYN").value(DEFAULT_SYSTEME_LMDYN))
            .andExpect(jsonPath("$.ordreStat").value(DEFAULT_ORDRE_STAT));
    }

    @Test
    @Transactional
    void getNonExistingUfr() throws Exception {
        // Get the ufr
        restUfrMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUfr() throws Exception {
        // Initialize the database
        ufrRepository.saveAndFlush(ufr);

        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();

        // Update the ufr
        Ufr updatedUfr = ufrRepository.findById(ufr.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUfr are not directly saved in db
        em.detach(updatedUfr);
        updatedUfr
            .libeleUfr(UPDATED_LIBELE_UFR)
            .sigleUfr(UPDATED_SIGLE_UFR)
            .systemeLMDYN(UPDATED_SYSTEME_LMDYN)
            .ordreStat(UPDATED_ORDRE_STAT);
        UfrDTO ufrDTO = ufrMapper.toDto(updatedUfr);

        restUfrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ufrDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
        Ufr testUfr = ufrList.get(ufrList.size() - 1);
        assertThat(testUfr.getLibeleUfr()).isEqualTo(UPDATED_LIBELE_UFR);
        assertThat(testUfr.getSigleUfr()).isEqualTo(UPDATED_SIGLE_UFR);
        assertThat(testUfr.getSystemeLMDYN()).isEqualTo(UPDATED_SYSTEME_LMDYN);
        assertThat(testUfr.getOrdreStat()).isEqualTo(UPDATED_ORDRE_STAT);
    }

    @Test
    @Transactional
    void putNonExistingUfr() throws Exception {
        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        ufr.setId(longCount.incrementAndGet());

        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUfrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ufrDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUfr() throws Exception {
        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        ufr.setId(longCount.incrementAndGet());

        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUfrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUfr() throws Exception {
        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        ufr.setId(longCount.incrementAndGet());

        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUfrMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUfrWithPatch() throws Exception {
        // Initialize the database
        ufrRepository.saveAndFlush(ufr);

        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();

        // Update the ufr using partial update
        Ufr partialUpdatedUfr = new Ufr();
        partialUpdatedUfr.setId(ufr.getId());

        partialUpdatedUfr.systemeLMDYN(UPDATED_SYSTEME_LMDYN).ordreStat(UPDATED_ORDRE_STAT);

        restUfrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUfr.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUfr))
            )
            .andExpect(status().isOk());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
        Ufr testUfr = ufrList.get(ufrList.size() - 1);
        assertThat(testUfr.getLibeleUfr()).isEqualTo(DEFAULT_LIBELE_UFR);
        assertThat(testUfr.getSigleUfr()).isEqualTo(DEFAULT_SIGLE_UFR);
        assertThat(testUfr.getSystemeLMDYN()).isEqualTo(UPDATED_SYSTEME_LMDYN);
        assertThat(testUfr.getOrdreStat()).isEqualTo(UPDATED_ORDRE_STAT);
    }

    @Test
    @Transactional
    void fullUpdateUfrWithPatch() throws Exception {
        // Initialize the database
        ufrRepository.saveAndFlush(ufr);

        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();

        // Update the ufr using partial update
        Ufr partialUpdatedUfr = new Ufr();
        partialUpdatedUfr.setId(ufr.getId());

        partialUpdatedUfr
            .libeleUfr(UPDATED_LIBELE_UFR)
            .sigleUfr(UPDATED_SIGLE_UFR)
            .systemeLMDYN(UPDATED_SYSTEME_LMDYN)
            .ordreStat(UPDATED_ORDRE_STAT);

        restUfrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUfr.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUfr))
            )
            .andExpect(status().isOk());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
        Ufr testUfr = ufrList.get(ufrList.size() - 1);
        assertThat(testUfr.getLibeleUfr()).isEqualTo(UPDATED_LIBELE_UFR);
        assertThat(testUfr.getSigleUfr()).isEqualTo(UPDATED_SIGLE_UFR);
        assertThat(testUfr.getSystemeLMDYN()).isEqualTo(UPDATED_SYSTEME_LMDYN);
        assertThat(testUfr.getOrdreStat()).isEqualTo(UPDATED_ORDRE_STAT);
    }

    @Test
    @Transactional
    void patchNonExistingUfr() throws Exception {
        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        ufr.setId(longCount.incrementAndGet());

        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUfrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ufrDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUfr() throws Exception {
        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        ufr.setId(longCount.incrementAndGet());

        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUfrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUfr() throws Exception {
        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        ufr.setId(longCount.incrementAndGet());

        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUfrMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUfr() throws Exception {
        // Initialize the database
        ufrRepository.saveAndFlush(ufr);

        int databaseSizeBeforeDelete = ufrRepository.findAll().size();

        // Delete the ufr
        restUfrMockMvc
            .perform(delete(ENTITY_API_URL_ID, ufr.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
