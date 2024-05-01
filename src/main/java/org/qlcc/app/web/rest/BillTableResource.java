package org.qlcc.app.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.qlcc.app.domain.BillTable;
import org.qlcc.app.repository.BillTableRepository;
import org.qlcc.app.service.BillTableService;
import org.qlcc.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.qlcc.app.domain.BillTable}.
 */
@RestController
@RequestMapping("/api/bill-tables")
public class BillTableResource {

    private final Logger log = LoggerFactory.getLogger(BillTableResource.class);

    private static final String ENTITY_NAME = "billTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillTableService billTableService;

    private final BillTableRepository billTableRepository;

    public BillTableResource(BillTableService billTableService, BillTableRepository billTableRepository) {
        this.billTableService = billTableService;
        this.billTableRepository = billTableRepository;
    }

    /**
     * {@code POST  /bill-tables} : Create a new billTable.
     *
     * @param billTable the billTable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billTable, or with status {@code 400 (Bad Request)} if the billTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BillTable> createBillTable(@Valid @RequestBody BillTable billTable) throws URISyntaxException {
        log.debug("REST request to save BillTable : {}", billTable);
        if (billTable.getId() != null) {
            throw new BadRequestAlertException("A new billTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        billTable = billTableService.save(billTable);
        return ResponseEntity.created(new URI("/api/bill-tables/" + billTable.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, billTable.getId().toString()))
            .body(billTable);
    }

    /**
     * {@code PUT  /bill-tables/:id} : Updates an existing billTable.
     *
     * @param id the id of the billTable to save.
     * @param billTable the billTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billTable,
     * or with status {@code 400 (Bad Request)} if the billTable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BillTable> updateBillTable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BillTable billTable
    ) throws URISyntaxException {
        log.debug("REST request to update BillTable : {}, {}", id, billTable);
        if (billTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        billTable = billTableService.update(billTable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billTable.getId().toString()))
            .body(billTable);
    }

    /**
     * {@code PATCH  /bill-tables/:id} : Partial updates given fields of an existing billTable, field will ignore if it is null
     *
     * @param id the id of the billTable to save.
     * @param billTable the billTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billTable,
     * or with status {@code 400 (Bad Request)} if the billTable is not valid,
     * or with status {@code 404 (Not Found)} if the billTable is not found,
     * or with status {@code 500 (Internal Server Error)} if the billTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillTable> partialUpdateBillTable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BillTable billTable
    ) throws URISyntaxException {
        log.debug("REST request to partial update BillTable partially : {}, {}", id, billTable);
        if (billTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillTable> result = billTableService.partialUpdate(billTable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billTable.getId().toString())
        );
    }

    /**
     * {@code GET  /bill-tables} : get all the billTables.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billTables in body.
     */
    @GetMapping("")
    public List<BillTable> getAllBillTables(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all BillTables");
        return billTableService.findAll();
    }

    /**
     * {@code GET  /bill-tables/:id} : get the "id" billTable.
     *
     * @param id the id of the billTable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billTable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BillTable> getBillTable(@PathVariable("id") Long id) {
        log.debug("REST request to get BillTable : {}", id);
        Optional<BillTable> billTable = billTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billTable);
    }

    /**
     * {@code DELETE  /bill-tables/:id} : delete the "id" billTable.
     *
     * @param id the id of the billTable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillTable(@PathVariable("id") Long id) {
        log.debug("REST request to delete BillTable : {}", id);
        billTableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
