package org.qlcc.app.repository;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.CitizenTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CitizenTable entity.
 */
@Repository
public interface CitizenTableRepository extends JpaRepository<CitizenTable, Long> {
    default Optional<CitizenTable> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CitizenTable> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CitizenTable> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select citizenTable from CitizenTable citizenTable left join fetch citizenTable.familyId",
        countQuery = "select count(citizenTable) from CitizenTable citizenTable"
    )
    Page<CitizenTable> findAllWithToOneRelationships(Pageable pageable);

    @Query("select citizenTable from CitizenTable citizenTable left join fetch citizenTable.familyId")
    List<CitizenTable> findAllWithToOneRelationships();

    @Query("select citizenTable from CitizenTable citizenTable left join fetch citizenTable.familyId where citizenTable.id =:id")
    Optional<CitizenTable> findOneWithToOneRelationships(@Param("id") Long id);
}
