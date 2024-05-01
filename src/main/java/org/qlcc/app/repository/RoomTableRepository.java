package org.qlcc.app.repository;

import org.qlcc.app.domain.RoomTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RoomTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomTableRepository extends JpaRepository<RoomTable, Long> {}
