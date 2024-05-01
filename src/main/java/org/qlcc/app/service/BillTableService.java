package org.qlcc.app.service;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.BillTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.qlcc.app.domain.BillTable}.
 */
public interface BillTableService {
    /**
     * Save a billTable.
     *
     * @param billTable the entity to save.
     * @return the persisted entity.
     */
    BillTable save(BillTable billTable);

    /**
     * Updates a billTable.
     *
     * @param billTable the entity to update.
     * @return the persisted entity.
     */
    BillTable update(BillTable billTable);

    /**
     * Partially updates a billTable.
     *
     * @param billTable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BillTable> partialUpdate(BillTable billTable);

    /**
     * Get all the billTables.
     *
     * @return the list of entities.
     */
    List<BillTable> findAll();

    /**
     * Get all the billTables with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BillTable> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" billTable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillTable> findOne(Long id);

    /**
     * Delete the "id" billTable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
