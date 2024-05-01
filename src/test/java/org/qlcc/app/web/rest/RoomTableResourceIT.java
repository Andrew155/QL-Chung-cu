package org.qlcc.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.qlcc.app.domain.RoomTableAsserts.*;
import static org.qlcc.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.qlcc.app.IntegrationTest;
import org.qlcc.app.domain.RoomTable;
import org.qlcc.app.repository.RoomTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RoomTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomTableResourceIT {

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ROOM_ID = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_ID = "BBBBBBBBBB";

    private static final String DEFAULT_AREA = "AAAAAAAAAA";
    private static final String UPDATED_AREA = "BBBBBBBBBB";

    private static final String DEFAULT_OWN_TIME = "AAAAAAAAAA";
    private static final String UPDATED_OWN_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_ID = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/room-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoomTableRepository roomTableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomTableMockMvc;

    private RoomTable roomTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomTable createEntity(EntityManager em) {
        RoomTable roomTable = new RoomTable()
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT)
            .deletedAt(DEFAULT_DELETED_AT)
            .roomId(DEFAULT_ROOM_ID)
            .area(DEFAULT_AREA)
            .ownTime(DEFAULT_OWN_TIME)
            .ownerId(DEFAULT_OWNER_ID)
            .ownerName(DEFAULT_OWNER_NAME)
            .status(DEFAULT_STATUS);
        return roomTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomTable createUpdatedEntity(EntityManager em) {
        RoomTable roomTable = new RoomTable()
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .roomId(UPDATED_ROOM_ID)
            .area(UPDATED_AREA)
            .ownTime(UPDATED_OWN_TIME)
            .ownerId(UPDATED_OWNER_ID)
            .ownerName(UPDATED_OWNER_NAME)
            .status(UPDATED_STATUS);
        return roomTable;
    }

    @BeforeEach
    public void initTest() {
        roomTable = createEntity(em);
    }

    @Test
    @Transactional
    void createRoomTable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RoomTable
        var returnedRoomTable = om.readValue(
            restRoomTableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomTable)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RoomTable.class
        );

        // Validate the RoomTable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRoomTableUpdatableFieldsEquals(returnedRoomTable, getPersistedRoomTable(returnedRoomTable));
    }

    @Test
    @Transactional
    void createRoomTableWithExistingId() throws Exception {
        // Create the RoomTable with an existing ID
        roomTable.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomTable)))
            .andExpect(status().isBadRequest());

        // Validate the RoomTable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoomIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        roomTable.setRoomId(null);

        // Create the RoomTable, which fails.

        restRoomTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomTable)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoomTables() throws Exception {
        // Initialize the database
        roomTableRepository.saveAndFlush(roomTable);

        // Get all the roomTableList
        restRoomTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].roomId").value(hasItem(DEFAULT_ROOM_ID)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
            .andExpect(jsonPath("$.[*].ownTime").value(hasItem(DEFAULT_OWN_TIME)))
            .andExpect(jsonPath("$.[*].ownerId").value(hasItem(DEFAULT_OWNER_ID)))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getRoomTable() throws Exception {
        // Initialize the database
        roomTableRepository.saveAndFlush(roomTable);

        // Get the roomTable
        restRoomTableMockMvc
            .perform(get(ENTITY_API_URL_ID, roomTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomTable.getId().intValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.roomId").value(DEFAULT_ROOM_ID))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA))
            .andExpect(jsonPath("$.ownTime").value(DEFAULT_OWN_TIME))
            .andExpect(jsonPath("$.ownerId").value(DEFAULT_OWNER_ID))
            .andExpect(jsonPath("$.ownerName").value(DEFAULT_OWNER_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingRoomTable() throws Exception {
        // Get the roomTable
        restRoomTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoomTable() throws Exception {
        // Initialize the database
        roomTableRepository.saveAndFlush(roomTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roomTable
        RoomTable updatedRoomTable = roomTableRepository.findById(roomTable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoomTable are not directly saved in db
        em.detach(updatedRoomTable);
        updatedRoomTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .roomId(UPDATED_ROOM_ID)
            .area(UPDATED_AREA)
            .ownTime(UPDATED_OWN_TIME)
            .ownerId(UPDATED_OWNER_ID)
            .ownerName(UPDATED_OWNER_NAME)
            .status(UPDATED_STATUS);

        restRoomTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoomTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRoomTable))
            )
            .andExpect(status().isOk());

        // Validate the RoomTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoomTableToMatchAllProperties(updatedRoomTable);
    }

    @Test
    @Transactional
    void putNonExistingRoomTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomTable.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoomTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roomTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoomTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roomTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomTableWithPatch() throws Exception {
        // Initialize the database
        roomTableRepository.saveAndFlush(roomTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roomTable using partial update
        RoomTable partialUpdatedRoomTable = new RoomTable();
        partialUpdatedRoomTable.setId(roomTable.getId());

        partialUpdatedRoomTable
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .roomId(UPDATED_ROOM_ID)
            .area(UPDATED_AREA)
            .ownTime(UPDATED_OWN_TIME);

        restRoomTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoomTable))
            )
            .andExpect(status().isOk());

        // Validate the RoomTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoomTableUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRoomTable, roomTable),
            getPersistedRoomTable(roomTable)
        );
    }

    @Test
    @Transactional
    void fullUpdateRoomTableWithPatch() throws Exception {
        // Initialize the database
        roomTableRepository.saveAndFlush(roomTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roomTable using partial update
        RoomTable partialUpdatedRoomTable = new RoomTable();
        partialUpdatedRoomTable.setId(roomTable.getId());

        partialUpdatedRoomTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .roomId(UPDATED_ROOM_ID)
            .area(UPDATED_AREA)
            .ownTime(UPDATED_OWN_TIME)
            .ownerId(UPDATED_OWNER_ID)
            .ownerName(UPDATED_OWNER_NAME)
            .status(UPDATED_STATUS);

        restRoomTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoomTable))
            )
            .andExpect(status().isOk());

        // Validate the RoomTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoomTableUpdatableFieldsEquals(partialUpdatedRoomTable, getPersistedRoomTable(partialUpdatedRoomTable));
    }

    @Test
    @Transactional
    void patchNonExistingRoomTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roomTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoomTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roomTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoomTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roomTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomTableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roomTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoomTable() throws Exception {
        // Initialize the database
        roomTableRepository.saveAndFlush(roomTable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the roomTable
        restRoomTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, roomTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roomTableRepository.count();
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

    protected RoomTable getPersistedRoomTable(RoomTable roomTable) {
        return roomTableRepository.findById(roomTable.getId()).orElseThrow();
    }

    protected void assertPersistedRoomTableToMatchAllProperties(RoomTable expectedRoomTable) {
        assertRoomTableAllPropertiesEquals(expectedRoomTable, getPersistedRoomTable(expectedRoomTable));
    }

    protected void assertPersistedRoomTableToMatchUpdatableProperties(RoomTable expectedRoomTable) {
        assertRoomTableAllUpdatablePropertiesEquals(expectedRoomTable, getPersistedRoomTable(expectedRoomTable));
    }
}
