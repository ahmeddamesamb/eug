package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.IntegrationTest;
import sn.ugb.gir.domain.Mention;
import sn.ugb.gir.repository.MentionRepository;
import sn.ugb.gir.repository.search.MentionSearchRepository;
import sn.ugb.gir.service.dto.MentionDTO;
import sn.ugb.gir.service.mapper.MentionMapper;

/**
 * Integration tests for the {@link MentionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MentionResourceIT {

    private static final String DEFAULT_LIBELLE_MENTION = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_MENTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/mentions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/mentions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MentionRepository mentionRepository;

    @Autowired
    private MentionMapper mentionMapper;

    @Autowired
    private MentionSearchRepository mentionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMentionMockMvc;

    private Mention mention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mention createEntity(EntityManager em) {
        Mention mention = new Mention().libelleMention(DEFAULT_LIBELLE_MENTION).actifYN(DEFAULT_ACTIF_YN);
        return mention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mention createUpdatedEntity(EntityManager em) {
        Mention mention = new Mention().libelleMention(UPDATED_LIBELLE_MENTION).actifYN(UPDATED_ACTIF_YN);
        return mention;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        mentionSearchRepository.deleteAll();
        assertThat(mentionSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        mention = createEntity(em);
    }

    @Test
    @Transactional
    void createMention() throws Exception {
        int databaseSizeBeforeCreate = mentionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        // Create the Mention
        MentionDTO mentionDTO = mentionMapper.toDto(mention);
        restMentionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mentionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(mentionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Mention testMention = mentionList.get(mentionList.size() - 1);
        assertThat(testMention.getLibelleMention()).isEqualTo(DEFAULT_LIBELLE_MENTION);
        assertThat(testMention.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createMentionWithExistingId() throws Exception {
        // Create the Mention with an existing ID
        mention.setId(1L);
        MentionDTO mentionDTO = mentionMapper.toDto(mention);

        int databaseSizeBeforeCreate = mentionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(mentionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMentionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLibelleMentionIsRequired() throws Exception {
        int databaseSizeBeforeTest = mentionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        // set the field null
        mention.setLibelleMention(null);

        // Create the Mention, which fails.
        MentionDTO mentionDTO = mentionMapper.toDto(mention);

        restMentionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mentionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMentions() throws Exception {
        // Initialize the database
        mentionRepository.saveAndFlush(mention);

        // Get all the mentionList
        restMentionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mention.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleMention").value(hasItem(DEFAULT_LIBELLE_MENTION)))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @Test
    @Transactional
    void getMention() throws Exception {
        // Initialize the database
        mentionRepository.saveAndFlush(mention);

        // Get the mention
        restMentionMockMvc
            .perform(get(ENTITY_API_URL_ID, mention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mention.getId().intValue()))
            .andExpect(jsonPath("$.libelleMention").value(DEFAULT_LIBELLE_MENTION))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMention() throws Exception {
        // Get the mention
        restMentionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMention() throws Exception {
        // Initialize the database
        mentionRepository.saveAndFlush(mention);

        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        mentionSearchRepository.save(mention);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(mentionSearchRepository.findAll());

        // Update the mention
        Mention updatedMention = mentionRepository.findById(mention.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMention are not directly saved in db
        em.detach(updatedMention);
        updatedMention.libelleMention(UPDATED_LIBELLE_MENTION).actifYN(UPDATED_ACTIF_YN);
        MentionDTO mentionDTO = mentionMapper.toDto(updatedMention);

        restMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mentionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mentionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
        Mention testMention = mentionList.get(mentionList.size() - 1);
        assertThat(testMention.getLibelleMention()).isEqualTo(UPDATED_LIBELLE_MENTION);
        assertThat(testMention.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(mentionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Mention> mentionSearchList = IterableUtils.toList(mentionSearchRepository.findAll());
                Mention testMentionSearch = mentionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testMentionSearch.getLibelleMention()).isEqualTo(UPDATED_LIBELLE_MENTION);
                assertThat(testMentionSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingMention() throws Exception {
        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        mention.setId(longCount.incrementAndGet());

        // Create the Mention
        MentionDTO mentionDTO = mentionMapper.toDto(mention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mentionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMention() throws Exception {
        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        mention.setId(longCount.incrementAndGet());

        // Create the Mention
        MentionDTO mentionDTO = mentionMapper.toDto(mention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMention() throws Exception {
        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        mention.setId(longCount.incrementAndGet());

        // Create the Mention
        MentionDTO mentionDTO = mentionMapper.toDto(mention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mentionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMentionWithPatch() throws Exception {
        // Initialize the database
        mentionRepository.saveAndFlush(mention);

        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();

        // Update the mention using partial update
        Mention partialUpdatedMention = new Mention();
        partialUpdatedMention.setId(mention.getId());

        partialUpdatedMention.actifYN(UPDATED_ACTIF_YN);

        restMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMention.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMention))
            )
            .andExpect(status().isOk());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
        Mention testMention = mentionList.get(mentionList.size() - 1);
        assertThat(testMention.getLibelleMention()).isEqualTo(DEFAULT_LIBELLE_MENTION);
        assertThat(testMention.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateMentionWithPatch() throws Exception {
        // Initialize the database
        mentionRepository.saveAndFlush(mention);

        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();

        // Update the mention using partial update
        Mention partialUpdatedMention = new Mention();
        partialUpdatedMention.setId(mention.getId());

        partialUpdatedMention.libelleMention(UPDATED_LIBELLE_MENTION).actifYN(UPDATED_ACTIF_YN);

        restMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMention.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMention))
            )
            .andExpect(status().isOk());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
        Mention testMention = mentionList.get(mentionList.size() - 1);
        assertThat(testMention.getLibelleMention()).isEqualTo(UPDATED_LIBELLE_MENTION);
        assertThat(testMention.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingMention() throws Exception {
        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        mention.setId(longCount.incrementAndGet());

        // Create the Mention
        MentionDTO mentionDTO = mentionMapper.toDto(mention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mentionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMention() throws Exception {
        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        mention.setId(longCount.incrementAndGet());

        // Create the Mention
        MentionDTO mentionDTO = mentionMapper.toDto(mention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMention() throws Exception {
        int databaseSizeBeforeUpdate = mentionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        mention.setId(longCount.incrementAndGet());

        // Create the Mention
        MentionDTO mentionDTO = mentionMapper.toDto(mention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mentionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mention in the database
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMention() throws Exception {
        // Initialize the database
        mentionRepository.saveAndFlush(mention);
        mentionRepository.save(mention);
        mentionSearchRepository.save(mention);

        int databaseSizeBeforeDelete = mentionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the mention
        restMentionMockMvc
            .perform(delete(ENTITY_API_URL_ID, mention.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mention> mentionList = mentionRepository.findAll();
        assertThat(mentionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(mentionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMention() throws Exception {
        // Initialize the database
        mention = mentionRepository.saveAndFlush(mention);
        mentionSearchRepository.save(mention);

        // Search the mention
        restMentionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + mention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mention.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleMention").value(hasItem(DEFAULT_LIBELLE_MENTION)))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
