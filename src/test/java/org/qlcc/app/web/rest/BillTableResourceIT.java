package org.qlcc.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.qlcc.app.domain.BillTableAsserts.*;
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
import org.qlcc.app.domain.BillTable;
import org.qlcc.app.repository.BillTableRepository;
import org.qlcc.app.service.BillTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BillTableResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BillTableResourceIT {

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BILL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BILL_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_BILL_ID = "AAAAAAAAAA";
    private static final String UPDATED_BILL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BILL_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_BILL_MONTH = "BBBBBBBBBB";

    private static final Integer DEFAULT_BILL_AMOUNT = 1;
    private static final Integer UPDATED_BILL_AMOUNT = 2;

    private static final String DEFAULT_ROOM_ID = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_BILL_COST = 1L;
    private static final Long UPDATED_BILL_COST = 2L;

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bill-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BillTableRepository billTableRepository;

    @Mock
    private BillTableRepository billTableRepositoryMock;

    @Mock
    private BillTableService billTableServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillTableMockMvc;

    private BillTable billTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillTable createEntity(EntityManager em) {
        BillTable billTable = new BillTable()
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT)
            .deletedAt(DEFAULT_DELETED_AT)
            .billType(DEFAULT_BILL_TYPE)
            .billId(DEFAULT_BILL_ID)
            .billMonth(DEFAULT_BILL_MONTH)
            .billAmount(DEFAULT_BILL_AMOUNT)
            .roomId(DEFAULT_ROOM_ID)
            .date(DEFAULT_DATE)
            .status(DEFAULT_STATUS)
            .billCost(DEFAULT_BILL_COST)
            .customerID(DEFAULT_CUSTOMER_ID);
        return billTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillTable createUpdatedEntity(EntityManager em) {
        BillTable billTable = new BillTable()
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .billType(UPDATED_BILL_TYPE)
            .billId(UPDATED_BILL_ID)
            .billMonth(UPDATED_BILL_MONTH)
            .billAmount(UPDATED_BILL_AMOUNT)
            .roomId(UPDATED_ROOM_ID)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS)
            .billCost(UPDATED_BILL_COST)
            .customerID(UPDATED_CUSTOMER_ID);
        return billTable;
    }

    @BeforeEach
    public void initTest() {
        billTable = createEntity(em);
    }

    @Test
    @Transactional
    void createBillTable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BillTable
        var returnedBillTable = om.readValue(
            restBillTableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billTable)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BillTable.class
        );

        // Validate the BillTable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBillTableUpdatableFieldsEquals(returnedBillTable, getPersistedBillTable(returnedBillTable));
    }

    @Test
    @Transactional
    void createBillTableWithExistingId() throws Exception {
        // Create the BillTable with an existing ID
        billTable.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billTable)))
            .andExpect(status().isBadRequest());

        // Validate the BillTable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBillIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        billTable.setBillId(null);

        // Create the BillTable, which fails.

        restBillTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billTable)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBillTables() throws Exception {
        // Initialize the database
        billTableRepository.saveAndFlush(billTable);

        // Get all the billTableList
        restBillTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].billType").value(hasItem(DEFAULT_BILL_TYPE)))
            .andExpect(jsonPath("$.[*].billId").value(hasItem(DEFAULT_BILL_ID)))
            .andExpect(jsonPath("$.[*].billMonth").value(hasItem(DEFAULT_BILL_MONTH)))
            .andExpect(jsonPath("$.[*].billAmount").value(hasItem(DEFAULT_BILL_AMOUNT)))
            .andExpect(jsonPath("$.[*].roomId").value(hasItem(DEFAULT_ROOM_ID)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].billCost").value(hasItem(DEFAULT_BILL_COST.intValue())))
            .andExpect(jsonPath("$.[*].customerID").value(hasItem(DEFAULT_CUSTOMER_ID)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBillTablesWithEagerRelationshipsIsEnabled() throws Exception {
        when(billTableServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBillTableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(billTableServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBillTablesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(billTableServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBillTableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(billTableRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBillTable() throws Exception {
        // Initialize the database
        billTableRepository.saveAndFlush(billTable);

        // Get the billTable
        restBillTableMockMvc
            .perform(get(ENTITY_API_URL_ID, billTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billTable.getId().intValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.billType").value(DEFAULT_BILL_TYPE))
            .andExpect(jsonPath("$.billId").value(DEFAULT_BILL_ID))
            .andExpect(jsonPath("$.billMonth").value(DEFAULT_BILL_MONTH))
            .andExpect(jsonPath("$.billAmount").value(DEFAULT_BILL_AMOUNT))
            .andExpect(jsonPath("$.roomId").value(DEFAULT_ROOM_ID))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.billCost").value(DEFAULT_BILL_COST.intValue()))
            .andExpect(jsonPath("$.customerID").value(DEFAULT_CUSTOMER_ID));
    }

    @Test
    @Transactional
    void getNonExistingBillTable() throws Exception {
        // Get the billTable
        restBillTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBillTable() throws Exception {
        // Initialize the database
        billTableRepository.saveAndFlush(billTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billTable
        BillTable updatedBillTable = billTableRepository.findById(billTable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBillTable are not directly saved in db
        em.detach(updatedBillTable);
        updatedBillTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .billType(UPDATED_BILL_TYPE)
            .billId(UPDATED_BILL_ID)
            .billMonth(UPDATED_BILL_MONTH)
            .billAmount(UPDATED_BILL_AMOUNT)
            .roomId(UPDATED_ROOM_ID)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS)
            .billCost(UPDATED_BILL_COST)
            .customerID(UPDATED_CUSTOMER_ID);

        restBillTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBillTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBillTable))
            )
            .andExpect(status().isOk());

        // Validate the BillTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBillTableToMatchAllProperties(updatedBillTable);
    }

    @Test
    @Transactional
    void putNonExistingBillTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billTable.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillTableWithPatch() throws Exception {
        // Initialize the database
        billTableRepository.saveAndFlush(billTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billTable using partial update
        BillTable partialUpdatedBillTable = new BillTable();
        partialUpdatedBillTable.setId(billTable.getId());

        partialUpdatedBillTable
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .billType(UPDATED_BILL_TYPE)
            .billId(UPDATED_BILL_ID)
            .billAmount(UPDATED_BILL_AMOUNT)
            .roomId(UPDATED_ROOM_ID)
            .date(UPDATED_DATE);

        restBillTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBillTable))
            )
            .andExpect(status().isOk());

        // Validate the BillTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillTableUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBillTable, billTable),
            getPersistedBillTable(billTable)
        );
    }

    @Test
    @Transactional
    void fullUpdateBillTableWithPatch() throws Exception {
        // Initialize the database
        billTableRepository.saveAndFlush(billTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billTable using partial update
        BillTable partialUpdatedBillTable = new BillTable();
        partialUpdatedBillTable.setId(billTable.getId());

        partialUpdatedBillTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .billType(UPDATED_BILL_TYPE)
            .billId(UPDATED_BILL_ID)
            .billMonth(UPDATED_BILL_MONTH)
            .billAmount(UPDATED_BILL_AMOUNT)
            .roomId(UPDATED_ROOM_ID)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS)
            .billCost(UPDATED_BILL_COST)
            .customerID(UPDATED_CUSTOMER_ID);

        restBillTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBillTable))
            )
            .andExpect(status().isOk());

        // Validate the BillTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillTableUpdatableFieldsEquals(partialUpdatedBillTable, getPersistedBillTable(partialUpdatedBillTable));
    }

    @Test
    @Transactional
    void patchNonExistingBillTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillTableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(billTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillTable() throws Exception {
        // Initialize the database
        billTableRepository.saveAndFlush(billTable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the billTable
        restBillTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, billTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return billTableRepository.count();
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

    protected BillTable getPersistedBillTable(BillTable billTable) {
        return billTableRepository.findById(billTable.getId()).orElseThrow();
    }

    protected void assertPersistedBillTableToMatchAllProperties(BillTable expectedBillTable) {
        assertBillTableAllPropertiesEquals(expectedBillTable, getPersistedBillTable(expectedBillTable));
    }

    protected void assertPersistedBillTableToMatchUpdatableProperties(BillTable expectedBillTable) {
        assertBillTableAllUpdatablePropertiesEquals(expectedBillTable, getPersistedBillTable(expectedBillTable));
    }
}
