package org.qlcc.app.repository;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.VehicleTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleTable entity.
 */
@Repository
public interface VehicleTableRepository extends JpaRepository<VehicleTable, Long> {
    default Optional<VehicleTable> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<VehicleTable> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<VehicleTable> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select vehicleTable from VehicleTable vehicleTable left join fetch vehicleTable.roomTable",
        countQuery = "select count(vehicleTable) from VehicleTable vehicleTable"
    )
    Page<VehicleTable> findAllWithToOneRelationships(Pageable pageable);

    @Query("select vehicleTable from VehicleTable vehicleTable left join fetch vehicleTable.roomTable")
    List<VehicleTable> findAllWithToOneRelationships();

    @Query("select vehicleTable from VehicleTable vehicleTable left join fetch vehicleTable.roomTable where vehicleTable.id =:id")
    Optional<VehicleTable> findOneWithToOneRelationships(@Param("id") Long id);
}
