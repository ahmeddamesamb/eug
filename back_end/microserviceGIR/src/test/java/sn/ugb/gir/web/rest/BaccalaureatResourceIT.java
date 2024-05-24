package sn.ugb.gir.web.rest;

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
import sn.ugb.gir.IntegrationTest;
import sn.ugb.gir.domain.Baccalaureat;
import sn.ugb.gir.repository.BaccalaureatRepository;
import sn.ugb.gir.service.dto.BaccalaureatDTO;
import sn.ugb.gir.service.mapper.BaccalaureatMapper;

/**
 * Integration tests for the {@link BaccalaureatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BaccalaureatResourceIT {

    private static final String DEFAULT_ORIGINE_SCOLAIRE = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINE_SCOLAIRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ANNEE_BAC = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANNEE_BAC = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NUMERO_TABLE = 1;
    private static final Integer UPDATED_NUMERO_TABLE = 2;

    private static final String DEFAULT_NATURE_BAC = "AAAAAAAAAA";
    private static final String UPDATED_NATURE_BAC = "BBBBBBBBBB";

    private static final String DEFAULT_MENTION_BAC = "AAAAAAAAAA";
    private static final String UPDATED_MENTION_BAC = "BBBBBBBBBB";

    private static final Float DEFAULT_MOYENNE_SELECTION_BAC = 1F;
    private static final Float UPDATED_MOYENNE_SELECTION_BAC = 2F;

    private static final Float DEFAULT_MOYENNE_BAC = 1F;
    private static final Float UPDATED_MOYENNE_BAC = 2F;

    private static final String ENTITY_API_URL = "/api/baccalaureats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BaccalaureatRepository baccalaureatRepository;

    @Autowired
    private BaccalaureatMapper baccalaureatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBaccalaureatMockMvc;

    private Baccalaureat baccalaureat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Baccalaureat createEntity(EntityManager em) {
        Baccalaureat baccalaureat = new Baccalaureat()
            .origineScolaire(DEFAULT_ORIGINE_SCOLAIRE)
            .anneeBac(DEFAULT_ANNEE_BAC)
            .numeroTable(DEFAULT_NUMERO_TABLE)
            .natureBac(DEFAULT_NATURE_BAC)
            .mentionBac(DEFAULT_MENTION_BAC)
            .moyenneSelectionBac(DEFAULT_MOYENNE_SELECTION_BAC)
            .moyenneBac(DEFAULT_MOYENNE_BAC);
        return baccalaureat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Baccalaureat createUpdatedEntity(EntityManager em) {
        Baccalaureat baccalaureat = new Baccalaureat()
            .origineScolaire(UPDATED_ORIGINE_SCOLAIRE)
            .anneeBac(UPDATED_ANNEE_BAC)
            .numeroTable(UPDATED_NUMERO_TABLE)
            .natureBac(UPDATED_NATURE_BAC)
            .mentionBac(UPDATED_MENTION_BAC)
            .moyenneSelectionBac(UPDATED_MOYENNE_SELECTION_BAC)
            .moyenneBac(UPDATED_MOYENNE_BAC);
        return baccalaureat;
    }

    @BeforeEach
    public void initTest() {
        baccalaureat = createEntity(em);
    }

    @Test
    @Transactional
    void createBaccalaureat() throws Exception {
        int databaseSizeBeforeCreate = baccalaureatRepository.findAll().size();
        // Create the Baccalaureat
        BaccalaureatDTO baccalaureatDTO = baccalaureatMapper.toDto(baccalaureat);
        restBaccalaureatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baccalaureatDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Baccalaureat in the database
        List<Baccalaureat> baccalaureatList = baccalaureatRepository.findAll();
        assertThat(baccalaureatList).hasSize(databaseSizeBeforeCreate + 1);
        Baccalaureat testBaccalaureat = baccalaureatList.get(baccalaureatList.size() - 1);
        assertThat(testBaccalaureat.getOrigineScolaire()).isEqualTo(DEFAULT_ORIGINE_SCOLAIRE);
        assertThat(testBaccalaureat.getAnneeBac()).isEqualTo(DEFAULT_ANNEE_BAC);
        assertThat(testBaccalaureat.getNumeroTable()).isEqualTo(DEFAULT_NUMERO_TABLE);
        assertThat(testBaccalaureat.getNatureBac()).isEqualTo(DEFAULT_NATURE_BAC);
        assertThat(testBaccalaureat.getMentionBac()).isEqualTo(DEFAULT_MENTION_BAC);
        assertThat(testBaccalaureat.getMoyenneSelectionBac()).isEqualTo(DEFAULT_MOYENNE_SELECTION_BAC);
        assertThat(testBaccalaureat.getMoyenneBac()).isEqualTo(DEFAULT_MOYENNE_BAC);
    }

    @Test
    @Transactional
    void createBaccalaureatWithExistingId() throws Exception {
        // Create the Baccalaureat with an existing ID
        baccalaureat.setId(1L);
        BaccalaureatDTO baccalaureatDTO = baccalaureatMapper.toDto(baccalaureat);

        int databaseSizeBeforeCreate = baccalaureatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaccalaureatMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baccalaureatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Baccalaureat in the database
        List<Baccalaureat> baccalaureatList = baccalaureatRepository.findAll();
        assertThat(baccalaureatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBaccalaureats() throws Exception {
        // Initialize the database
        baccalaureatRepository.saveAndFlush(baccalaureat);

        // Get all the baccalaureatList
        restBaccalaureatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(baccalaureat.getId().intValue())))
            .andExpect(jsonPath("$.[*].origineScolaire").value(hasItem(DEFAULT_ORIGINE_SCOLAIRE)))
            .andExpect(jsonPath("$.[*].anneeBac").value(hasItem(DEFAULT_ANNEE_BAC.toString())))
            .andExpect(jsonPath("$.[*].numeroTable").value(hasItem(DEFAULT_NUMERO_TABLE)))
            .andExpect(jsonPath("$.[*].natureBac").value(hasItem(DEFAULT_NATURE_BAC)))
            .andExpect(jsonPath("$.[*].mentionBac").value(hasItem(DEFAULT_MENTION_BAC)))
            .andExpect(jsonPath("$.[*].moyenneSelectionBac").value(hasItem(DEFAULT_MOYENNE_SELECTION_BAC.doubleValue())))
            .andExpect(jsonPath("$.[*].moyenneBac").value(hasItem(DEFAULT_MOYENNE_BAC.doubleValue())));
    }

    @Test
    @Transactional
    void getBaccalaureat() throws Exception {
        // Initialize the database
        baccalaureatRepository.saveAndFlush(baccalaureat);

        // Get the baccalaureat
        restBaccalaureatMockMvc
            .perform(get(ENTITY_API_URL_ID, baccalaureat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(baccalaureat.getId().intValue()))
            .andExpect(jsonPath("$.origineScolaire").value(DEFAULT_ORIGINE_SCOLAIRE))
            .andExpect(jsonPath("$.anneeBac").value(DEFAULT_ANNEE_BAC.toString()))
            .andExpect(jsonPath("$.numeroTable").value(DEFAULT_NUMERO_TABLE))
            .andExpect(jsonPath("$.natureBac").value(DEFAULT_NATURE_BAC))
            .andExpect(jsonPath("$.mentionBac").value(DEFAULT_MENTION_BAC))
            .andExpect(jsonPath("$.moyenneSelectionBac").value(DEFAULT_MOYENNE_SELECTION_BAC.doubleValue()))
            .andExpect(jsonPath("$.moyenneBac").value(DEFAULT_MOYENNE_BAC.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingBaccalaureat() throws Exception {
        // Get the baccalaureat
        restBaccalaureatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBaccalaureat() throws Exception {
        // Initialize the database
        baccalaureatRepository.saveAndFlush(baccalaureat);

        int databaseSizeBeforeUpdate = baccalaureatRepository.findAll().size();

        // Update the baccalaureat
        Baccalaureat updatedBaccalaureat = baccalaureatRepository.findById(baccalaureat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBaccalaureat are not directly saved in db
        em.detach(updatedBaccalaureat);
        updatedBaccalaureat
            .origineScolaire(UPDATED_ORIGINE_SCOLAIRE)
            .anneeBac(UPDATED_ANNEE_BAC)
            .numeroTable(UPDATED_NUMERO_TABLE)
            .natureBac(UPDATED_NATURE_BAC)
            .mentionBac(UPDATED_MENTION_BAC)
            .moyenneSelectionBac(UPDATED_MOYENNE_SELECTION_BAC)
            .moyenneBac(UPDATED_MOYENNE_BAC);
        BaccalaureatDTO baccalaureatDTO = baccalaureatMapper.toDto(updatedBaccalaureat);

        restBaccalaureatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baccalaureatDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baccalaureatDTO))
            )
            .andExpect(status().isOk());

        // Validate the Baccalaureat in the database
        List<Baccalaureat> baccalaureatList = baccalaureatRepository.findAll();
        assertThat(baccalaureatList).hasSize(databaseSizeBeforeUpdate);
        Baccalaureat testBaccalaureat = baccalaureatList.get(baccalaureatList.size() - 1);
        assertThat(testBaccalaureat.getOrigineScolaire()).isEqualTo(UPDATED_ORIGINE_SCOLAIRE);
        assertThat(testBaccalaureat.getAnneeBac()).isEqualTo(UPDATED_ANNEE_BAC);
        assertThat(testBaccalaureat.getNumeroTable()).isEqualTo(UPDATED_NUMERO_TABLE);
        assertThat(testBaccalaureat.getNatureBac()).isEqualTo(UPDATED_NATURE_BAC);
        assertThat(testBaccalaureat.getMentionBac()).isEqualTo(UPDATED_MENTION_BAC);
        assertThat(testBaccalaureat.getMoyenneSelectionBac()).isEqualTo(UPDATED_MOYENNE_SELECTION_BAC);
        assertThat(testBaccalaureat.getMoyenneBac()).isEqualTo(UPDATED_MOYENNE_BAC);
    }

    @Test
    @Transactional
    void putNonExistingBaccalaureat() throws Exception {
        int databaseSizeBeforeUpdate = baccalaureatRepository.findAll().size();
        baccalaureat.setId(longCount.incrementAndGet());

        // Create the Baccalaureat
        BaccalaureatDTO baccalaureatDTO = baccalaureatMapper.toDto(baccalaureat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaccalaureatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baccalaureatDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baccalaureatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Baccalaureat in the database
        List<Baccalaureat> baccalaureatList = baccalaureatRepository.findAll();
        assertThat(baccalaureatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBaccalaureat() throws Exception {
        int databaseSizeBeforeUpdate = baccalaureatRepository.findAll().size();
        baccalaureat.setId(longCount.incrementAndGet());

        // Create the Baccalaureat
        BaccalaureatDTO baccalaureatDTO = baccalaureatMapper.toDto(baccalaureat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaccalaureatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baccalaureatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Baccalaureat in the database
        List<Baccalaureat> baccalaureatList = baccalaureatRepository.findAll();
        assertThat(baccalaureatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBaccalaureat() throws Exception {
        int databaseSizeBeforeUpdate = baccalaureatRepository.findAll().size();
        baccalaureat.setId(longCount.incrementAndGet());

        // Create the Baccalaureat
        BaccalaureatDTO baccalaureatDTO = baccalaureatMapper.toDto(baccalaureat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaccalaureatMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baccalaureatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Baccalaureat in the database
        List<Baccalaureat> baccalaureatList = baccalaureatRepository.findAll();
        assertThat(baccalaureatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBaccalaureatWithPatch() throws Exception {
        // Initialize the database
        baccalaureatRepository.saveAndFlush(baccalaureat);

        int databaseSizeBeforeUpdate = baccalaureatRepository.findAll().size();

        // Update the baccalaureat using partial update
        Baccalaureat partialUpdatedBaccalaureat = new Baccalaureat();
        partialUpdatedBaccalaureat.setId(baccalaureat.getId());

        partialUpdatedBaccalaureat.anneeBac(UPDATED_ANNEE_BAC).moyenneBac(UPDATED_MOYENNE_BAC);

        restBaccalaureatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaccalaureat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBaccalaureat))
            )
            .andExpect(status().isOk());

        // Validate the Baccalaureat in the database
        List<Baccalaureat> baccalaureatList = baccalaureatRepository.findAll();
        assertThat(baccalaureatList).hasSize(databaseSizeBeforeUpdate);
        Baccalaureat testBaccalaureat = baccalaureatList.get(baccalaureatList.size() - 1);
        assertThat(testBaccalaureat.getOrigineScolaire()).isEqualTo(DEFAULT_ORIGINE_SCOLAIRE);
        assertThat(testBaccalaureat.getAnneeBac()).isEqualTo(UPDATED_ANNEE_BAC);
        assertThat(testBaccalaureat.getNumeroTable()).isEqualTo(DEFAULT_NUMERO_TABLE);
        assertThat(testBaccalaureat.getNatureBac()).isEqualTo(DEFAULT_NATURE_BAC);
        assertThat(testBaccalaureat.getMentionBac()).isEqualTo(DEFAULT_MENTION_BAC);
        assertThat(testBaccalaureat.getMoyenneSelectionBac()).isEqualTo(DEFAULT_MOYENNE_SELECTION_BAC);
        assertThat(testBaccalaureat.getMoyenneBac()).isEqualTo(UPDATED_MOYENNE_BAC);
    }

    @Test
    @Transactional
    void fullUpdateBaccalaureatWithPatch() throws Exception {
        // Initialize the database
        baccalaureatRepository.saveAndFlush(baccalaureat);

        int databaseSizeBeforeUpdate = baccalaureatRepository.findAll().size();

        // Update the baccalaureat using partial update
        Baccalaureat partialUpdatedBaccalaureat = new Baccalaureat();
        partialUpdatedBaccalaureat.setId(baccalaureat.getId());

        partialUpdatedBaccalaureat
            .origineScolaire(UPDATED_ORIGINE_SCOLAIRE)
            .anneeBac(UPDATED_ANNEE_BAC)
            .numeroTable(UPDATED_NUMERO_TABLE)
            .natureBac(UPDATED_NATURE_BAC)
            .mentionBac(UPDATED_MENTION_BAC)
            .moyenneSelectionBac(UPDATED_MOYENNE_SELECTION_BAC)
            .moyenneBac(UPDATED_MOYENNE_BAC);

        restBaccalaureatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaccalaureat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBaccalaureat))
            )
            .andExpect(status().isOk());

        // Validate the Baccalaureat in the database
        List<Baccalaureat> baccalaureatList = baccalaureatRepository.findAll();
        assertThat(baccalaureatList).hasSize(databaseSizeBeforeUpdate);
        Baccalaureat testBaccalaureat = baccalaureatList.get(baccalaureatList.size() - 1);
        assertThat(testBaccalaureat.getOrigineScolaire()).isEqualTo(UPDATED_ORIGINE_SCOLAIRE);
        assertThat(testBaccalaureat.getAnneeBac()).isEqualTo(UPDATED_ANNEE_BAC);
        assertThat(testBaccalaureat.getNumeroTable()).isEqualTo(UPDATED_NUMERO_TABLE);
        assertThat(testBaccalaureat.getNatureBac()).isEqualTo(UPDATED_NATURE_BAC);
        assertThat(testBaccalaureat.getMentionBac()).isEqualTo(UPDATED_MENTION_BAC);
        assertThat(testBaccalaureat.getMoyenneSelectionBac()).isEqualTo(UPDATED_MOYENNE_SELECTION_BAC);
        assertThat(testBaccalaureat.getMoyenneBac()).isEqualTo(UPDATED_MOYENNE_BAC);
    }

    @Test
    @Transactional
    void patchNonExistingBaccalaureat() throws Exception {
        int databaseSizeBeforeUpdate = baccalaureatRepository.findAll().size();
        baccalaureat.setId(longCount.incrementAndGet());

        // Create the Baccalaureat
        BaccalaureatDTO baccalaureatDTO = baccalaureatMapper.toDto(baccalaureat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaccalaureatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, baccalaureatDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baccalaureatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Baccalaureat in the database
        List<Baccalaureat> baccalaureatList = baccalaureatRepository.findAll();
        assertThat(baccalaureatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBaccalaureat() throws Exception {
        int databaseSizeBeforeUpdate = baccalaureatRepository.findAll().size();
        baccalaureat.setId(longCount.incrementAndGet());

        // Create the Baccalaureat
        BaccalaureatDTO baccalaureatDTO = baccalaureatMapper.toDto(baccalaureat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaccalaureatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baccalaureatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Baccalaureat in the database
        List<Baccalaureat> baccalaureatList = baccalaureatRepository.findAll();
        assertThat(baccalaureatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBaccalaureat() throws Exception {
        int databaseSizeBeforeUpdate = baccalaureatRepository.findAll().size();
        baccalaureat.setId(longCount.incrementAndGet());

        // Create the Baccalaureat
        BaccalaureatDTO baccalaureatDTO = baccalaureatMapper.toDto(baccalaureat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaccalaureatMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baccalaureatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Baccalaureat in the database
        List<Baccalaureat> baccalaureatList = baccalaureatRepository.findAll();
        assertThat(baccalaureatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBaccalaureat() throws Exception {
        // Initialize the database
        baccalaureatRepository.saveAndFlush(baccalaureat);

        int databaseSizeBeforeDelete = baccalaureatRepository.findAll().size();

        // Delete the baccalaureat
        restBaccalaureatMockMvc
            .perform(delete(ENTITY_API_URL_ID, baccalaureat.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Baccalaureat> baccalaureatList = baccalaureatRepository.findAll();
        assertThat(baccalaureatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
