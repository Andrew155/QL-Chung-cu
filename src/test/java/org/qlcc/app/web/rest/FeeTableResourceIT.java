package org.qlcc.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.qlcc.app.domain.FeeTableAsserts.*;
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
import org.qlcc.app.domain.FeeTable;
import org.qlcc.app.repository.FeeTableRepository;
import org.qlcc.app.service.FeeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FeeTableResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FeeTableResourceIT {

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FEE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FEE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FEE_DESC = "AAAAAAAAAA";
    private static final String UPDATED_FEE_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_FEE_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_FEE_MONTH = "BBBBBBBBBB";

    private static final Long DEFAULT_FEE_COST = 1L;
    private static final Long UPDATED_FEE_COST = 2L;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_FEE_ID = "AAAAAAAAAA";
    private static final String UPDATED_FEE_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fee-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeeTableRepository feeTableRepository;

    @Mock
    private FeeTableRepository feeTableRepositoryMock;

    @Mock
    private FeeTableService feeTableServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeeTableMockMvc;

    private FeeTable feeTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeeTable createEntity(EntityManager em) {
        FeeTable feeTable = new FeeTable()
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT)
            .deletedAt(DEFAULT_DELETED_AT)
            .feeType(DEFAULT_FEE_TYPE)
            .feeDesc(DEFAULT_FEE_DESC)
            .feeMonth(DEFAULT_FEE_MONTH)
            .feeCost(DEFAULT_FEE_COST)
            .date(DEFAULT_DATE)
            .status(DEFAULT_STATUS)
            .feeId(DEFAULT_FEE_ID);
        return feeTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeeTable createUpdatedEntity(EntityManager em) {
        FeeTable feeTable = new FeeTable()
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .feeType(UPDATED_FEE_TYPE)
            .feeDesc(UPDATED_FEE_DESC)
            .feeMonth(UPDATED_FEE_MONTH)
            .feeCost(UPDATED_FEE_COST)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS)
            .feeId(UPDATED_FEE_ID);
        return feeTable;
    }

    @BeforeEach
    public void initTest() {
        feeTable = createEntity(em);
    }

    @Test
    @Transactional
    void createFeeTable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FeeTable
        var returnedFeeTable = om.readValue(
            restFeeTableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feeTable)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeeTable.class
        );

        // Validate the FeeTable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFeeTableUpdatableFieldsEquals(returnedFeeTable, getPersistedFeeTable(returnedFeeTable));
    }

    @Test
    @Transactional
    void createFeeTableWithExistingId() throws Exception {
        // Create the FeeTable with an existing ID
        feeTable.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeeTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feeTable)))
            .andExpect(status().isBadRequest());

        // Validate the FeeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFeeIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feeTable.setFeeId(null);

        // Create the FeeTable, which fails.

        restFeeTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feeTable)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeeTables() throws Exception {
        // Initialize the database
        feeTableRepository.saveAndFlush(feeTable);

        // Get all the feeTableList
        restFeeTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feeTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].feeType").value(hasItem(DEFAULT_FEE_TYPE)))
            .andExpect(jsonPath("$.[*].feeDesc").value(hasItem(DEFAULT_FEE_DESC)))
            .andExpect(jsonPath("$.[*].feeMonth").value(hasItem(DEFAULT_FEE_MONTH)))
            .andExpect(jsonPath("$.[*].feeCost").value(hasItem(DEFAULT_FEE_COST.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].feeId").value(hasItem(DEFAULT_FEE_ID)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeeTablesWithEagerRelationshipsIsEnabled() throws Exception {
        when(feeTableServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeeTableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(feeTableServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeeTablesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(feeTableServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeeTableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(feeTableRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFeeTable() throws Exception {
        // Initialize the database
        feeTableRepository.saveAndFlush(feeTable);

        // Get the feeTable
        restFeeTableMockMvc
            .perform(get(ENTITY_API_URL_ID, feeTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feeTable.getId().intValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.feeType").value(DEFAULT_FEE_TYPE))
            .andExpect(jsonPath("$.feeDesc").value(DEFAULT_FEE_DESC))
            .andExpect(jsonPath("$.feeMonth").value(DEFAULT_FEE_MONTH))
            .andExpect(jsonPath("$.feeCost").value(DEFAULT_FEE_COST.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.feeId").value(DEFAULT_FEE_ID));
    }

    @Test
    @Transactional
    void getNonExistingFeeTable() throws Exception {
        // Get the feeTable
        restFeeTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeeTable() throws Exception {
        // Initialize the database
        feeTableRepository.saveAndFlush(feeTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feeTable
        FeeTable updatedFeeTable = feeTableRepository.findById(feeTable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeeTable are not directly saved in db
        em.detach(updatedFeeTable);
        updatedFeeTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .feeType(UPDATED_FEE_TYPE)
            .feeDesc(UPDATED_FEE_DESC)
            .feeMonth(UPDATED_FEE_MONTH)
            .feeCost(UPDATED_FEE_COST)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS)
            .feeId(UPDATED_FEE_ID);

        restFeeTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeeTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFeeTable))
            )
            .andExpect(status().isOk());

        // Validate the FeeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeeTableToMatchAllProperties(updatedFeeTable);
    }

    @Test
    @Transactional
    void putNonExistingFeeTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feeTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeeTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feeTable.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feeTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeeTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feeTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feeTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeeTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feeTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feeTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeeTableWithPatch() throws Exception {
        // Initialize the database
        feeTableRepository.saveAndFlush(feeTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feeTable using partial update
        FeeTable partialUpdatedFeeTable = new FeeTable();
        partialUpdatedFeeTable.setId(feeTable.getId());

        partialUpdatedFeeTable
            .updateAt(UPDATED_UPDATE_AT)
            .feeType(UPDATED_FEE_TYPE)
            .feeDesc(UPDATED_FEE_DESC)
            .feeMonth(UPDATED_FEE_MONTH)
            .status(UPDATED_STATUS);

        restFeeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeeTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeeTable))
            )
            .andExpect(status().isOk());

        // Validate the FeeTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeeTableUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFeeTable, feeTable), getPersistedFeeTable(feeTable));
    }

    @Test
    @Transactional
    void fullUpdateFeeTableWithPatch() throws Exception {
        // Initialize the database
        feeTableRepository.saveAndFlush(feeTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feeTable using partial update
        FeeTable partialUpdatedFeeTable = new FeeTable();
        partialUpdatedFeeTable.setId(feeTable.getId());

        partialUpdatedFeeTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .feeType(UPDATED_FEE_TYPE)
            .feeDesc(UPDATED_FEE_DESC)
            .feeMonth(UPDATED_FEE_MONTH)
            .feeCost(UPDATED_FEE_COST)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS)
            .feeId(UPDATED_FEE_ID);

        restFeeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeeTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeeTable))
            )
            .andExpect(status().isOk());

        // Validate the FeeTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeeTableUpdatableFieldsEquals(partialUpdatedFeeTable, getPersistedFeeTable(partialUpdatedFeeTable));
    }

    @Test
    @Transactional
    void patchNonExistingFeeTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feeTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feeTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feeTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeeTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feeTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feeTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeeTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feeTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeTableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feeTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeeTable() throws Exception {
        // Initialize the database
        feeTableRepository.saveAndFlush(feeTable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feeTable
        restFeeTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, feeTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feeTableRepository.count();
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

    protected FeeTable getPersistedFeeTable(FeeTable feeTable) {
        return feeTableRepository.findById(feeTable.getId()).orElseThrow();
    }

    protected void assertPersistedFeeTableToMatchAllProperties(FeeTable expectedFeeTable) {
        assertFeeTableAllPropertiesEquals(expectedFeeTable, getPersistedFeeTable(expectedFeeTable));
    }

    protected void assertPersistedFeeTableToMatchUpdatableProperties(FeeTable expectedFeeTable) {
        assertFeeTableAllUpdatablePropertiesEquals(expectedFeeTable, getPersistedFeeTable(expectedFeeTable));
    }
}
