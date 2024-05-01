package org.qlcc.app.service.impl;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.CitizenTable;
import org.qlcc.app.repository.CitizenTableRepository;
import org.qlcc.app.service.CitizenTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.qlcc.app.domain.CitizenTable}.
 */
@Service
@Transactional
public class CitizenTableServiceImpl implements CitizenTableService {

    private final Logger log = LoggerFactory.getLogger(CitizenTableServiceImpl.class);

    private final CitizenTableRepository citizenTableRepository;

    public CitizenTableServiceImpl(CitizenTableRepository citizenTableRepository) {
        this.citizenTableRepository = citizenTableRepository;
    }

    @Override
    public CitizenTable save(CitizenTable citizenTable) {
        log.debug("Request to save CitizenTable : {}", citizenTable);
        return citizenTableRepository.save(citizenTable);
    }

    @Override
    public CitizenTable update(CitizenTable citizenTable) {
        log.debug("Request to update CitizenTable : {}", citizenTable);
        return citizenTableRepository.save(citizenTable);
    }

    @Override
    public Optional<CitizenTable> partialUpdate(CitizenTable citizenTable) {
        log.debug("Request to partially update CitizenTable : {}", citizenTable);

        return citizenTableRepository
            .findById(citizenTable.getId())
            .map(existingCitizenTable -> {
                if (citizenTable.getCreateAt() != null) {
                    existingCitizenTable.setCreateAt(citizenTable.getCreateAt());
                }
                if (citizenTable.getUpdateAt() != null) {
                    existingCitizenTable.setUpdateAt(citizenTable.getUpdateAt());
                }
                if (citizenTable.getDeletedAt() != null) {
                    existingCitizenTable.setDeletedAt(citizenTable.getDeletedAt());
                }
                if (citizenTable.getCitizenID() != null) {
                    existingCitizenTable.setCitizenID(citizenTable.getCitizenID());
                }
                if (citizenTable.getName() != null) {
                    existingCitizenTable.setName(citizenTable.getName());
                }
                if (citizenTable.getDob() != null) {
                    existingCitizenTable.setDob(citizenTable.getDob());
                }
                if (citizenTable.getContact() != null) {
                    existingCitizenTable.setContact(citizenTable.getContact());
                }
                if (citizenTable.getGender() != null) {
                    existingCitizenTable.setGender(citizenTable.getGender());
                }
                if (citizenTable.getRelation() != null) {
                    existingCitizenTable.setRelation(citizenTable.getRelation());
                }
                if (citizenTable.getStatus() != null) {
                    existingCitizenTable.setStatus(citizenTable.getStatus());
                }

                return existingCitizenTable;
            })
            .map(citizenTableRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitizenTable> findAll() {
        log.debug("Request to get all CitizenTables");
        return citizenTableRepository.findAll();
    }

    public Page<CitizenTable> findAllWithEagerRelationships(Pageable pageable) {
        return citizenTableRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CitizenTable> findOne(Long id) {
        log.debug("Request to get CitizenTable : {}", id);
        return citizenTableRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CitizenTable : {}", id);
        citizenTableRepository.deleteById(id);
    }
}
