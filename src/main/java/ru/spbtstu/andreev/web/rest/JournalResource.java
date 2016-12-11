package ru.spbtstu.andreev.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.spbtstu.andreev.domain.Journal;

import ru.spbtstu.andreev.repository.JournalRepository;
import ru.spbtstu.andreev.web.rest.util.HeaderUtil;
import ru.spbtstu.andreev.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Journal.
 */
@RestController
@RequestMapping("/api")
public class JournalResource {

    private final Logger log = LoggerFactory.getLogger(JournalResource.class);
        
    @Inject
    private JournalRepository journalRepository;

    /**
     * POST  /journals : Create a new journal.
     *
     * @param journal the journal to create
     * @return the ResponseEntity with status 201 (Created) and with body the new journal, or with status 400 (Bad Request) if the journal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/journals")
    @Timed
    public ResponseEntity<Journal> createJournal(@RequestBody Journal journal) throws URISyntaxException {
        log.debug("REST request to save Journal : {}", journal);
        if (journal.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("journal", "idexists", "A new journal cannot already have an ID")).body(null);
        }
        Journal result = journalRepository.save(journal);
        return ResponseEntity.created(new URI("/api/journals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("journal", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /journals : Updates an existing journal.
     *
     * @param journal the journal to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated journal,
     * or with status 400 (Bad Request) if the journal is not valid,
     * or with status 500 (Internal Server Error) if the journal couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/journals")
    @Timed
    public ResponseEntity<Journal> updateJournal(@RequestBody Journal journal) throws URISyntaxException {
        log.debug("REST request to update Journal : {}", journal);
        if (journal.getId() == null) {
            return createJournal(journal);
        }
        Journal result = journalRepository.save(journal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("journal", journal.getId().toString()))
            .body(result);
    }

    /**
     * GET  /journals : get all the journals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of journals in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/journals")
    @Timed
    public ResponseEntity<List<Journal>> getAllJournals(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Journals");
        Page<Journal> page = journalRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/journals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /journals/:id : get the "id" journal.
     *
     * @param id the id of the journal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the journal, or with status 404 (Not Found)
     */
    @GetMapping("/journals/{id}")
    @Timed
    public ResponseEntity<Journal> getJournal(@PathVariable Long id) {
        log.debug("REST request to get Journal : {}", id);
        Journal journal = journalRepository.findOne(id);
        return Optional.ofNullable(journal)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /journals/:id : delete the "id" journal.
     *
     * @param id the id of the journal to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/journals/{id}")
    @Timed
    public ResponseEntity<Void> deleteJournal(@PathVariable Long id) {
        log.debug("REST request to delete Journal : {}", id);
        journalRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("journal", id.toString())).build();
    }

}
