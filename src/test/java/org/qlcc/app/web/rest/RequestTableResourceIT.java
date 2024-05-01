package org.qlcc.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.qlcc.app.domain.RequestTableAsserts.*;
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
import org.qlcc.app.domain.RequestTable;
import org.qlcc.app.repository.RequestTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RequestTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequestTableResourceIT {

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_REPLY = "AAAAAAAAAA";
    private static final String UPDATED_REPLY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/request-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RequestTableRepository requestTableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequestTableMockMvc;

    private RequestTable requestTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestTable createEntity(EntityManager em) {
        RequestTable requestTable = new RequestTable()
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT)
            .deletedAt(DEFAULT_DELETED_AT)
            .status(DEFAULT_STATUS)
            .userId(DEFAULT_USER_ID)
            .title(DEFAULT_TITLE)
            .message(DEFAULT_MESSAGE)
            .reply(DEFAULT_REPLY)
            .note(DEFAULT_NOTE);
        return requestTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestTable createUpdatedEntity(EntityManager em) {
        RequestTable requestTable = new RequestTable()
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .status(UPDATED_STATUS)
            .userId(UPDATED_USER_ID)
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .reply(UPDATED_REPLY)
            .note(UPDATED_NOTE);
        return requestTable;
    }

    @BeforeEach
    public void initTest() {
        requestTable = createEntity(em);
    }

    @Test
    @Transactional
    void createRequestTable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RequestTable
        var returnedRequestTable = om.readValue(
            restRequestTableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestTable)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RequestTable.class
        );

        // Validate the RequestTable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRequestTableUpdatableFieldsEquals(returnedRequestTable, getPersistedRequestTable(returnedRequestTable));
    }

    @Test
    @Transactional
    void createRequestTableWithExistingId() throws Exception {
        // Create the RequestTable with an existing ID
        requestTable.setId(1);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestTable)))
            .andExpect(status().isBadRequest());

        // Validate the RequestTable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRequestTables() throws Exception {
        // Initialize the database
        requestTableRepository.saveAndFlush(requestTable);

        // Get all the requestTableList
        restRequestTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].reply").value(hasItem(DEFAULT_REPLY)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getRequestTable() throws Exception {
        // Initialize the database
        requestTableRepository.saveAndFlush(requestTable);

        // Get the requestTable
        restRequestTableMockMvc
            .perform(get(ENTITY_API_URL_ID, requestTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requestTable.getId().intValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.reply").value(DEFAULT_REPLY))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingRequestTable() throws Exception {
        // Get the requestTable
        restRequestTableMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRequestTable() throws Exception {
        // Initialize the database
        requestTableRepository.saveAndFlush(requestTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the requestTable
        RequestTable updatedRequestTable = requestTableRepository.findById(requestTable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRequestTable are not directly saved in db
        em.detach(updatedRequestTable);
        updatedRequestTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .status(UPDATED_STATUS)
            .userId(UPDATED_USER_ID)
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .reply(UPDATED_REPLY)
            .note(UPDATED_NOTE);

        restRequestTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRequestTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRequestTable))
            )
            .andExpect(status().isOk());

        // Validate the RequestTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRequestTableToMatchAllProperties(updatedRequestTable);
    }

    @Test
    @Transactional
    void putNonExistingRequestTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requestTable.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requestTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(requestTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRequestTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requestTable.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(requestTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRequestTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requestTable.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RequestTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRequestTableWithPatch() throws Exception {
        // Initialize the database
        requestTableRepository.saveAndFlush(requestTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the requestTable using partial update
        RequestTable partialUpdatedRequestTable = new RequestTable();
        partialUpdatedRequestTable.setId(requestTable.getId());

        partialUpdatedRequestTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .userId(UPDATED_USER_ID)
            .message(UPDATED_MESSAGE)
            .note(UPDATED_NOTE);

        restRequestTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequestTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRequestTable))
            )
            .andExpect(status().isOk());

        // Validate the RequestTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRequestTableUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRequestTable, requestTable),
            getPersistedRequestTable(requestTable)
        );
    }

    @Test
    @Transactional
    void fullUpdateRequestTableWithPatch() throws Exception {
        // Initialize the database
        requestTableRepository.saveAndFlush(requestTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the requestTable using partial update
        RequestTable partialUpdatedRequestTable = new RequestTable();
        partialUpdatedRequestTable.setId(requestTable.getId());

        partialUpdatedRequestTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .status(UPDATED_STATUS)
            .userId(UPDATED_USER_ID)
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .reply(UPDATED_REPLY)
            .note(UPDATED_NOTE);

        restRequestTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequestTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRequestTable))
            )
            .andExpect(status().isOk());

        // Validate the RequestTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRequestTableUpdatableFieldsEquals(partialUpdatedRequestTable, getPersistedRequestTable(partialUpdatedRequestTable));
    }

    @Test
    @Transactional
    void patchNonExistingRequestTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requestTable.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, requestTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(requestTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRequestTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requestTable.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(requestTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRequestTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        requestTable.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestTableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(requestTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RequestTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRequestTable() throws Exception {
        // Initialize the database
        requestTableRepository.saveAndFlush(requestTable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the requestTable
        restRequestTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, requestTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return requestTableRepository.count();
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

    protected RequestTable getPersistedRequestTable(RequestTable requestTable) {
        return requestTableRepository.findById(requestTable.getId()).orElseThrow();
    }

    protected void assertPersistedRequestTableToMatchAllProperties(RequestTable expectedRequestTable) {
        assertRequestTableAllPropertiesEquals(expectedRequestTable, getPersistedRequestTable(expectedRequestTable));
    }

    protected void assertPersistedRequestTableToMatchUpdatableProperties(RequestTable expectedRequestTable) {
        assertRequestTableAllUpdatablePropertiesEquals(expectedRequestTable, getPersistedRequestTable(expectedRequestTable));
    }
}
