package org.qlcc.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.qlcc.app.domain.CitizenTableAsserts.*;
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
import org.qlcc.app.domain.CitizenTable;
import org.qlcc.app.repository.CitizenTableRepository;
import org.qlcc.app.service.CitizenTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CitizenTableResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CitizenTableResourceIT {

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CITIZEN_ID = "AAAAAAAAAA";
    private static final String UPDATED_CITIZEN_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOB = "AAAAAAAAAA";
    private static final String UPDATED_DOB = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_RELATION = "AAAAAAAAAA";
    private static final String UPDATED_RELATION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/citizen-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CitizenTableRepository citizenTableRepository;

    @Mock
    private CitizenTableRepository citizenTableRepositoryMock;

    @Mock
    private CitizenTableService citizenTableServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCitizenTableMockMvc;

    private CitizenTable citizenTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CitizenTable createEntity(EntityManager em) {
        CitizenTable citizenTable = new CitizenTable()
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT)
            .deletedAt(DEFAULT_DELETED_AT)
            .citizenID(DEFAULT_CITIZEN_ID)
            .name(DEFAULT_NAME)
            .dob(DEFAULT_DOB)
            .contact(DEFAULT_CONTACT)
            .gender(DEFAULT_GENDER)
            .relation(DEFAULT_RELATION)
            .status(DEFAULT_STATUS);
        return citizenTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CitizenTable createUpdatedEntity(EntityManager em) {
        CitizenTable citizenTable = new CitizenTable()
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .citizenID(UPDATED_CITIZEN_ID)
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .contact(UPDATED_CONTACT)
            .gender(UPDATED_GENDER)
            .relation(UPDATED_RELATION)
            .status(UPDATED_STATUS);
        return citizenTable;
    }

    @BeforeEach
    public void initTest() {
        citizenTable = createEntity(em);
    }

    @Test
    @Transactional
    void createCitizenTable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CitizenTable
        var returnedCitizenTable = om.readValue(
            restCitizenTableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenTable)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CitizenTable.class
        );

        // Validate the CitizenTable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCitizenTableUpdatableFieldsEquals(returnedCitizenTable, getPersistedCitizenTable(returnedCitizenTable));
    }

    @Test
    @Transactional
    void createCitizenTableWithExistingId() throws Exception {
        // Create the CitizenTable with an existing ID
        citizenTable.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitizenTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenTable)))
            .andExpect(status().isBadRequest());

        // Validate the CitizenTable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCitizenIDIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        citizenTable.setCitizenID(null);

        // Create the CitizenTable, which fails.

        restCitizenTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenTable)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCitizenTables() throws Exception {
        // Initialize the database
        citizenTableRepository.saveAndFlush(citizenTable);

        // Get all the citizenTableList
        restCitizenTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(citizenTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())))
            .andExpect(jsonPath("$.[*].citizenID").value(hasItem(DEFAULT_CITIZEN_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].relation").value(hasItem(DEFAULT_RELATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCitizenTablesWithEagerRelationshipsIsEnabled() throws Exception {
        when(citizenTableServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCitizenTableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(citizenTableServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCitizenTablesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(citizenTableServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCitizenTableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(citizenTableRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCitizenTable() throws Exception {
        // Initialize the database
        citizenTableRepository.saveAndFlush(citizenTable);

        // Get the citizenTable
        restCitizenTableMockMvc
            .perform(get(ENTITY_API_URL_ID, citizenTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(citizenTable.getId().intValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()))
            .andExpect(jsonPath("$.citizenID").value(DEFAULT_CITIZEN_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.relation").value(DEFAULT_RELATION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingCitizenTable() throws Exception {
        // Get the citizenTable
        restCitizenTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCitizenTable() throws Exception {
        // Initialize the database
        citizenTableRepository.saveAndFlush(citizenTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the citizenTable
        CitizenTable updatedCitizenTable = citizenTableRepository.findById(citizenTable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCitizenTable are not directly saved in db
        em.detach(updatedCitizenTable);
        updatedCitizenTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .citizenID(UPDATED_CITIZEN_ID)
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .contact(UPDATED_CONTACT)
            .gender(UPDATED_GENDER)
            .relation(UPDATED_RELATION)
            .status(UPDATED_STATUS);

        restCitizenTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCitizenTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCitizenTable))
            )
            .andExpect(status().isOk());

        // Validate the CitizenTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCitizenTableToMatchAllProperties(updatedCitizenTable);
    }

    @Test
    @Transactional
    void putNonExistingCitizenTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citizenTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitizenTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citizenTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(citizenTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitizenTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCitizenTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citizenTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitizenTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(citizenTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitizenTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCitizenTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citizenTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitizenTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citizenTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CitizenTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCitizenTableWithPatch() throws Exception {
        // Initialize the database
        citizenTableRepository.saveAndFlush(citizenTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the citizenTable using partial update
        CitizenTable partialUpdatedCitizenTable = new CitizenTable();
        partialUpdatedCitizenTable.setId(citizenTable.getId());

        partialUpdatedCitizenTable
            .createAt(UPDATED_CREATE_AT)
            .name(UPDATED_NAME)
            .contact(UPDATED_CONTACT)
            .relation(UPDATED_RELATION)
            .status(UPDATED_STATUS);

        restCitizenTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCitizenTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCitizenTable))
            )
            .andExpect(status().isOk());

        // Validate the CitizenTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCitizenTableUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCitizenTable, citizenTable),
            getPersistedCitizenTable(citizenTable)
        );
    }

    @Test
    @Transactional
    void fullUpdateCitizenTableWithPatch() throws Exception {
        // Initialize the database
        citizenTableRepository.saveAndFlush(citizenTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the citizenTable using partial update
        CitizenTable partialUpdatedCitizenTable = new CitizenTable();
        partialUpdatedCitizenTable.setId(citizenTable.getId());

        partialUpdatedCitizenTable
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .deletedAt(UPDATED_DELETED_AT)
            .citizenID(UPDATED_CITIZEN_ID)
            .name(UPDATED_NAME)
            .dob(UPDATED_DOB)
            .contact(UPDATED_CONTACT)
            .gender(UPDATED_GENDER)
            .relation(UPDATED_RELATION)
            .status(UPDATED_STATUS);

        restCitizenTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCitizenTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCitizenTable))
            )
            .andExpect(status().isOk());

        // Validate the CitizenTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCitizenTableUpdatableFieldsEquals(partialUpdatedCitizenTable, getPersistedCitizenTable(partialUpdatedCitizenTable));
    }

    @Test
    @Transactional
    void patchNonExistingCitizenTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citizenTable.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitizenTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, citizenTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(citizenTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitizenTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCitizenTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citizenTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitizenTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(citizenTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitizenTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCitizenTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citizenTable.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitizenTableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(citizenTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CitizenTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCitizenTable() throws Exception {
        // Initialize the database
        citizenTableRepository.saveAndFlush(citizenTable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the citizenTable
        restCitizenTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, citizenTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return citizenTableRepository.count();
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

    protected CitizenTable getPersistedCitizenTable(CitizenTable citizenTable) {
        return citizenTableRepository.findById(citizenTable.getId()).orElseThrow();
    }

    protected void assertPersistedCitizenTableToMatchAllProperties(CitizenTable expectedCitizenTable) {
        assertCitizenTableAllPropertiesEquals(expectedCitizenTable, getPersistedCitizenTable(expectedCitizenTable));
    }

    protected void assertPersistedCitizenTableToMatchUpdatableProperties(CitizenTable expectedCitizenTable) {
        assertCitizenTableAllUpdatablePropertiesEquals(expectedCitizenTable, getPersistedCitizenTable(expectedCitizenTable));
    }
}
