package org.qlcc.app.service;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.RequestTable;

/**
 * Service Interface for managing {@link org.qlcc.app.domain.RequestTable}.
 */
public interface RequestTableService {
    /**
     * Save a requestTable.
     *
     * @param requestTable the entity to save.
     * @return the persisted entity.
     */
    RequestTable save(RequestTable requestTable);

    /**
     * Updates a requestTable.
     *
     * @param requestTable the entity to update.
     * @return the persisted entity.
     */
    RequestTable update(RequestTable requestTable);

    /**
     * Partially updates a requestTable.
     *
     * @param requestTable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RequestTable> partialUpdate(RequestTable requestTable);

    /**
     * Get all the requestTables.
     *
     * @return the list of entities.
     */
    List<RequestTable> findAll();

    /**
     * Get the "id" requestTable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RequestTable> findOne(Integer id);

    /**
     * Delete the "id" requestTable.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
