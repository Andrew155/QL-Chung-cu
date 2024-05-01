package org.qlcc.app.service;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.RoomTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.qlcc.app.domain.RoomTable}.
 */
public interface RoomTableService {
    /**
     * Save a roomTable.
     *
     * @param roomTable the entity to save.
     * @return the persisted entity.
     */
    RoomTable save(RoomTable roomTable);

    /**
     * Updates a roomTable.
     *
     * @param roomTable the entity to update.
     * @return the persisted entity.
     */
    RoomTable update(RoomTable roomTable);

    /**
     * Partially updates a roomTable.
     *
     * @param roomTable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RoomTable> partialUpdate(RoomTable roomTable);

    /**
     * Get all the roomTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RoomTable> findAll(Pageable pageable);

    /**
     * Get all the RoomTable where CitizenTable is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<RoomTable> findAllWhereCitizenTableIsNull();

    /**
     * Get the "id" roomTable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoomTable> findOne(Long id);

    /**
     * Delete the "id" roomTable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
