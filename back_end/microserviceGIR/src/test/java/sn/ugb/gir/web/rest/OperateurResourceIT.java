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
import sn.ugb.gir.domain.Operateur;
import sn.ugb.gir.repository.OperateurRepository;
import sn.ugb.gir.service.dto.OperateurDTO;
import sn.ugb.gir.service.mapper.OperateurMapper;

/**
 * Integration tests for the {@link OperateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperateurResourceIT {

    private static final String DEFAULT_LIBELLE_OPERATEUR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_OPERATEUR = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_OPERATEUR = "AAAAAAAAAA";
    private static final String UPDATED_CODE_OPERATEUR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperateurRepository operateurRepository;

    @Autowired
    private OperateurMapper operateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperateurMockMvc;

    private Operateur operateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operateur createEntity(EntityManager em) {
        Operateur operateur = new Operateur()
            .libelleOperateur(DEFAULT_LIBELLE_OPERATEUR)
            .userLogin(DEFAULT_USER_LOGIN)
            .codeOperateur(DEFAULT_CODE_OPERATEUR);
        return operateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operateur createUpdatedEntity(EntityManager em) {
        Operateur operateur = new Operateur()
            .libelleOperateur(UPDATED_LIBELLE_OPERATEUR)
            .userLogin(UPDATED_USER_LOGIN)
            .codeOperateur(UPDATED_CODE_OPERATEUR);
        return operateur;
    }

    @BeforeEach
    public void initTest() {
        operateur = createEntity(em);
    }

    @Test
    @Transactional
    void createOperateur() throws Exception {
        int databaseSizeBeforeCreate = operateurRepository.findAll().size();
        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);
        restOperateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateurDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeCreate + 1);
        Operateur testOperateur = operateurList.get(operateurList.size() - 1);
        assertThat(testOperateur.getLibelleOperateur()).isEqualTo(DEFAULT_LIBELLE_OPERATEUR);
        assertThat(testOperateur.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testOperateur.getCodeOperateur()).isEqualTo(DEFAULT_CODE_OPERATEUR);
    }

    @Test
    @Transactional
    void createOperateurWithExistingId() throws Exception {
        // Create the Operateur with an existing ID
        operateur.setId(1L);
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        int databaseSizeBeforeCreate = operateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleOperateurIsRequired() throws Exception {
        int databaseSizeBeforeTest = operateurRepository.findAll().size();
        // set the field null
        operateur.setLibelleOperateur(null);

        // Create the Operateur, which fails.
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        restOperateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateurDTO))
            )
            .andExpect(status().isBadRequest());

        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = operateurRepository.findAll().size();
        // set the field null
        operateur.setUserLogin(null);

        // Create the Operateur, which fails.
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        restOperateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateurDTO))
            )
            .andExpect(status().isBadRequest());

        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeOperateurIsRequired() throws Exception {
        int databaseSizeBeforeTest = operateurRepository.findAll().size();
        // set the field null
        operateur.setCodeOperateur(null);

        // Create the Operateur, which fails.
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        restOperateurMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateurDTO))
            )
            .andExpect(status().isBadRequest());

        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOperateurs() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList
        restOperateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleOperateur").value(hasItem(DEFAULT_LIBELLE_OPERATEUR)))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].codeOperateur").value(hasItem(DEFAULT_CODE_OPERATEUR)));
    }

    @Test
    @Transactional
    void getOperateur() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        // Get the operateur
        restOperateurMockMvc
            .perform(get(ENTITY_API_URL_ID, operateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operateur.getId().intValue()))
            .andExpect(jsonPath("$.libelleOperateur").value(DEFAULT_LIBELLE_OPERATEUR))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN))
            .andExpect(jsonPath("$.codeOperateur").value(DEFAULT_CODE_OPERATEUR));
    }

    @Test
    @Transactional
    void getNonExistingOperateur() throws Exception {
        // Get the operateur
        restOperateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOperateur() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();

        // Update the operateur
        Operateur updatedOperateur = operateurRepository.findById(operateur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOperateur are not directly saved in db
        em.detach(updatedOperateur);
        updatedOperateur.libelleOperateur(UPDATED_LIBELLE_OPERATEUR).userLogin(UPDATED_USER_LOGIN).codeOperateur(UPDATED_CODE_OPERATEUR);
        OperateurDTO operateurDTO = operateurMapper.toDto(updatedOperateur);

        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operateurDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
        Operateur testOperateur = operateurList.get(operateurList.size() - 1);
        assertThat(testOperateur.getLibelleOperateur()).isEqualTo(UPDATED_LIBELLE_OPERATEUR);
        assertThat(testOperateur.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testOperateur.getCodeOperateur()).isEqualTo(UPDATED_CODE_OPERATEUR);
    }

    @Test
    @Transactional
    void putNonExistingOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(longCount.incrementAndGet());

        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operateurDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(longCount.incrementAndGet());

        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(longCount.incrementAndGet());

        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperateurWithPatch() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();

        // Update the operateur using partial update
        Operateur partialUpdatedOperateur = new Operateur();
        partialUpdatedOperateur.setId(operateur.getId());

        partialUpdatedOperateur.libelleOperateur(UPDATED_LIBELLE_OPERATEUR).codeOperateur(UPDATED_CODE_OPERATEUR);

        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperateur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperateur))
            )
            .andExpect(status().isOk());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
        Operateur testOperateur = operateurList.get(operateurList.size() - 1);
        assertThat(testOperateur.getLibelleOperateur()).isEqualTo(UPDATED_LIBELLE_OPERATEUR);
        assertThat(testOperateur.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testOperateur.getCodeOperateur()).isEqualTo(UPDATED_CODE_OPERATEUR);
    }

    @Test
    @Transactional
    void fullUpdateOperateurWithPatch() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();

        // Update the operateur using partial update
        Operateur partialUpdatedOperateur = new Operateur();
        partialUpdatedOperateur.setId(operateur.getId());

        partialUpdatedOperateur
            .libelleOperateur(UPDATED_LIBELLE_OPERATEUR)
            .userLogin(UPDATED_USER_LOGIN)
            .codeOperateur(UPDATED_CODE_OPERATEUR);

        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperateur.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperateur))
            )
            .andExpect(status().isOk());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
        Operateur testOperateur = operateurList.get(operateurList.size() - 1);
        assertThat(testOperateur.getLibelleOperateur()).isEqualTo(UPDATED_LIBELLE_OPERATEUR);
        assertThat(testOperateur.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testOperateur.getCodeOperateur()).isEqualTo(UPDATED_CODE_OPERATEUR);
    }

    @Test
    @Transactional
    void patchNonExistingOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(longCount.incrementAndGet());

        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operateurDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(longCount.incrementAndGet());

        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperateur() throws Exception {
        int databaseSizeBeforeUpdate = operateurRepository.findAll().size();
        operateur.setId(longCount.incrementAndGet());

        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operateur in the database
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperateur() throws Exception {
        // Initialize the database
        operateurRepository.saveAndFlush(operateur);

        int databaseSizeBeforeDelete = operateurRepository.findAll().size();

        // Delete the operateur
        restOperateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, operateur.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operateur> operateurList = operateurRepository.findAll();
        assertThat(operateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
