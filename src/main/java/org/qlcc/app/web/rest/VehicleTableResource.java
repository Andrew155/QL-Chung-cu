package org.qlcc.app.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.qlcc.app.domain.VehicleTable;
import org.qlcc.app.repository.VehicleTableRepository;
import org.qlcc.app.service.VehicleTableService;
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
 * REST controller for managing {@link org.qlcc.app.domain.VehicleTable}.
 */
@RestController
@RequestMapping("/api/vehicle-tables")
public class VehicleTableResource {

    private final Logger log = LoggerFactory.getLogger(VehicleTableResource.class);

    private static final String ENTITY_NAME = "vehicleTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleTableService vehicleTableService;

    private final VehicleTableRepository vehicleTableRepository;

    public VehicleTableResource(VehicleTableService vehicleTableService, VehicleTableRepository vehicleTableRepository) {
        this.vehicleTableService = vehicleTableService;
        this.vehicleTableRepository = vehicleTableRepository;
    }

    /**
     * {@code POST  /vehicle-tables} : Create a new vehicleTable.
     *
     * @param vehicleTable the vehicleTable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleTable, or with status {@code 400 (Bad Request)} if the vehicleTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleTable> createVehicleTable(@Valid @RequestBody VehicleTable vehicleTable) throws URISyntaxException {
        log.debug("REST request to save VehicleTable : {}", vehicleTable);
        if (vehicleTable.getId() != null) {
            throw new BadRequestAlertException("A new vehicleTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleTable = vehicleTableService.save(vehicleTable);
        return ResponseEntity.created(new URI("/api/vehicle-tables/" + vehicleTable.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleTable.getId().toString()))
            .body(vehicleTable);
    }

    /**
     * {@code PUT  /vehicle-tables/:id} : Updates an existing vehicleTable.
     *
     * @param id the id of the vehicleTable to save.
     * @param vehicleTable the vehicleTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleTable,
     * or with status {@code 400 (Bad Request)} if the vehicleTable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleTable> updateVehicleTable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleTable vehicleTable
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleTable : {}, {}", id, vehicleTable);
        if (vehicleTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleTable = vehicleTableService.update(vehicleTable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleTable.getId().toString()))
            .body(vehicleTable);
    }

    /**
     * {@code PATCH  /vehicle-tables/:id} : Partial updates given fields of an existing vehicleTable, field will ignore if it is null
     *
     * @param id the id of the vehicleTable to save.
     * @param vehicleTable the vehicleTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleTable,
     * or with status {@code 400 (Bad Request)} if the vehicleTable is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleTable is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleTable> partialUpdateVehicleTable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehicleTable vehicleTable
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleTable partially : {}, {}", id, vehicleTable);
        if (vehicleTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleTable> result = vehicleTableService.partialUpdate(vehicleTable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleTable.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-tables} : get all the vehicleTables.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleTables in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleTable>> getAllVehicleTables(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of VehicleTables");
        Page<VehicleTable> page;
        if (eagerload) {
            page = vehicleTableService.findAllWithEagerRelationships(pageable);
        } else {
            page = vehicleTableService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-tables/:id} : get the "id" vehicleTable.
     *
     * @param id the id of the vehicleTable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleTable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleTable> getVehicleTable(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleTable : {}", id);
        Optional<VehicleTable> vehicleTable = vehicleTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleTable);
    }

    /**
     * {@code DELETE  /vehicle-tables/:id} : delete the "id" vehicleTable.
     *
     * @param id the id of the vehicleTable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleTable(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleTable : {}", id);
        vehicleTableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
