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
import sn.ugb.gir.domain.InformationImage;
import sn.ugb.gir.repository.InformationImageRepository;
import sn.ugb.gir.repository.search.InformationImageSearchRepository;
import sn.ugb.gir.service.dto.InformationImageDTO;
import sn.ugb.gir.service.mapper.InformationImageMapper;

/**
 * Integration tests for the {@link InformationImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InformationImageResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CHEMIN_PATH = "AAAAAAAAAA";
    private static final String UPDATED_CHEMIN_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_CHEMIN_FILE = "AAAAAAAAAA";
    private static final String UPDATED_CHEMIN_FILE = "BBBBBBBBBB";

    private static final String DEFAULT_EN_COURS_YN = "AAAAAAAAAA";
    private static final String UPDATED_EN_COURS_YN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/information-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/information-images/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InformationImageRepository informationImageRepository;

    @Autowired
    private InformationImageMapper informationImageMapper;

    @Autowired
    private InformationImageSearchRepository informationImageSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInformationImageMockMvc;

    private InformationImage informationImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InformationImage createEntity(EntityManager em) {
        InformationImage informationImage = new InformationImage()
            .description(DEFAULT_DESCRIPTION)
            .cheminPath(DEFAULT_CHEMIN_PATH)
            .cheminFile(DEFAULT_CHEMIN_FILE)
            .enCoursYN(DEFAULT_EN_COURS_YN);
        return informationImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InformationImage createUpdatedEntity(EntityManager em) {
        InformationImage informationImage = new InformationImage()
            .description(UPDATED_DESCRIPTION)
            .cheminPath(UPDATED_CHEMIN_PATH)
            .cheminFile(UPDATED_CHEMIN_FILE)
            .enCoursYN(UPDATED_EN_COURS_YN);
        return informationImage;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        informationImageSearchRepository.deleteAll();
        assertThat(informationImageSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        informationImage = createEntity(em);
    }

    @Test
    @Transactional
    void createInformationImage() throws Exception {
        int databaseSizeBeforeCreate = informationImageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        // Create the InformationImage
        InformationImageDTO informationImageDTO = informationImageMapper.toDto(informationImage);
        restInformationImageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationImageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InformationImage in the database
        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        InformationImage testInformationImage = informationImageList.get(informationImageList.size() - 1);
        assertThat(testInformationImage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInformationImage.getCheminPath()).isEqualTo(DEFAULT_CHEMIN_PATH);
        assertThat(testInformationImage.getCheminFile()).isEqualTo(DEFAULT_CHEMIN_FILE);
        assertThat(testInformationImage.getEnCoursYN()).isEqualTo(DEFAULT_EN_COURS_YN);
    }

    @Test
    @Transactional
    void createInformationImageWithExistingId() throws Exception {
        // Create the InformationImage with an existing ID
        informationImage.setId(1L);
        InformationImageDTO informationImageDTO = informationImageMapper.toDto(informationImage);

        int databaseSizeBeforeCreate = informationImageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informationImageSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restInformationImageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InformationImage in the database
        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCheminPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = informationImageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        // set the field null
        informationImage.setCheminPath(null);

        // Create the InformationImage, which fails.
        InformationImageDTO informationImageDTO = informationImageMapper.toDto(informationImage);

        restInformationImageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationImageDTO))
            )
            .andExpect(status().isBadRequest());

        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCheminFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = informationImageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        // set the field null
        informationImage.setCheminFile(null);

        // Create the InformationImage, which fails.
        InformationImageDTO informationImageDTO = informationImageMapper.toDto(informationImage);

        restInformationImageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationImageDTO))
            )
            .andExpect(status().isBadRequest());

        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllInformationImages() throws Exception {
        // Initialize the database
        informationImageRepository.saveAndFlush(informationImage);

        // Get all the informationImageList
        restInformationImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(informationImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cheminPath").value(hasItem(DEFAULT_CHEMIN_PATH)))
            .andExpect(jsonPath("$.[*].cheminFile").value(hasItem(DEFAULT_CHEMIN_FILE)))
            .andExpect(jsonPath("$.[*].enCoursYN").value(hasItem(DEFAULT_EN_COURS_YN)));
    }

    @Test
    @Transactional
    void getInformationImage() throws Exception {
        // Initialize the database
        informationImageRepository.saveAndFlush(informationImage);

        // Get the informationImage
        restInformationImageMockMvc
            .perform(get(ENTITY_API_URL_ID, informationImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(informationImage.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.cheminPath").value(DEFAULT_CHEMIN_PATH))
            .andExpect(jsonPath("$.cheminFile").value(DEFAULT_CHEMIN_FILE))
            .andExpect(jsonPath("$.enCoursYN").value(DEFAULT_EN_COURS_YN));
    }

    @Test
    @Transactional
    void getNonExistingInformationImage() throws Exception {
        // Get the informationImage
        restInformationImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInformationImage() throws Exception {
        // Initialize the database
        informationImageRepository.saveAndFlush(informationImage);

        int databaseSizeBeforeUpdate = informationImageRepository.findAll().size();
        informationImageSearchRepository.save(informationImage);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informationImageSearchRepository.findAll());

        // Update the informationImage
        InformationImage updatedInformationImage = informationImageRepository.findById(informationImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInformationImage are not directly saved in db
        em.detach(updatedInformationImage);
        updatedInformationImage
            .description(UPDATED_DESCRIPTION)
            .cheminPath(UPDATED_CHEMIN_PATH)
            .cheminFile(UPDATED_CHEMIN_FILE)
            .enCoursYN(UPDATED_EN_COURS_YN);
        InformationImageDTO informationImageDTO = informationImageMapper.toDto(updatedInformationImage);

        restInformationImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, informationImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the InformationImage in the database
        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeUpdate);
        InformationImage testInformationImage = informationImageList.get(informationImageList.size() - 1);
        assertThat(testInformationImage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInformationImage.getCheminPath()).isEqualTo(UPDATED_CHEMIN_PATH);
        assertThat(testInformationImage.getCheminFile()).isEqualTo(UPDATED_CHEMIN_FILE);
        assertThat(testInformationImage.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<InformationImage> informationImageSearchList = IterableUtils.toList(informationImageSearchRepository.findAll());
                InformationImage testInformationImageSearch = informationImageSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testInformationImageSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testInformationImageSearch.getCheminPath()).isEqualTo(UPDATED_CHEMIN_PATH);
                assertThat(testInformationImageSearch.getCheminFile()).isEqualTo(UPDATED_CHEMIN_FILE);
                assertThat(testInformationImageSearch.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingInformationImage() throws Exception {
        int databaseSizeBeforeUpdate = informationImageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        informationImage.setId(longCount.incrementAndGet());

        // Create the InformationImage
        InformationImageDTO informationImageDTO = informationImageMapper.toDto(informationImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformationImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, informationImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InformationImage in the database
        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchInformationImage() throws Exception {
        int databaseSizeBeforeUpdate = informationImageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        informationImage.setId(longCount.incrementAndGet());

        // Create the InformationImage
        InformationImageDTO informationImageDTO = informationImageMapper.toDto(informationImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InformationImage in the database
        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInformationImage() throws Exception {
        int databaseSizeBeforeUpdate = informationImageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        informationImage.setId(longCount.incrementAndGet());

        // Create the InformationImage
        InformationImageDTO informationImageDTO = informationImageMapper.toDto(informationImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationImageMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InformationImage in the database
        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateInformationImageWithPatch() throws Exception {
        // Initialize the database
        informationImageRepository.saveAndFlush(informationImage);

        int databaseSizeBeforeUpdate = informationImageRepository.findAll().size();

        // Update the informationImage using partial update
        InformationImage partialUpdatedInformationImage = new InformationImage();
        partialUpdatedInformationImage.setId(informationImage.getId());

        partialUpdatedInformationImage.enCoursYN(UPDATED_EN_COURS_YN);

        restInformationImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInformationImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInformationImage))
            )
            .andExpect(status().isOk());

        // Validate the InformationImage in the database
        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeUpdate);
        InformationImage testInformationImage = informationImageList.get(informationImageList.size() - 1);
        assertThat(testInformationImage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInformationImage.getCheminPath()).isEqualTo(DEFAULT_CHEMIN_PATH);
        assertThat(testInformationImage.getCheminFile()).isEqualTo(DEFAULT_CHEMIN_FILE);
        assertThat(testInformationImage.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
    }

    @Test
    @Transactional
    void fullUpdateInformationImageWithPatch() throws Exception {
        // Initialize the database
        informationImageRepository.saveAndFlush(informationImage);

        int databaseSizeBeforeUpdate = informationImageRepository.findAll().size();

        // Update the informationImage using partial update
        InformationImage partialUpdatedInformationImage = new InformationImage();
        partialUpdatedInformationImage.setId(informationImage.getId());

        partialUpdatedInformationImage
            .description(UPDATED_DESCRIPTION)
            .cheminPath(UPDATED_CHEMIN_PATH)
            .cheminFile(UPDATED_CHEMIN_FILE)
            .enCoursYN(UPDATED_EN_COURS_YN);

        restInformationImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInformationImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInformationImage))
            )
            .andExpect(status().isOk());

        // Validate the InformationImage in the database
        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeUpdate);
        InformationImage testInformationImage = informationImageList.get(informationImageList.size() - 1);
        assertThat(testInformationImage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInformationImage.getCheminPath()).isEqualTo(UPDATED_CHEMIN_PATH);
        assertThat(testInformationImage.getCheminFile()).isEqualTo(UPDATED_CHEMIN_FILE);
        assertThat(testInformationImage.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
    }

    @Test
    @Transactional
    void patchNonExistingInformationImage() throws Exception {
        int databaseSizeBeforeUpdate = informationImageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        informationImage.setId(longCount.incrementAndGet());

        // Create the InformationImage
        InformationImageDTO informationImageDTO = informationImageMapper.toDto(informationImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformationImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, informationImageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(informationImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InformationImage in the database
        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInformationImage() throws Exception {
        int databaseSizeBeforeUpdate = informationImageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        informationImage.setId(longCount.incrementAndGet());

        // Create the InformationImage
        InformationImageDTO informationImageDTO = informationImageMapper.toDto(informationImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(informationImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InformationImage in the database
        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInformationImage() throws Exception {
        int databaseSizeBeforeUpdate = informationImageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        informationImage.setId(longCount.incrementAndGet());

        // Create the InformationImage
        InformationImageDTO informationImageDTO = informationImageMapper.toDto(informationImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationImageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(informationImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InformationImage in the database
        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteInformationImage() throws Exception {
        // Initialize the database
        informationImageRepository.saveAndFlush(informationImage);
        informationImageRepository.save(informationImage);
        informationImageSearchRepository.save(informationImage);

        int databaseSizeBeforeDelete = informationImageRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the informationImage
        restInformationImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, informationImage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InformationImage> informationImageList = informationImageRepository.findAll();
        assertThat(informationImageList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informationImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchInformationImage() throws Exception {
        // Initialize the database
        informationImage = informationImageRepository.saveAndFlush(informationImage);
        informationImageSearchRepository.save(informationImage);

        // Search the informationImage
        restInformationImageMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + informationImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(informationImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cheminPath").value(hasItem(DEFAULT_CHEMIN_PATH)))
            .andExpect(jsonPath("$.[*].cheminFile").value(hasItem(DEFAULT_CHEMIN_FILE)))
            .andExpect(jsonPath("$.[*].enCoursYN").value(hasItem(DEFAULT_EN_COURS_YN)));
    }
}
