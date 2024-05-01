package org.qlcc.app.repository;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.BillTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BillTable entity.
 */
@Repository
public interface BillTableRepository extends JpaRepository<BillTable, Long> {
    default Optional<BillTable> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BillTable> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BillTable> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select billTable from BillTable billTable left join fetch billTable.roomTable left join fetch billTable.applicationTable",
        countQuery = "select count(billTable) from BillTable billTable"
    )
    Page<BillTable> findAllWithToOneRelationships(Pageable pageable);

    @Query("select billTable from BillTable billTable left join fetch billTable.roomTable left join fetch billTable.applicationTable")
    List<BillTable> findAllWithToOneRelationships();

    @Query(
        "select billTable from BillTable billTable left join fetch billTable.roomTable left join fetch billTable.applicationTable where billTable.id =:id"
    )
    Optional<BillTable> findOneWithToOneRelationships(@Param("id") Long id);
}
