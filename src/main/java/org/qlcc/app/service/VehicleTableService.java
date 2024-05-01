package org.qlcc.app.service;

import java.util.Optional;
import org.qlcc.app.domain.VehicleTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.qlcc.app.domain.VehicleTable}.
 */
public interface VehicleTableService {
    /**
     * Save a vehicleTable.
     *
     * @param vehicleTable the entity to save.
     * @return the persisted entity.
     */
    VehicleTable save(VehicleTable vehicleTable);

    /**
     * Updates a vehicleTable.
     *
     * @param vehicleTable the entity to update.
     * @return the persisted entity.
     */
    VehicleTable update(VehicleTable vehicleTable);

    /**
     * Partially updates a vehicleTable.
     *
     * @param vehicleTable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VehicleTable> partialUpdate(VehicleTable vehicleTable);

    /**
     * Get all the vehicleTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehicleTable> findAll(Pageable pageable);

    /**
     * Get all the vehicleTables with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehicleTable> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" vehicleTable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleTable> findOne(Long id);

    /**
     * Delete the "id" vehicleTable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
