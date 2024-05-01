package org.qlcc.app.service.impl;

import java.util.Optional;
import org.qlcc.app.domain.FeeTable;
import org.qlcc.app.repository.FeeTableRepository;
import org.qlcc.app.service.FeeTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.qlcc.app.domain.FeeTable}.
 */
@Service
@Transactional
public class FeeTableServiceImpl implements FeeTableService {

    private final Logger log = LoggerFactory.getLogger(FeeTableServiceImpl.class);

    private final FeeTableRepository feeTableRepository;

    public FeeTableServiceImpl(FeeTableRepository feeTableRepository) {
        this.feeTableRepository = feeTableRepository;
    }

    @Override
    public FeeTable save(FeeTable feeTable) {
        log.debug("Request to save FeeTable : {}", feeTable);
        return feeTableRepository.save(feeTable);
    }

    @Override
    public FeeTable update(FeeTable feeTable) {
        log.debug("Request to update FeeTable : {}", feeTable);
        return feeTableRepository.save(feeTable);
    }

    @Override
    public Optional<FeeTable> partialUpdate(FeeTable feeTable) {
        log.debug("Request to partially update FeeTable : {}", feeTable);

        return feeTableRepository
            .findById(feeTable.getId())
            .map(existingFeeTable -> {
                if (feeTable.getCreateAt() != null) {
                    existingFeeTable.setCreateAt(feeTable.getCreateAt());
                }
                if (feeTable.getUpdateAt() != null) {
                    existingFeeTable.setUpdateAt(feeTable.getUpdateAt());
                }
                if (feeTable.getDeletedAt() != null) {
                    existingFeeTable.setDeletedAt(feeTable.getDeletedAt());
                }
                if (feeTable.getFeeType() != null) {
                    existingFeeTable.setFeeType(feeTable.getFeeType());
                }
                if (feeTable.getFeeDesc() != null) {
                    existingFeeTable.setFeeDesc(feeTable.getFeeDesc());
                }
                if (feeTable.getFeeMonth() != null) {
                    existingFeeTable.setFeeMonth(feeTable.getFeeMonth());
                }
                if (feeTable.getFeeCost() != null) {
                    existingFeeTable.setFeeCost(feeTable.getFeeCost());
                }
                if (feeTable.getDate() != null) {
                    existingFeeTable.setDate(feeTable.getDate());
                }
                if (feeTable.getStatus() != null) {
                    existingFeeTable.setStatus(feeTable.getStatus());
                }
                if (feeTable.getFeeId() != null) {
                    existingFeeTable.setFeeId(feeTable.getFeeId());
                }

                return existingFeeTable;
            })
            .map(feeTableRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeeTable> findAll(Pageable pageable) {
        log.debug("Request to get all FeeTables");
        return feeTableRepository.findAll(pageable);
    }

    public Page<FeeTable> findAllWithEagerRelationships(Pageable pageable) {
        return feeTableRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FeeTable> findOne(Long id) {
        log.debug("Request to get FeeTable : {}", id);
        return feeTableRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FeeTable : {}", id);
        feeTableRepository.deleteById(id);
    }
}
