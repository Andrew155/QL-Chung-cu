package org.qlcc.app.repository;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.FeeTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FeeTable entity.
 */
@Repository
public interface FeeTableRepository extends JpaRepository<FeeTable, Long> {
    default Optional<FeeTable> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FeeTable> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FeeTable> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select feeTable from FeeTable feeTable left join fetch feeTable.roomTable",
        countQuery = "select count(feeTable) from FeeTable feeTable"
    )
    Page<FeeTable> findAllWithToOneRelationships(Pageable pageable);

    @Query("select feeTable from FeeTable feeTable left join fetch feeTable.roomTable")
    List<FeeTable> findAllWithToOneRelationships();

    @Query("select feeTable from FeeTable feeTable left join fetch feeTable.roomTable where feeTable.id =:id")
    Optional<FeeTable> findOneWithToOneRelationships(@Param("id") Long id);
}
