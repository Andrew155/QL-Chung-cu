package org.qlcc.app.service.impl;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.BillTable;
import org.qlcc.app.repository.BillTableRepository;
import org.qlcc.app.service.BillTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.qlcc.app.domain.BillTable}.
 */
@Service
@Transactional
public class BillTableServiceImpl implements BillTableService {

    private final Logger log = LoggerFactory.getLogger(BillTableServiceImpl.class);

    private final BillTableRepository billTableRepository;

    public BillTableServiceImpl(BillTableRepository billTableRepository) {
        this.billTableRepository = billTableRepository;
    }

    @Override
    public BillTable save(BillTable billTable) {
        log.debug("Request to save BillTable : {}", billTable);
        return billTableRepository.save(billTable);
    }

    @Override
    public BillTable update(BillTable billTable) {
        log.debug("Request to update BillTable : {}", billTable);
        return billTableRepository.save(billTable);
    }

    @Override
    public Optional<BillTable> partialUpdate(BillTable billTable) {
        log.debug("Request to partially update BillTable : {}", billTable);

        return billTableRepository
            .findById(billTable.getId())
            .map(existingBillTable -> {
                if (billTable.getCreateAt() != null) {
                    existingBillTable.setCreateAt(billTable.getCreateAt());
                }
                if (billTable.getUpdateAt() != null) {
                    existingBillTable.setUpdateAt(billTable.getUpdateAt());
                }
                if (billTable.getDeletedAt() != null) {
                    existingBillTable.setDeletedAt(billTable.getDeletedAt());
                }
                if (billTable.getBillType() != null) {
                    existingBillTable.setBillType(billTable.getBillType());
                }
                if (billTable.getBillId() != null) {
                    existingBillTable.setBillId(billTable.getBillId());
                }
                if (billTable.getBillMonth() != null) {
                    existingBillTable.setBillMonth(billTable.getBillMonth());
                }
                if (billTable.getBillAmount() != null) {
                    existingBillTable.setBillAmount(billTable.getBillAmount());
                }
                if (billTable.getRoomId() != null) {
                    existingBillTable.setRoomId(billTable.getRoomId());
                }
                if (billTable.getDate() != null) {
                    existingBillTable.setDate(billTable.getDate());
                }
                if (billTable.getStatus() != null) {
                    existingBillTable.setStatus(billTable.getStatus());
                }
                if (billTable.getBillCost() != null) {
                    existingBillTable.setBillCost(billTable.getBillCost());
                }
                if (billTable.getCustomerID() != null) {
                    existingBillTable.setCustomerID(billTable.getCustomerID());
                }

                return existingBillTable;
            })
            .map(billTableRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillTable> findAll() {
        log.debug("Request to get all BillTables");
        return billTableRepository.findAll();
    }

    public Page<BillTable> findAllWithEagerRelationships(Pageable pageable) {
        return billTableRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillTable> findOne(Long id) {
        log.debug("Request to get BillTable : {}", id);
        return billTableRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillTable : {}", id);
        billTableRepository.deleteById(id);
    }
}
