package org.qlcc.app.service.impl;

import java.util.Optional;
import org.qlcc.app.domain.DonationTable;
import org.qlcc.app.repository.DonationTableRepository;
import org.qlcc.app.service.DonationTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.qlcc.app.domain.DonationTable}.
 */
@Service
@Transactional
public class DonationTableServiceImpl implements DonationTableService {

    private final Logger log = LoggerFactory.getLogger(DonationTableServiceImpl.class);

    private final DonationTableRepository donationTableRepository;

    public DonationTableServiceImpl(DonationTableRepository donationTableRepository) {
        this.donationTableRepository = donationTableRepository;
    }

    @Override
    public DonationTable save(DonationTable donationTable) {
        log.debug("Request to save DonationTable : {}", donationTable);
        return donationTableRepository.save(donationTable);
    }

    @Override
    public DonationTable update(DonationTable donationTable) {
        log.debug("Request to update DonationTable : {}", donationTable);
        return donationTableRepository.save(donationTable);
    }

    @Override
    public Optional<DonationTable> partialUpdate(DonationTable donationTable) {
        log.debug("Request to partially update DonationTable : {}", donationTable);

        return donationTableRepository
            .findById(donationTable.getId())
            .map(existingDonationTable -> {
                if (donationTable.getCreateAt() != null) {
                    existingDonationTable.setCreateAt(donationTable.getCreateAt());
                }
                if (donationTable.getUpdateAt() != null) {
                    existingDonationTable.setUpdateAt(donationTable.getUpdateAt());
                }
                if (donationTable.getDeletedAt() != null) {
                    existingDonationTable.setDeletedAt(donationTable.getDeletedAt());
                }
                if (donationTable.getDonationId() != null) {
                    existingDonationTable.setDonationId(donationTable.getDonationId());
                }
                if (donationTable.getDonationType() != null) {
                    existingDonationTable.setDonationType(donationTable.getDonationType());
                }
                if (donationTable.getDonationDesc() != null) {
                    existingDonationTable.setDonationDesc(donationTable.getDonationDesc());
                }
                if (donationTable.getDonationMonth() != null) {
                    existingDonationTable.setDonationMonth(donationTable.getDonationMonth());
                }
                if (donationTable.getDonationCost() != null) {
                    existingDonationTable.setDonationCost(donationTable.getDonationCost());
                }
                if (donationTable.getRoomId() != null) {
                    existingDonationTable.setRoomId(donationTable.getRoomId());
                }
                if (donationTable.getStatus() != null) {
                    existingDonationTable.setStatus(donationTable.getStatus());
                }

                return existingDonationTable;
            })
            .map(donationTableRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DonationTable> findAll(Pageable pageable) {
        log.debug("Request to get all DonationTables");
        return donationTableRepository.findAll(pageable);
    }

    public Page<DonationTable> findAllWithEagerRelationships(Pageable pageable) {
        return donationTableRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DonationTable> findOne(Long id) {
        log.debug("Request to get DonationTable : {}", id);
        return donationTableRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DonationTable : {}", id);
        donationTableRepository.deleteById(id);
    }
}
