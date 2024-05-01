package org.qlcc.app.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.qlcc.app.domain.RoomTable;
import org.qlcc.app.repository.RoomTableRepository;
import org.qlcc.app.service.RoomTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.qlcc.app.domain.RoomTable}.
 */
@Service
@Transactional
public class RoomTableServiceImpl implements RoomTableService {

    private final Logger log = LoggerFactory.getLogger(RoomTableServiceImpl.class);

    private final RoomTableRepository roomTableRepository;

    public RoomTableServiceImpl(RoomTableRepository roomTableRepository) {
        this.roomTableRepository = roomTableRepository;
    }

    @Override
    public RoomTable save(RoomTable roomTable) {
        log.debug("Request to save RoomTable : {}", roomTable);
        return roomTableRepository.save(roomTable);
    }

    @Override
    public RoomTable update(RoomTable roomTable) {
        log.debug("Request to update RoomTable : {}", roomTable);
        return roomTableRepository.save(roomTable);
    }

    @Override
    public Optional<RoomTable> partialUpdate(RoomTable roomTable) {
        log.debug("Request to partially update RoomTable : {}", roomTable);

        return roomTableRepository
            .findById(roomTable.getId())
            .map(existingRoomTable -> {
                if (roomTable.getCreateAt() != null) {
                    existingRoomTable.setCreateAt(roomTable.getCreateAt());
                }
                if (roomTable.getUpdateAt() != null) {
                    existingRoomTable.setUpdateAt(roomTable.getUpdateAt());
                }
                if (roomTable.getDeletedAt() != null) {
                    existingRoomTable.setDeletedAt(roomTable.getDeletedAt());
                }
                if (roomTable.getRoomId() != null) {
                    existingRoomTable.setRoomId(roomTable.getRoomId());
                }
                if (roomTable.getArea() != null) {
                    existingRoomTable.setArea(roomTable.getArea());
                }
                if (roomTable.getOwnTime() != null) {
                    existingRoomTable.setOwnTime(roomTable.getOwnTime());
                }
                if (roomTable.getOwnerId() != null) {
                    existingRoomTable.setOwnerId(roomTable.getOwnerId());
                }
                if (roomTable.getOwnerName() != null) {
                    existingRoomTable.setOwnerName(roomTable.getOwnerName());
                }
                if (roomTable.getStatus() != null) {
                    existingRoomTable.setStatus(roomTable.getStatus());
                }

                return existingRoomTable;
            })
            .map(roomTableRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomTable> findAll(Pageable pageable) {
        log.debug("Request to get all RoomTables");
        return roomTableRepository.findAll(pageable);
    }

    /**
     *  Get all the roomTables where CitizenTable is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RoomTable> findAllWhereCitizenTableIsNull() {
        log.debug("Request to get all roomTables where CitizenTable is null");
        return StreamSupport.stream(roomTableRepository.findAll().spliterator(), false)
            .filter(roomTable -> roomTable.getCitizenTable() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomTable> findOne(Long id) {
        log.debug("Request to get RoomTable : {}", id);
        return roomTableRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RoomTable : {}", id);
        roomTableRepository.deleteById(id);
    }
}
