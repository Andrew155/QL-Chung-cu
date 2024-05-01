package org.qlcc.app.service;

import java.util.Optional;
import org.qlcc.app.domain.DonationTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.qlcc.app.domain.DonationTable}.
 */
public interface DonationTableService {
    /**
     * Save a donationTable.
     *
     * @param donationTable the entity to save.
     * @return the persisted entity.
     */
    DonationTable save(DonationTable donationTable);

    /**
     * Updates a donationTable.
     *
     * @param donationTable the entity to update.
     * @return the persisted entity.
     */
    DonationTable update(DonationTable donationTable);

    /**
     * Partially updates a donationTable.
     *
     * @param donationTable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DonationTable> partialUpdate(DonationTable donationTable);

    /**
     * Get all the donationTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DonationTable> findAll(Pageable pageable);

    /**
     * Get all the donationTables with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DonationTable> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" donationTable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DonationTable> findOne(Long id);

    /**
     * Delete the "id" donationTable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
