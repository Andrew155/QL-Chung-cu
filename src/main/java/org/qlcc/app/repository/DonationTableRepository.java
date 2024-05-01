package org.qlcc.app.repository;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.DonationTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DonationTable entity.
 */
@Repository
public interface DonationTableRepository extends JpaRepository<DonationTable, Long> {
    default Optional<DonationTable> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DonationTable> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DonationTable> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select donationTable from DonationTable donationTable left join fetch donationTable.roomTable",
        countQuery = "select count(donationTable) from DonationTable donationTable"
    )
    Page<DonationTable> findAllWithToOneRelationships(Pageable pageable);

    @Query("select donationTable from DonationTable donationTable left join fetch donationTable.roomTable")
    List<DonationTable> findAllWithToOneRelationships();

    @Query("select donationTable from DonationTable donationTable left join fetch donationTable.roomTable where donationTable.id =:id")
    Optional<DonationTable> findOneWithToOneRelationships(@Param("id") Long id);
}
