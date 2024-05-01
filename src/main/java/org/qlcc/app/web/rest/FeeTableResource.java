package org.qlcc.app.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.qlcc.app.domain.FeeTable;
import org.qlcc.app.repository.FeeTableRepository;
import org.qlcc.app.service.FeeTableService;
import org.qlcc.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.qlcc.app.domain.FeeTable}.
 */
@RestController
@RequestMapping("/api/fee-tables")
public class FeeTableResource {

    private final Logger log = LoggerFactory.getLogger(FeeTableResource.class);

    private static final String ENTITY_NAME = "feeTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeeTableService feeTableService;

    private final FeeTableRepository feeTableRepository;

    public FeeTableResource(FeeTableService feeTableService, FeeTableRepository feeTableRepository) {
        this.feeTableService = feeTableService;
        this.feeTableRepository = feeTableRepository;
    }

    /**
     * {@code POST  /fee-tables} : Create a new feeTable.
     *
     * @param feeTable the feeTable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feeTable, or with status {@code 400 (Bad Request)} if the feeTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FeeTable> createFeeTable(@Valid @RequestBody FeeTable feeTable) throws URISyntaxException {
        log.debug("REST request to save FeeTable : {}", feeTable);
        if (feeTable.getId() != null) {
            throw new BadRequestAlertException("A new feeTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        feeTable = feeTableService.save(feeTable);
        return ResponseEntity.created(new URI("/api/fee-tables/" + feeTable.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, feeTable.getId().toString()))
            .body(feeTable);
    }

    /**
     * {@code PUT  /fee-tables/:id} : Updates an existing feeTable.
     *
     * @param id the id of the feeTable to save.
     * @param feeTable the feeTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feeTable,
     * or with status {@code 400 (Bad Request)} if the feeTable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feeTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeeTable> updateFeeTable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FeeTable feeTable
    ) throws URISyntaxException {
        log.debug("REST request to update FeeTable : {}, {}", id, feeTable);
        if (feeTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feeTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feeTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        feeTable = feeTableService.update(feeTable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feeTable.getId().toString()))
            .body(feeTable);
    }

    /**
     * {@code PATCH  /fee-tables/:id} : Partial updates given fields of an existing feeTable, field will ignore if it is null
     *
     * @param id the id of the feeTable to save.
     * @param feeTable the feeTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feeTable,
     * or with status {@code 400 (Bad Request)} if the feeTable is not valid,
     * or with status {@code 404 (Not Found)} if the feeTable is not found,
     * or with status {@code 500 (Internal Server Error)} if the feeTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeeTable> partialUpdateFeeTable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FeeTable feeTable
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeeTable partially : {}, {}", id, feeTable);
        if (feeTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feeTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feeTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeeTable> result = feeTableService.partialUpdate(feeTable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feeTable.getId().toString())
        );
    }

    /**
     * {@code GET  /fee-tables} : get all the feeTables.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feeTables in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FeeTable>> getAllFeeTables(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of FeeTables");
        Page<FeeTable> page;
        if (eagerload) {
            page = feeTableService.findAllWithEagerRelationships(pageable);
        } else {
            page = feeTableService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fee-tables/:id} : get the "id" feeTable.
     *
     * @param id the id of the feeTable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feeTable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeeTable> getFeeTable(@PathVariable("id") Long id) {
        log.debug("REST request to get FeeTable : {}", id);
        Optional<FeeTable> feeTable = feeTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feeTable);
    }

    /**
     * {@code DELETE  /fee-tables/:id} : delete the "id" feeTable.
     *
     * @param id the id of the feeTable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeeTable(@PathVariable("id") Long id) {
        log.debug("REST request to delete FeeTable : {}", id);
        feeTableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
