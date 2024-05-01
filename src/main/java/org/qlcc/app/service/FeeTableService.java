package org.qlcc.app.service;

import java.util.Optional;
import org.qlcc.app.domain.FeeTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.qlcc.app.domain.FeeTable}.
 */
public interface FeeTableService {
    /**
     * Save a feeTable.
     *
     * @param feeTable the entity to save.
     * @return the persisted entity.
     */
    FeeTable save(FeeTable feeTable);

    /**
     * Updates a feeTable.
     *
     * @param feeTable the entity to update.
     * @return the persisted entity.
     */
    FeeTable update(FeeTable feeTable);

    /**
     * Partially updates a feeTable.
     *
     * @param feeTable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FeeTable> partialUpdate(FeeTable feeTable);

    /**
     * Get all the feeTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FeeTable> findAll(Pageable pageable);

    /**
     * Get all the feeTables with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FeeTable> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" feeTable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FeeTable> findOne(Long id);

    /**
     * Delete the "id" feeTable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
