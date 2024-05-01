package org.qlcc.app.repository;

import org.qlcc.app.domain.RequestTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RequestTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestTableRepository extends JpaRepository<RequestTable, Integer> {}
