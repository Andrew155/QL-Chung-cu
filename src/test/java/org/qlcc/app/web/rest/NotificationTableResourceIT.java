package org.qlcc.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.qlcc.app.domain.NotificationTableAsserts.*;
import static org.qlcc.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.qlcc.app.IntegrationTest;
import org.qlcc.app.domain.NotificationTable;
import org.qlcc.app.repository.NotificationTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NotificationTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationTableResourceIT {

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/notification-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationTableRepository notificationTableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationTableMockMvc;

    private NotificationTable notificationTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationTable createEntity(EntityManager em) {
        NotificationTable notificationTable = new NotificationTable()
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT)
            .deletedAt(DEFAULT_DELETED_AT)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .userID(DEFAULT_USER_ID);
        return notificationTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationTable createUpdatedEntity(EntityManager em) {
        NotificationTable notificationTable = new NotificationTable()
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .userID(UPDATED_USER_ID);
        return notificationTable;
    }

    @BeforeEach
    public void initTest() {
        notificationTable = createEntity(em);
    }

    @Test
    @Transactional
    void createNotificationTable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NotificationTable
        var returnedNotificationTable = om.readValue(
            restNotificationTableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTable)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificationTable.class
        );

        // Validate the NotificationTable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNotificationTableUpdatableFieldsEquals(returnedNotificationTable, getPersistedNotificationTable(returnedNotificationTable));
    }

    @Test
    @Transactional
    void createNotificationTableWithExistingId() throws Exception {
        // Create the NotificationTable with an existing ID
        notificationTable.setId(1);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTable)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNotificationTables() throws Exception {
        // Initialize the database
        notificationTableRepository.saveAndFlush(notificationTable);

        // Get all the notificationTableList
        restNotificationTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].userID").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    void getNotificationTable() throws Exception {
        // Initialize the database
        notificationTableRepository.saveAndFlush(notificationTable);

        // Get the notificationTable
        restNotificationTableMockMvc
            .perform(get(ENTITY_API_URL_ID, notificationTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationTable.getId().intValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.userID").value(DEFAULT_USER_ID));
    }

    @Test
    @Transactional
    void getNonExistingNotificationTable() throws Exception {
        // Get the notificationTable
        restNotificationTableMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificationTable() throws Exception {
        // Initialize the database
        notificationTableRepository.saveAndFlush(notificationTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationTable
        NotificationTable updatedNotificationTable = notificationTableRepository.findById(notificationTable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNotificationTable are not directly saved in db
        em.detach(updatedNotificationTable);
        updatedNotificationTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .userID(UPDATED_USER_ID);

        restNotificationTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNotificationTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNotificationTable))
            )
            .andExpect(status().isOk());

        // Validate the NotificationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationTableToMatchAllProperties(updatedNotificationTable);
    }

    @Test
    @Transactional
    void putNonExistingNotificationTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTable.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificationTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTable.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificationTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTable.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationTableWithPatch() throws Exception {
        // Initialize the database
        notificationTableRepository.saveAndFlush(notificationTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationTable using partial update
        NotificationTable partialUpdatedNotificationTable = new NotificationTable();
        partialUpdatedNotificationTable.setId(notificationTable.getId());

        partialUpdatedNotificationTable.updateAt(UPDATED_UPDATE_AT).content(UPDATED_CONTENT);

        restNotificationTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationTable))
            )
            .andExpect(status().isOk());

        // Validate the NotificationTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationTableUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificationTable, notificationTable),
            getPersistedNotificationTable(notificationTable)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificationTableWithPatch() throws Exception {
        // Initialize the database
        notificationTableRepository.saveAndFlush(notificationTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationTable using partial update
        NotificationTable partialUpdatedNotificationTable = new NotificationTable();
        partialUpdatedNotificationTable.setId(notificationTable.getId());

        partialUpdatedNotificationTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .userID(UPDATED_USER_ID);

        restNotificationTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationTable))
            )
            .andExpect(status().isOk());

        // Validate the NotificationTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationTableUpdatableFieldsEquals(
            partialUpdatedNotificationTable,
            getPersistedNotificationTable(partialUpdatedNotificationTable)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNotificationTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTable.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificationTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTable.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificationTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTable.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationTableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificationTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificationTable() throws Exception {
        // Initialize the database
        notificationTableRepository.saveAndFlush(notificationTable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificationTable
        restNotificationTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificationTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationTableRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected NotificationTable getPersistedNotificationTable(NotificationTable notificationTable) {
        return notificationTableRepository.findById(notificationTable.getId()).orElseThrow();
    }

    protected void assertPersistedNotificationTableToMatchAllProperties(NotificationTable expectedNotificationTable) {
        assertNotificationTableAllPropertiesEquals(expectedNotificationTable, getPersistedNotificationTable(expectedNotificationTable));
    }

    protected void assertPersistedNotificationTableToMatchUpdatableProperties(NotificationTable expectedNotificationTable) {
        assertNotificationTableAllUpdatablePropertiesEquals(
            expectedNotificationTable,
            getPersistedNotificationTable(expectedNotificationTable)
        );
    }
}
