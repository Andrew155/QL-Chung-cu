package org.qlcc.app.repository;

import org.qlcc.app.domain.NotificationTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NotificationTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationTableRepository extends JpaRepository<NotificationTable, Integer> {}
