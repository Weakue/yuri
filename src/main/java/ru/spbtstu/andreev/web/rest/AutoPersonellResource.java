package ru.spbtstu.andreev.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.spbtstu.andreev.domain.AutoPersonell;

import ru.spbtstu.andreev.repository.AutoPersonellRepository;
import ru.spbtstu.andreev.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing AutoPersonell.
 */
@RestController
@RequestMapping("/api")
public class AutoPersonellResource {

    private final Logger log = LoggerFactory.getLogger(AutoPersonellResource.class);
        
    @Inject
    private AutoPersonellRepository autoPersonellRepository;

    /**
     * POST  /auto-personells : Create a new autoPersonell.
     *
     * @param autoPersonell the autoPersonell to create
     * @return the ResponseEntity with status 201 (Created) and with body the new autoPersonell, or with status 400 (Bad Request) if the autoPersonell has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/auto-personells")
    @Timed
    public ResponseEntity<AutoPersonell> createAutoPersonell(@RequestBody AutoPersonell autoPersonell) throws URISyntaxException {
        log.debug("REST request to save AutoPersonell : {}", autoPersonell);
        if (autoPersonell.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("autoPersonell", "idexists", "A new autoPersonell cannot already have an ID")).body(null);
        }
        AutoPersonell result = autoPersonellRepository.save(autoPersonell);
        return ResponseEntity.created(new URI("/api/auto-personells/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("autoPersonell", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /auto-personells : Updates an existing autoPersonell.
     *
     * @param autoPersonell the autoPersonell to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated autoPersonell,
     * or with status 400 (Bad Request) if the autoPersonell is not valid,
     * or with status 500 (Internal Server Error) if the autoPersonell couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/auto-personells")
    @Timed
    public ResponseEntity<AutoPersonell> updateAutoPersonell(@RequestBody AutoPersonell autoPersonell) throws URISyntaxException {
        log.debug("REST request to update AutoPersonell : {}", autoPersonell);
        if (autoPersonell.getId() == null) {
            return createAutoPersonell(autoPersonell);
        }
        AutoPersonell result = autoPersonellRepository.save(autoPersonell);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("autoPersonell", autoPersonell.getId().toString()))
            .body(result);
    }

    /**
     * GET  /auto-personells : get all the autoPersonells.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of autoPersonells in body
     */
    @GetMapping("/auto-personells")
    @Timed
    public List<AutoPersonell> getAllAutoPersonells() {
        log.debug("REST request to get all AutoPersonells");
        List<AutoPersonell> autoPersonells = autoPersonellRepository.findAll();
        return autoPersonells;
    }

    /**
     * GET  /auto-personells/:id : get the "id" autoPersonell.
     *
     * @param id the id of the autoPersonell to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the autoPersonell, or with status 404 (Not Found)
     */
    @GetMapping("/auto-personells/{id}")
    @Timed
    public ResponseEntity<AutoPersonell> getAutoPersonell(@PathVariable Long id) {
        log.debug("REST request to get AutoPersonell : {}", id);
        AutoPersonell autoPersonell = autoPersonellRepository.findOne(id);
        return Optional.ofNullable(autoPersonell)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /auto-personells/:id : delete the "id" autoPersonell.
     *
     * @param id the id of the autoPersonell to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/auto-personells/{id}")
    @Timed
    public ResponseEntity<Void> deleteAutoPersonell(@PathVariable Long id) {
        log.debug("REST request to delete AutoPersonell : {}", id);
        autoPersonellRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("autoPersonell", id.toString())).build();
    }

}
