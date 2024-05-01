package org.qlcc.app.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.qlcc.app.domain.DonationTable;
import org.qlcc.app.repository.DonationTableRepository;
import org.qlcc.app.service.DonationTableService;
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
 * REST controller for managing {@link org.qlcc.app.domain.DonationTable}.
 */
@RestController
@RequestMapping("/api/donation-tables")
public class DonationTableResource {

    private final Logger log = LoggerFactory.getLogger(DonationTableResource.class);

    private static final String ENTITY_NAME = "donationTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DonationTableService donationTableService;

    private final DonationTableRepository donationTableRepository;

    public DonationTableResource(DonationTableService donationTableService, DonationTableRepository donationTableRepository) {
        this.donationTableService = donationTableService;
        this.donationTableRepository = donationTableRepository;
    }

    /**
     * {@code POST  /donation-tables} : Create a new donationTable.
     *
     * @param donationTable the donationTable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new donationTable, or with status {@code 400 (Bad Request)} if the donationTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DonationTable> createDonationTable(@Valid @RequestBody DonationTable donationTable) throws URISyntaxException {
        log.debug("REST request to save DonationTable : {}", donationTable);
        if (donationTable.getId() != null) {
            throw new BadRequestAlertException("A new donationTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        donationTable = donationTableService.save(donationTable);
        return ResponseEntity.created(new URI("/api/donation-tables/" + donationTable.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, donationTable.getId().toString()))
            .body(donationTable);
    }

    /**
     * {@code PUT  /donation-tables/:id} : Updates an existing donationTable.
     *
     * @param id the id of the donationTable to save.
     * @param donationTable the donationTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donationTable,
     * or with status {@code 400 (Bad Request)} if the donationTable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the donationTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DonationTable> updateDonationTable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DonationTable donationTable
    ) throws URISyntaxException {
        log.debug("REST request to update DonationTable : {}, {}", id, donationTable);
        if (donationTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donationTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donationTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        donationTable = donationTableService.update(donationTable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donationTable.getId().toString()))
            .body(donationTable);
    }

    /**
     * {@code PATCH  /donation-tables/:id} : Partial updates given fields of an existing donationTable, field will ignore if it is null
     *
     * @param id the id of the donationTable to save.
     * @param donationTable the donationTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donationTable,
     * or with status {@code 400 (Bad Request)} if the donationTable is not valid,
     * or with status {@code 404 (Not Found)} if the donationTable is not found,
     * or with status {@code 500 (Internal Server Error)} if the donationTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DonationTable> partialUpdateDonationTable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DonationTable donationTable
    ) throws URISyntaxException {
        log.debug("REST request to partial update DonationTable partially : {}, {}", id, donationTable);
        if (donationTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donationTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donationTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DonationTable> result = donationTableService.partialUpdate(donationTable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donationTable.getId().toString())
        );
    }

    /**
     * {@code GET  /donation-tables} : get all the donationTables.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of donationTables in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DonationTable>> getAllDonationTables(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of DonationTables");
        Page<DonationTable> page;
        if (eagerload) {
            page = donationTableService.findAllWithEagerRelationships(pageable);
        } else {
            page = donationTableService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /donation-tables/:id} : get the "id" donationTable.
     *
     * @param id the id of the donationTable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donationTable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DonationTable> getDonationTable(@PathVariable("id") Long id) {
        log.debug("REST request to get DonationTable : {}", id);
        Optional<DonationTable> donationTable = donationTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(donationTable);
    }

    /**
     * {@code DELETE  /donation-tables/:id} : delete the "id" donationTable.
     *
     * @param id the id of the donationTable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonationTable(@PathVariable("id") Long id) {
        log.debug("REST request to delete DonationTable : {}", id);
        donationTableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
