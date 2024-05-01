package org.qlcc.app.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.qlcc.app.domain.CitizenTable;
import org.qlcc.app.repository.CitizenTableRepository;
import org.qlcc.app.service.CitizenTableService;
import org.qlcc.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.qlcc.app.domain.CitizenTable}.
 */
@RestController
@RequestMapping("/api/citizen-tables")
public class CitizenTableResource {

    private final Logger log = LoggerFactory.getLogger(CitizenTableResource.class);

    private static final String ENTITY_NAME = "citizenTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CitizenTableService citizenTableService;

    private final CitizenTableRepository citizenTableRepository;

    public CitizenTableResource(CitizenTableService citizenTableService, CitizenTableRepository citizenTableRepository) {
        this.citizenTableService = citizenTableService;
        this.citizenTableRepository = citizenTableRepository;
    }

    /**
     * {@code POST  /citizen-tables} : Create a new citizenTable.
     *
     * @param citizenTable the citizenTable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new citizenTable, or with status {@code 400 (Bad Request)} if the citizenTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CitizenTable> createCitizenTable(@Valid @RequestBody CitizenTable citizenTable) throws URISyntaxException {
        log.debug("REST request to save CitizenTable : {}", citizenTable);
        if (citizenTable.getId() != null) {
            throw new BadRequestAlertException("A new citizenTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        citizenTable = citizenTableService.save(citizenTable);
        return ResponseEntity.created(new URI("/api/citizen-tables/" + citizenTable.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, citizenTable.getId().toString()))
            .body(citizenTable);
    }

    /**
     * {@code PUT  /citizen-tables/:id} : Updates an existing citizenTable.
     *
     * @param id the id of the citizenTable to save.
     * @param citizenTable the citizenTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated citizenTable,
     * or with status {@code 400 (Bad Request)} if the citizenTable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the citizenTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CitizenTable> updateCitizenTable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CitizenTable citizenTable
    ) throws URISyntaxException {
        log.debug("REST request to update CitizenTable : {}, {}", id, citizenTable);
        if (citizenTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, citizenTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citizenTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        citizenTable = citizenTableService.update(citizenTable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, citizenTable.getId().toString()))
            .body(citizenTable);
    }

    /**
     * {@code PATCH  /citizen-tables/:id} : Partial updates given fields of an existing citizenTable, field will ignore if it is null
     *
     * @param id the id of the citizenTable to save.
     * @param citizenTable the citizenTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated citizenTable,
     * or with status {@code 400 (Bad Request)} if the citizenTable is not valid,
     * or with status {@code 404 (Not Found)} if the citizenTable is not found,
     * or with status {@code 500 (Internal Server Error)} if the citizenTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CitizenTable> partialUpdateCitizenTable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CitizenTable citizenTable
    ) throws URISyntaxException {
        log.debug("REST request to partial update CitizenTable partially : {}, {}", id, citizenTable);
        if (citizenTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, citizenTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citizenTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CitizenTable> result = citizenTableService.partialUpdate(citizenTable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, citizenTable.getId().toString())
        );
    }

    /**
     * {@code GET  /citizen-tables} : get all the citizenTables.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of citizenTables in body.
     */
    @GetMapping("")
    public List<CitizenTable> getAllCitizenTables(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all CitizenTables");
        return citizenTableService.findAll();
    }

    /**
     * {@code GET  /citizen-tables/:id} : get the "id" citizenTable.
     *
     * @param id the id of the citizenTable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the citizenTable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CitizenTable> getCitizenTable(@PathVariable("id") Long id) {
        log.debug("REST request to get CitizenTable : {}", id);
        Optional<CitizenTable> citizenTable = citizenTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(citizenTable);
    }

    /**
     * {@code DELETE  /citizen-tables/:id} : delete the "id" citizenTable.
     *
     * @param id the id of the citizenTable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCitizenTable(@PathVariable("id") Long id) {
        log.debug("REST request to delete CitizenTable : {}", id);
        citizenTableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
