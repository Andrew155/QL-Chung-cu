package org.qlcc.app.service;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.NotificationTable;

/**
 * Service Interface for managing {@link org.qlcc.app.domain.NotificationTable}.
 */
public interface NotificationTableService {
    /**
     * Save a notificationTable.
     *
     * @param notificationTable the entity to save.
     * @return the persisted entity.
     */
    NotificationTable save(NotificationTable notificationTable);

    /**
     * Updates a notificationTable.
     *
     * @param notificationTable the entity to update.
     * @return the persisted entity.
     */
    NotificationTable update(NotificationTable notificationTable);

    /**
     * Partially updates a notificationTable.
     *
     * @param notificationTable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NotificationTable> partialUpdate(NotificationTable notificationTable);

    /**
     * Get all the notificationTables.
     *
     * @return the list of entities.
     */
    List<NotificationTable> findAll();

    /**
     * Get all the NotificationTable where RequestTable is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<NotificationTable> findAllWhereRequestTableIsNull();

    /**
     * Get the "id" notificationTable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotificationTable> findOne(Integer id);

    /**
     * Delete the "id" notificationTable.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
