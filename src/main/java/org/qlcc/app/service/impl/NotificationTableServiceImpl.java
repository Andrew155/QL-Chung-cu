package org.qlcc.app.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.qlcc.app.domain.NotificationTable;
import org.qlcc.app.repository.NotificationTableRepository;
import org.qlcc.app.service.NotificationTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.qlcc.app.domain.NotificationTable}.
 */
@Service
@Transactional
public class NotificationTableServiceImpl implements NotificationTableService {

    private final Logger log = LoggerFactory.getLogger(NotificationTableServiceImpl.class);

    private final NotificationTableRepository notificationTableRepository;

    public NotificationTableServiceImpl(NotificationTableRepository notificationTableRepository) {
        this.notificationTableRepository = notificationTableRepository;
    }

    @Override
    public NotificationTable save(NotificationTable notificationTable) {
        log.debug("Request to save NotificationTable : {}", notificationTable);
        return notificationTableRepository.save(notificationTable);
    }

    @Override
    public NotificationTable update(NotificationTable notificationTable) {
        log.debug("Request to update NotificationTable : {}", notificationTable);
        return notificationTableRepository.save(notificationTable);
    }

    @Override
    public Optional<NotificationTable> partialUpdate(NotificationTable notificationTable) {
        log.debug("Request to partially update NotificationTable : {}", notificationTable);

        return notificationTableRepository
            .findById(notificationTable.getId())
            .map(existingNotificationTable -> {
                if (notificationTable.getCreateAt() != null) {
                    existingNotificationTable.setCreateAt(notificationTable.getCreateAt());
                }
                if (notificationTable.getUpdateAt() != null) {
                    existingNotificationTable.setUpdateAt(notificationTable.getUpdateAt());
                }
                if (notificationTable.getDeletedAt() != null) {
                    existingNotificationTable.setDeletedAt(notificationTable.getDeletedAt());
                }
                if (notificationTable.getTitle() != null) {
                    existingNotificationTable.setTitle(notificationTable.getTitle());
                }
                if (notificationTable.getContent() != null) {
                    existingNotificationTable.setContent(notificationTable.getContent());
                }
                if (notificationTable.getUserID() != null) {
                    existingNotificationTable.setUserID(notificationTable.getUserID());
                }

                return existingNotificationTable;
            })
            .map(notificationTableRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTable> findAll() {
        log.debug("Request to get all NotificationTables");
        return notificationTableRepository.findAll();
    }

    /**
     *  Get all the notificationTables where RequestTable is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NotificationTable> findAllWhereRequestTableIsNull() {
        log.debug("Request to get all notificationTables where RequestTable is null");
        return StreamSupport.stream(notificationTableRepository.findAll().spliterator(), false)
            .filter(notificationTable -> notificationTable.getRequestTable() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationTable> findOne(Integer id) {
        log.debug("Request to get NotificationTable : {}", id);
        return notificationTableRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete NotificationTable : {}", id);
        notificationTableRepository.deleteById(id);
    }
}
