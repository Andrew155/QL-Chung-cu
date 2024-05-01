package org.qlcc.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.qlcc.app.domain.DonationTableAsserts.*;
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
import org.qlcc.app.domain.DonationTable;
import org.qlcc.app.repository.DonationTableRepository;
import org.qlcc.app.service.DonationTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DonationTableResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DonationTableResourceIT {

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DONATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_DONATION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DONATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DONATION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DONATION_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DONATION_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_DONATION_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_DONATION_MONTH = "BBBBBBBBBB";

    private static final Long DEFAULT_DONATION_COST = 1L;
    private static final Long UPDATED_DONATION_COST = 2L;

    private static final String DEFAULT_ROOM_ID = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/donation-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DonationTableRepository donationTableRepository;

    @Mock
    private DonationTableRepository donationTableRepositoryMock;

    @Mock
    private DonationTableService donationTableServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDonationTableMockMvc;

    private DonationTable donationTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DonationTable createEntity(EntityManager em) {
        DonationTable donationTable = new DonationTable()
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT)
            .deletedAt(DEFAULT_DELETED_AT)
            .donationId(DEFAULT_DONATION_ID)
            .donationType(DEFAULT_DONATION_TYPE)
            .donationDesc(DEFAULT_DONATION_DESC)
            .donationMonth(DEFAULT_DONATION_MONTH)
            .donationCost(DEFAULT_DONATION_COST)
            .roomId(DEFAULT_ROOM_ID)
            .status(DEFAULT_STATUS);
        return donationTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DonationTable createUpdatedEntity(EntityManager em) {
        DonationTable donationTable = new DonationTable()
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .donationId(UPDATED_DONATION_ID)
            .donationType(UPDATED_DONATION_TYPE)
            .donationDesc(UPDATED_DONATION_DESC)
            .donationMonth(UPDATED_DONATION_MONTH)
            .donationCost(UPDATED_DONATION_COST)
            .roomId(UPDATED_ROOM_ID)
            .status(UPDATED_STATUS);
        return donationTable;
    }

    @BeforeEach
    public void initTest() {
        donationTable = createEntity(em);
    }

    @Test
    @Transactional
    void createDonationTable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DonationTable
        var returnedDonationTable = om.readValue(
            restDonationTableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(donationTable)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DonationTable.class
        );

        // Validate the DonationTable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDonationTableUpdatableFieldsEquals(returnedDonationTable, getPersistedDonationTable(returnedDonationTable));
    }

    @Test
    @Transactional
    void createDonationTableWithExistingId() throws Exception {
        // Create the DonationTable with an existing ID
        donationTable.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonationTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(donationTable)))
            .andExpect(status().isBadRequest());

        // Validate the DonationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDonationIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        donationTable.setDonationId(null);

        // Create the DonationTable, which fails.

        restDonationTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(donationTable)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDonationTables() throws Exception {
        // Initialize the database
        donationTableRepository.saveAndFlush(donationTable);

        // Get all the donationTableList
        restDonationTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donationTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].donationId").value(hasItem(DEFAULT_DONATION_ID)))
            .andExpect(jsonPath("$.[*].donationType").value(hasItem(DEFAULT_DONATION_TYPE)))
            .andExpect(jsonPath("$.[*].donationDesc").value(hasItem(DEFAULT_DONATION_DESC)))
            .andExpect(jsonPath("$.[*].donationMonth").value(hasItem(DEFAULT_DONATION_MONTH)))
            .andExpect(jsonPath("$.[*].donationCost").value(hasItem(DEFAULT_DONATION_COST.intValue())))
            .andExpect(jsonPath("$.[*].roomId").value(hasItem(DEFAULT_ROOM_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDonationTablesWithEagerRelationshipsIsEnabled() throws Exception {
        when(donationTableServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDonationTableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(donationTableServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDonationTablesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(donationTableServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDonationTableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(donationTableRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDonationTable() throws Exception {
        // Initialize the database
        donationTableRepository.saveAndFlush(donationTable);

        // Get the donationTable
        restDonationTableMockMvc
            .perform(get(ENTITY_API_URL_ID, donationTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(donationTable.getId().intValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.donationId").value(DEFAULT_DONATION_ID))
            .andExpect(jsonPath("$.donationType").value(DEFAULT_DONATION_TYPE))
            .andExpect(jsonPath("$.donationDesc").value(DEFAULT_DONATION_DESC))
            .andExpect(jsonPath("$.donationMonth").value(DEFAULT_DONATION_MONTH))
            .andExpect(jsonPath("$.donationCost").value(DEFAULT_DONATION_COST.intValue()))
            .andExpect(jsonPath("$.roomId").value(DEFAULT_ROOM_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingDonationTable() throws Exception {
        // Get the donationTable
        restDonationTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDonationTable() throws Exception {
        // Initialize the database
        donationTableRepository.saveAndFlush(donationTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the donationTable
        DonationTable updatedDonationTable = donationTableRepository.findById(donationTable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDonationTable are not directly saved in db
        em.detach(updatedDonationTable);
        updatedDonationTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .donationId(UPDATED_DONATION_ID)
            .donationType(UPDATED_DONATION_TYPE)
            .donationDesc(UPDATED_DONATION_DESC)
            .donationMonth(UPDATED_DONATION_MONTH)
            .donationCost(UPDATED_DONATION_COST)
            .roomId(UPDATED_ROOM_ID)
            .status(UPDATED_STATUS);

        restDonationTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDonationTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDonationTable))
            )
            .andExpect(status().isOk());

        // Validate the DonationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDonationTableToMatchAllProperties(updatedDonationTable);
    }

    @Test
    @Transactional
    void putNonExistingDonationTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        donationTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonationTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donationTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(donationTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDonationTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        donationTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(donationTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDonationTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        donationTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(donationTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DonationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDonationTableWithPatch() throws Exception {
        // Initialize the database
        donationTableRepository.saveAndFlush(donationTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the donationTable using partial update
        DonationTable partialUpdatedDonationTable = new DonationTable();
        partialUpdatedDonationTable.setId(donationTable.getId());

        partialUpdatedDonationTable
            .deletedAt(UPDATED_DELETED_AT)
            .donationId(UPDATED_DONATION_ID)
            .donationType(UPDATED_DONATION_TYPE)
            .donationCost(UPDATED_DONATION_COST);

        restDonationTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonationTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDonationTable))
            )
            .andExpect(status().isOk());

        // Validate the DonationTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDonationTableUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDonationTable, donationTable),
            getPersistedDonationTable(donationTable)
        );
    }

    @Test
    @Transactional
    void fullUpdateDonationTableWithPatch() throws Exception {
        // Initialize the database
        donationTableRepository.saveAndFlush(donationTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the donationTable using partial update
        DonationTable partialUpdatedDonationTable = new DonationTable();
        partialUpdatedDonationTable.setId(donationTable.getId());

        partialUpdatedDonationTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .donationId(UPDATED_DONATION_ID)
            .donationType(UPDATED_DONATION_TYPE)
            .donationDesc(UPDATED_DONATION_DESC)
            .donationMonth(UPDATED_DONATION_MONTH)
            .donationCost(UPDATED_DONATION_COST)
            .roomId(UPDATED_ROOM_ID)
            .status(UPDATED_STATUS);

        restDonationTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonationTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDonationTable))
            )
            .andExpect(status().isOk());

        // Validate the DonationTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDonationTableUpdatableFieldsEquals(partialUpdatedDonationTable, getPersistedDonationTable(partialUpdatedDonationTable));
    }

    @Test
    @Transactional
    void patchNonExistingDonationTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        donationTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonationTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, donationTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(donationTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDonationTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        donationTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(donationTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDonationTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        donationTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonationTableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(donationTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DonationTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDonationTable() throws Exception {
        // Initialize the database
        donationTableRepository.saveAndFlush(donationTable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the donationTable
        restDonationTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, donationTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return donationTableRepository.count();
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

    protected DonationTable getPersistedDonationTable(DonationTable donationTable) {
        return donationTableRepository.findById(donationTable.getId()).orElseThrow();
    }

    protected void assertPersistedDonationTableToMatchAllProperties(DonationTable expectedDonationTable) {
        assertDonationTableAllPropertiesEquals(expectedDonationTable, getPersistedDonationTable(expectedDonationTable));
    }

    protected void assertPersistedDonationTableToMatchUpdatableProperties(DonationTable expectedDonationTable) {
        assertDonationTableAllUpdatablePropertiesEquals(expectedDonationTable, getPersistedDonationTable(expectedDonationTable));
    }
}
