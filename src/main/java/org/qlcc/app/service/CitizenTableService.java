package org.qlcc.app.service;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.CitizenTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.qlcc.app.domain.CitizenTable}.
 */
public interface CitizenTableService {
    /**
     * Save a citizenTable.
     *
     * @param citizenTable the entity to save.
     * @return the persisted entity.
     */
    CitizenTable save(CitizenTable citizenTable);

    /**
     * Updates a citizenTable.
     *
     * @param citizenTable the entity to update.
     * @return the persisted entity.
     */
    CitizenTable update(CitizenTable citizenTable);

    /**
     * Partially updates a citizenTable.
     *
     * @param citizenTable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CitizenTable> partialUpdate(CitizenTable citizenTable);

    /**
     * Get all the citizenTables.
     *
     * @return the list of entities.
     */
    List<CitizenTable> findAll();

    /**
     * Get all the citizenTables with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CitizenTable> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" citizenTable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CitizenTable> findOne(Long id);

    /**
     * Delete the "id" citizenTable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
