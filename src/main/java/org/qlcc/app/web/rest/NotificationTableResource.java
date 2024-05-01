package org.qlcc.app.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.qlcc.app.domain.NotificationTable;
import org.qlcc.app.repository.NotificationTableRepository;
import org.qlcc.app.service.NotificationTableService;
import org.qlcc.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.qlcc.app.domain.NotificationTable}.
 */
@RestController
@RequestMapping("/api/notification-tables")
public class NotificationTableResource {

    private final Logger log = LoggerFactory.getLogger(NotificationTableResource.class);

    private static final String ENTITY_NAME = "notificationTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationTableService notificationTableService;

    private final NotificationTableRepository notificationTableRepository;

    public NotificationTableResource(
        NotificationTableService notificationTableService,
        NotificationTableRepository notificationTableRepository
    ) {
        this.notificationTableService = notificationTableService;
        this.notificationTableRepository = notificationTableRepository;
    }

    /**
     * {@code POST  /notification-tables} : Create a new notificationTable.
     *
     * @param notificationTable the notificationTable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationTable, or with status {@code 400 (Bad Request)} if the notificationTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NotificationTable> createNotificationTable(@Valid @RequestBody NotificationTable notificationTable)
        throws URISyntaxException {
        log.debug("REST request to save NotificationTable : {}", notificationTable);
        if (notificationTable.getId() != null) {
            throw new BadRequestAlertException("A new notificationTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        notificationTable = notificationTableService.save(notificationTable);
        return ResponseEntity.created(new URI("/api/notification-tables/" + notificationTable.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, notificationTable.getId().toString()))
            .body(notificationTable);
    }

    /**
     * {@code PUT  /notification-tables/:id} : Updates an existing notificationTable.
     *
     * @param id the id of the notificationTable to save.
     * @param notificationTable the notificationTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationTable,
     * or with status {@code 400 (Bad Request)} if the notificationTable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificationTable> updateNotificationTable(
        @PathVariable(value = "id", required = false) final Integer id,
        @Valid @RequestBody NotificationTable notificationTable
    ) throws URISyntaxException {
        log.debug("REST request to update NotificationTable : {}, {}", id, notificationTable);
        if (notificationTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        notificationTable = notificationTableService.update(notificationTable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationTable.getId().toString()))
            .body(notificationTable);
    }

    /**
     * {@code PATCH  /notification-tables/:id} : Partial updates given fields of an existing notificationTable, field will ignore if it is null
     *
     * @param id the id of the notificationTable to save.
     * @param notificationTable the notificationTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationTable,
     * or with status {@code 400 (Bad Request)} if the notificationTable is not valid,
     * or with status {@code 404 (Not Found)} if the notificationTable is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificationTable> partialUpdateNotificationTable(
        @PathVariable(value = "id", required = false) final Integer id,
        @NotNull @RequestBody NotificationTable notificationTable
    ) throws URISyntaxException {
        log.debug("REST request to partial update NotificationTable partially : {}, {}", id, notificationTable);
        if (notificationTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificationTable> result = notificationTableService.partialUpdate(notificationTable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationTable.getId().toString())
        );
    }

    /**
     * {@code GET  /notification-tables} : get all the notificationTables.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationTables in body.
     */
    @GetMapping("")
    public List<NotificationTable> getAllNotificationTables(@RequestParam(name = "filter", required = false) String filter) {
        if ("requesttable-is-null".equals(filter)) {
            log.debug("REST request to get all NotificationTables where requestTable is null");
            return notificationTableService.findAllWhereRequestTableIsNull();
        }
        log.debug("REST request to get all NotificationTables");
        return notificationTableService.findAll();
    }

    /**
     * {@code GET  /notification-tables/:id} : get the "id" notificationTable.
     *
     * @param id the id of the notificationTable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationTable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationTable> getNotificationTable(@PathVariable("id") Integer id) {
        log.debug("REST request to get NotificationTable : {}", id);
        Optional<NotificationTable> notificationTable = notificationTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationTable);
    }

    /**
     * {@code DELETE  /notification-tables/:id} : delete the "id" notificationTable.
     *
     * @param id the id of the notificationTable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificationTable(@PathVariable("id") Integer id) {
        log.debug("REST request to delete NotificationTable : {}", id);
        notificationTableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
