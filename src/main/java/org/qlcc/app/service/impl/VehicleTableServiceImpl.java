package org.qlcc.app.service.impl;

import java.util.Optional;
import org.qlcc.app.domain.VehicleTable;
import org.qlcc.app.repository.VehicleTableRepository;
import org.qlcc.app.service.VehicleTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.qlcc.app.domain.VehicleTable}.
 */
@Service
@Transactional
public class VehicleTableServiceImpl implements VehicleTableService {

    private final Logger log = LoggerFactory.getLogger(VehicleTableServiceImpl.class);

    private final VehicleTableRepository vehicleTableRepository;

    public VehicleTableServiceImpl(VehicleTableRepository vehicleTableRepository) {
        this.vehicleTableRepository = vehicleTableRepository;
    }

    @Override
    public VehicleTable save(VehicleTable vehicleTable) {
        log.debug("Request to save VehicleTable : {}", vehicleTable);
        return vehicleTableRepository.save(vehicleTable);
    }

    @Override
    public VehicleTable update(VehicleTable vehicleTable) {
        log.debug("Request to update VehicleTable : {}", vehicleTable);
        return vehicleTableRepository.save(vehicleTable);
    }

    @Override
    public Optional<VehicleTable> partialUpdate(VehicleTable vehicleTable) {
        log.debug("Request to partially update VehicleTable : {}", vehicleTable);

        return vehicleTableRepository
            .findById(vehicleTable.getId())
            .map(existingVehicleTable -> {
                if (vehicleTable.getCreateAt() != null) {
                    existingVehicleTable.setCreateAt(vehicleTable.getCreateAt());
                }
                if (vehicleTable.getUpdateAt() != null) {
                    existingVehicleTable.setUpdateAt(vehicleTable.getUpdateAt());
                }
                if (vehicleTable.getDeletedAt() != null) {
                    existingVehicleTable.setDeletedAt(vehicleTable.getDeletedAt());
                }
                if (vehicleTable.getVehicleName() != null) {
                    existingVehicleTable.setVehicleName(vehicleTable.getVehicleName());
                }
                if (vehicleTable.getVehicleType() != null) {
                    existingVehicleTable.setVehicleType(vehicleTable.getVehicleType());
                }
                if (vehicleTable.getVehicleId() != null) {
                    existingVehicleTable.setVehicleId(vehicleTable.getVehicleId());
                }
                if (vehicleTable.getRoomId() != null) {
                    existingVehicleTable.setRoomId(vehicleTable.getRoomId());
                }
                if (vehicleTable.getOwnerId() != null) {
                    existingVehicleTable.setOwnerId(vehicleTable.getOwnerId());
                }
                if (vehicleTable.getVehicleFee() != null) {
                    existingVehicleTable.setVehicleFee(vehicleTable.getVehicleFee());
                }

                return existingVehicleTable;
            })
            .map(vehicleTableRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleTable> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleTables");
        return vehicleTableRepository.findAll(pageable);
    }

    public Page<VehicleTable> findAllWithEagerRelationships(Pageable pageable) {
        return vehicleTableRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleTable> findOne(Long id) {
        log.debug("Request to get VehicleTable : {}", id);
        return vehicleTableRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VehicleTable : {}", id);
        vehicleTableRepository.deleteById(id);
    }
}
