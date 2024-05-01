package org.qlcc.app.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.qlcc.app.domain.RoomTable;
import org.qlcc.app.repository.RoomTableRepository;
import org.qlcc.app.service.RoomTableService;
import org.qlcc.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.qlcc.app.domain.RoomTable}.
 */
@RestController
@RequestMapping("/api/room-tables")
public class RoomTableResource {

    private final Logger log = LoggerFactory.getLogger(RoomTableResource.class);

    private static final String ENTITY_NAME = "roomTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomTableService roomTableService;

    private final RoomTableRepository roomTableRepository;

    public RoomTableResource(RoomTableService roomTableService, RoomTableRepository roomTableRepository) {
        this.roomTableService = roomTableService;
        this.roomTableRepository = roomTableRepository;
    }

    /**
     * {@code POST  /room-tables} : Create a new roomTable.
     *
     * @param roomTable the roomTable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomTable, or with status {@code 400 (Bad Request)} if the roomTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RoomTable> createRoomTable(@Valid @RequestBody RoomTable roomTable) throws URISyntaxException {
        log.debug("REST request to save RoomTable : {}", roomTable);
        if (roomTable.getId() != null) {
            throw new BadRequestAlertException("A new roomTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        roomTable = roomTableService.save(roomTable);
        return ResponseEntity.created(new URI("/api/room-tables/" + roomTable.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, roomTable.getId().toString()))
            .body(roomTable);
    }

    /**
     * {@code PUT  /room-tables/:id} : Updates an existing roomTable.
     *
     * @param id the id of the roomTable to save.
     * @param roomTable the roomTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomTable,
     * or with status {@code 400 (Bad Request)} if the roomTable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoomTable> updateRoomTable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RoomTable roomTable
    ) throws URISyntaxException {
        log.debug("REST request to update RoomTable : {}, {}", id, roomTable);
        if (roomTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        roomTable = roomTableService.update(roomTable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomTable.getId().toString()))
            .body(roomTable);
    }

    /**
     * {@code PATCH  /room-tables/:id} : Partial updates given fields of an existing roomTable, field will ignore if it is null
     *
     * @param id the id of the roomTable to save.
     * @param roomTable the roomTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomTable,
     * or with status {@code 400 (Bad Request)} if the roomTable is not valid,
     * or with status {@code 404 (Not Found)} if the roomTable is not found,
     * or with status {@code 500 (Internal Server Error)} if the roomTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoomTable> partialUpdateRoomTable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RoomTable roomTable
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoomTable partially : {}, {}", id, roomTable);
        if (roomTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoomTable> result = roomTableService.partialUpdate(roomTable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomTable.getId().toString())
        );
    }

    /**
     * {@code GET  /room-tables} : get all the roomTables.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomTables in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RoomTable>> getAllRoomTables(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("citizentable-is-null".equals(filter)) {
            log.debug("REST request to get all RoomTables where citizenTable is null");
            return new ResponseEntity<>(roomTableService.findAllWhereCitizenTableIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of RoomTables");
        Page<RoomTable> page = roomTableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /room-tables/:id} : get the "id" roomTable.
     *
     * @param id the id of the roomTable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomTable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomTable> getRoomTable(@PathVariable("id") Long id) {
        log.debug("REST request to get RoomTable : {}", id);
        Optional<RoomTable> roomTable = roomTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomTable);
    }

    /**
     * {@code DELETE  /room-tables/:id} : delete the "id" roomTable.
     *
     * @param id the id of the roomTable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomTable(@PathVariable("id") Long id) {
        log.debug("REST request to delete RoomTable : {}", id);
        roomTableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
