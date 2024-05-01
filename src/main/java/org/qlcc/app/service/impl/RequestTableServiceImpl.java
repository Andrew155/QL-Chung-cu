package org.qlcc.app.service.impl;

import java.util.List;
import java.util.Optional;
import org.qlcc.app.domain.RequestTable;
import org.qlcc.app.repository.RequestTableRepository;
import org.qlcc.app.service.RequestTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.qlcc.app.domain.RequestTable}.
 */
@Service
@Transactional
public class RequestTableServiceImpl implements RequestTableService {

    private final Logger log = LoggerFactory.getLogger(RequestTableServiceImpl.class);

    private final RequestTableRepository requestTableRepository;

    public RequestTableServiceImpl(RequestTableRepository requestTableRepository) {
        this.requestTableRepository = requestTableRepository;
    }

    @Override
    public RequestTable save(RequestTable requestTable) {
        log.debug("Request to save RequestTable : {}", requestTable);
        return requestTableRepository.save(requestTable);
    }

    @Override
    public RequestTable update(RequestTable requestTable) {
        log.debug("Request to update RequestTable : {}", requestTable);
        return requestTableRepository.save(requestTable);
    }

    @Override
    public Optional<RequestTable> partialUpdate(RequestTable requestTable) {
        log.debug("Request to partially update RequestTable : {}", requestTable);

        return requestTableRepository
            .findById(requestTable.getId())
            .map(existingRequestTable -> {
                if (requestTable.getCreateAt() != null) {
                    existingRequestTable.setCreateAt(requestTable.getCreateAt());
                }
                if (requestTable.getUpdateAt() != null) {
                    existingRequestTable.setUpdateAt(requestTable.getUpdateAt());
                }
                if (requestTable.getDeletedAt() != null) {
                    existingRequestTable.setDeletedAt(requestTable.getDeletedAt());
                }
                if (requestTable.getStatus() != null) {
                    existingRequestTable.setStatus(requestTable.getStatus());
                }
                if (requestTable.getUserId() != null) {
                    existingRequestTable.setUserId(requestTable.getUserId());
                }
                if (requestTable.getTitle() != null) {
                    existingRequestTable.setTitle(requestTable.getTitle());
                }
                if (requestTable.getMessage() != null) {
                    existingRequestTable.setMessage(requestTable.getMessage());
                }
                if (requestTable.getReply() != null) {
                    existingRequestTable.setReply(requestTable.getReply());
                }
                if (requestTable.getNote() != null) {
                    existingRequestTable.setNote(requestTable.getNote());
                }

                return existingRequestTable;
            })
            .map(requestTableRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestTable> findAll() {
        log.debug("Request to get all RequestTables");
        return requestTableRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RequestTable> findOne(Integer id) {
        log.debug("Request to get RequestTable : {}", id);
        return requestTableRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete RequestTable : {}", id);
        requestTableRepository.deleteById(id);
    }
}
