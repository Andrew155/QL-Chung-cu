package org.qlcc.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.qlcc.app.domain.VehicleTableAsserts.*;
import static org.qlcc.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.qlcc.app.IntegrationTest;
import org.qlcc.app.domain.VehicleTable;
import org.qlcc.app.repository.VehicleTableRepository;
import org.qlcc.app.service.VehicleTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VehicleTableResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VehicleTableResourceIT {

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VEHICLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VEHICLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VEHICLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ROOM_ID = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_ID = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_ID = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_VEHICLE_FEE = 1L;
    private static final Long UPDATED_VEHICLE_FEE = 2L;

    private static final String ENTITY_API_URL = "/api/vehicle-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleTableRepository vehicleTableRepository;

    @Mock
    private VehicleTableRepository vehicleTableRepositoryMock;

    @Mock
    private VehicleTableService vehicleTableServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleTableMockMvc;

    private VehicleTable vehicleTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleTable createEntity(EntityManager em) {
        VehicleTable vehicleTable = new VehicleTable()
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT)
            .deletedAt(DEFAULT_DELETED_AT)
            .vehicleName(DEFAULT_VEHICLE_NAME)
            .vehicleType(DEFAULT_VEHICLE_TYPE)
            .vehicleId(DEFAULT_VEHICLE_ID)
            .roomId(DEFAULT_ROOM_ID)
            .ownerId(DEFAULT_OWNER_ID)
            .vehicleFee(DEFAULT_VEHICLE_FEE);
        return vehicleTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleTable createUpdatedEntity(EntityManager em) {
        VehicleTable vehicleTable = new VehicleTable()
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .vehicleName(UPDATED_VEHICLE_NAME)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .vehicleId(UPDATED_VEHICLE_ID)
            .roomId(UPDATED_ROOM_ID)
            .ownerId(UPDATED_OWNER_ID)
            .vehicleFee(UPDATED_VEHICLE_FEE);
        return vehicleTable;
    }

    @BeforeEach
    public void initTest() {
        vehicleTable = createEntity(em);
    }

    @Test
    @Transactional
    void createVehicleTable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleTable
        var returnedVehicleTable = om.readValue(
            restVehicleTableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleTable)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleTable.class
        );

        // Validate the VehicleTable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVehicleTableUpdatableFieldsEquals(returnedVehicleTable, getPersistedVehicleTable(returnedVehicleTable));
    }

    @Test
    @Transactional
    void createVehicleTableWithExistingId() throws Exception {
        // Create the VehicleTable with an existing ID
        vehicleTable.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleTable)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleTable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVehicleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleTable.setVehicleId(null);

        // Create the VehicleTable, which fails.

        restVehicleTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleTable)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleTables() throws Exception {
        // Initialize the database
        vehicleTableRepository.saveAndFlush(vehicleTable);

        // Get all the vehicleTableList
        restVehicleTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].vehicleName").value(hasItem(DEFAULT_VEHICLE_NAME)))
            .andExpect(jsonPath("$.[*].vehicleType").value(hasItem(DEFAULT_VEHICLE_TYPE)))
            .andExpect(jsonPath("$.[*].vehicleId").value(hasItem(DEFAULT_VEHICLE_ID)))
            .andExpect(jsonPath("$.[*].roomId").value(hasItem(DEFAULT_ROOM_ID)))
            .andExpect(jsonPath("$.[*].ownerId").value(hasItem(DEFAULT_OWNER_ID)))
            .andExpect(jsonPath("$.[*].vehicleFee").value(hasItem(DEFAULT_VEHICLE_FEE.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehicleTablesWithEagerRelationshipsIsEnabled() throws Exception {
        when(vehicleTableServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehicleTableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vehicleTableServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehicleTablesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vehicleTableServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehicleTableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vehicleTableRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVehicleTable() throws Exception {
        // Initialize the database
        vehicleTableRepository.saveAndFlush(vehicleTable);

        // Get the vehicleTable
        restVehicleTableMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleTable.getId().intValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.vehicleName").value(DEFAULT_VEHICLE_NAME))
            .andExpect(jsonPath("$.vehicleType").value(DEFAULT_VEHICLE_TYPE))
            .andExpect(jsonPath("$.vehicleId").value(DEFAULT_VEHICLE_ID))
            .andExpect(jsonPath("$.roomId").value(DEFAULT_ROOM_ID))
            .andExpect(jsonPath("$.ownerId").value(DEFAULT_OWNER_ID))
            .andExpect(jsonPath("$.vehicleFee").value(DEFAULT_VEHICLE_FEE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingVehicleTable() throws Exception {
        // Get the vehicleTable
        restVehicleTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleTable() throws Exception {
        // Initialize the database
        vehicleTableRepository.saveAndFlush(vehicleTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleTable
        VehicleTable updatedVehicleTable = vehicleTableRepository.findById(vehicleTable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleTable are not directly saved in db
        em.detach(updatedVehicleTable);
        updatedVehicleTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .vehicleName(UPDATED_VEHICLE_NAME)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .vehicleId(UPDATED_VEHICLE_ID)
            .roomId(UPDATED_ROOM_ID)
            .ownerId(UPDATED_OWNER_ID)
            .vehicleFee(UPDATED_VEHICLE_FEE);

        restVehicleTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicleTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVehicleTable))
            )
            .andExpect(status().isOk());

        // Validate the VehicleTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleTableToMatchAllProperties(updatedVehicleTable);
    }

    @Test
    @Transactional
    void putNonExistingVehicleTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleTableWithPatch() throws Exception {
        // Initialize the database
        vehicleTableRepository.saveAndFlush(vehicleTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleTable using partial update
        VehicleTable partialUpdatedVehicleTable = new VehicleTable();
        partialUpdatedVehicleTable.setId(vehicleTable.getId());

        partialUpdatedVehicleTable.createAt(UPDATED_CREATE_AT).vehicleId(UPDATED_VEHICLE_ID).roomId(UPDATED_ROOM_ID);

        restVehicleTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleTable))
            )
            .andExpect(status().isOk());

        // Validate the VehicleTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleTableUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleTable, vehicleTable),
            getPersistedVehicleTable(vehicleTable)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleTableWithPatch() throws Exception {
        // Initialize the database
        vehicleTableRepository.saveAndFlush(vehicleTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleTable using partial update
        VehicleTable partialUpdatedVehicleTable = new VehicleTable();
        partialUpdatedVehicleTable.setId(vehicleTable.getId());

        partialUpdatedVehicleTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .vehicleName(UPDATED_VEHICLE_NAME)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .vehicleId(UPDATED_VEHICLE_ID)
            .roomId(UPDATED_ROOM_ID)
            .ownerId(UPDATED_OWNER_ID)
            .vehicleFee(UPDATED_VEHICLE_FEE);

        restVehicleTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleTable))
            )
            .andExpect(status().isOk());

        // Validate the VehicleTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleTableUpdatableFieldsEquals(partialUpdatedVehicleTable, getPersistedVehicleTable(partialUpdatedVehicleTable));
    }

    @Test
    @Transactional
    void patchNonExistingVehicleTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleTableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleTable() throws Exception {
        // Initialize the database
        vehicleTableRepository.saveAndFlush(vehicleTable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleTable
        restVehicleTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleTableRepository.count();
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

    protected VehicleTable getPersistedVehicleTable(VehicleTable vehicleTable) {
        return vehicleTableRepository.findById(vehicleTable.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleTableToMatchAllProperties(VehicleTable expectedVehicleTable) {
        assertVehicleTableAllPropertiesEquals(expectedVehicleTable, getPersistedVehicleTable(expectedVehicleTable));
    }

    protected void assertPersistedVehicleTableToMatchUpdatableProperties(VehicleTable expectedVehicleTable) {
        assertVehicleTableAllUpdatablePropertiesEquals(expectedVehicleTable, getPersistedVehicleTable(expectedVehicleTable));
    }
}
