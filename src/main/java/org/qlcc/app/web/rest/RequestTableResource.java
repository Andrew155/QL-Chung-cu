package org.qlcc.app.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.qlcc.app.domain.RequestTable;
import org.qlcc.app.repository.RequestTableRepository;
import org.qlcc.app.service.RequestTableService;
import org.qlcc.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.qlcc.app.domain.RequestTable}.
 */
@RestController
@RequestMapping("/api/request-tables")
public class RequestTableResource {

    private final Logger log = LoggerFactory.getLogger(RequestTableResource.class);

    private static final String ENTITY_NAME = "requestTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestTableService requestTableService;

    private final RequestTableRepository requestTableRepository;

    public RequestTableResource(RequestTableService requestTableService, RequestTableRepository requestTableRepository) {
        this.requestTableService = requestTableService;
        this.requestTableRepository = requestTableRepository;
    }

    /**
     * {@code POST  /request-tables} : Create a new requestTable.
     *
     * @param requestTable the requestTable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requestTable, or with status {@code 400 (Bad Request)} if the requestTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RequestTable> createRequestTable(@Valid @RequestBody RequestTable requestTable) throws URISyntaxException {
        log.debug("REST request to save RequestTable : {}", requestTable);
        if (requestTable.getId() != null) {
            throw new BadRequestAlertException("A new requestTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        requestTable = requestTableService.save(requestTable);
        return ResponseEntity.created(new URI("/api/request-tables/" + requestTable.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, requestTable.getId().toString()))
            .body(requestTable);
    }

    /**
     * {@code PUT  /request-tables/:id} : Updates an existing requestTable.
     *
     * @param id the id of the requestTable to save.
     * @param requestTable the requestTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestTable,
     * or with status {@code 400 (Bad Request)} if the requestTable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requestTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RequestTable> updateRequestTable(
        @PathVariable(value = "id", required = false) final Integer id,
        @Valid @RequestBody RequestTable requestTable
    ) throws URISyntaxException {
        log.debug("REST request to update RequestTable : {}, {}", id, requestTable);
        if (requestTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        requestTable = requestTableService.update(requestTable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requestTable.getId().toString()))
            .body(requestTable);
    }

    /**
     * {@code PATCH  /request-tables/:id} : Partial updates given fields of an existing requestTable, field will ignore if it is null
     *
     * @param id the id of the requestTable to save.
     * @param requestTable the requestTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestTable,
     * or with status {@code 400 (Bad Request)} if the requestTable is not valid,
     * or with status {@code 404 (Not Found)} if the requestTable is not found,
     * or with status {@code 500 (Internal Server Error)} if the requestTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RequestTable> partialUpdateRequestTable(
        @PathVariable(value = "id", required = false) final Integer id,
        @NotNull @RequestBody RequestTable requestTable
    ) throws URISyntaxException {
        log.debug("REST request to partial update RequestTable partially : {}, {}", id, requestTable);
        if (requestTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RequestTable> result = requestTableService.partialUpdate(requestTable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requestTable.getId().toString())
        );
    }

    /**
     * {@code GET  /request-tables} : get all the requestTables.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requestTables in body.
     */
    @GetMapping("")
    public List<RequestTable> getAllRequestTables() {
        log.debug("REST request to get all RequestTables");
        return requestTableService.findAll();
    }

    /**
     * {@code GET  /request-tables/:id} : get the "id" requestTable.
     *
     * @param id the id of the requestTable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requestTable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RequestTable> getRequestTable(@PathVariable("id") Integer id) {
        log.debug("REST request to get RequestTable : {}", id);
        Optional<RequestTable> requestTable = requestTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requestTable);
    }

    /**
     * {@code DELETE  /request-tables/:id} : delete the "id" requestTable.
     *
     * @param id the id of the requestTable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequestTable(@PathVariable("id") Integer id) {
        log.debug("REST request to delete RequestTable : {}", id);
        requestTableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
