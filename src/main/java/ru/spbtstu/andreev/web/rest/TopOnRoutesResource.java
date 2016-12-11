package ru.spbtstu.andreev.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.spbtstu.andreev.domain.TopOnRoutes;

import ru.spbtstu.andreev.repository.TopOnRoutesRepository;
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
 * REST controller for managing TopOnRoutes.
 */
@RestController
@RequestMapping("/api")
public class TopOnRoutesResource {

    private final Logger log = LoggerFactory.getLogger(TopOnRoutesResource.class);
        
    @Inject
    private TopOnRoutesRepository topOnRoutesRepository;

    /**
     * POST  /top-on-routes : Create a new topOnRoutes.
     *
     * @param topOnRoutes the topOnRoutes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new topOnRoutes, or with status 400 (Bad Request) if the topOnRoutes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/top-on-routes")
    @Timed
    public ResponseEntity<TopOnRoutes> createTopOnRoutes(@RequestBody TopOnRoutes topOnRoutes) throws URISyntaxException {
        log.debug("REST request to save TopOnRoutes : {}", topOnRoutes);
        if (topOnRoutes.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("topOnRoutes", "idexists", "A new topOnRoutes cannot already have an ID")).body(null);
        }
        TopOnRoutes result = topOnRoutesRepository.save(topOnRoutes);
        return ResponseEntity.created(new URI("/api/top-on-routes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("topOnRoutes", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /top-on-routes : Updates an existing topOnRoutes.
     *
     * @param topOnRoutes the topOnRoutes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated topOnRoutes,
     * or with status 400 (Bad Request) if the topOnRoutes is not valid,
     * or with status 500 (Internal Server Error) if the topOnRoutes couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/top-on-routes")
    @Timed
    public ResponseEntity<TopOnRoutes> updateTopOnRoutes(@RequestBody TopOnRoutes topOnRoutes) throws URISyntaxException {
        log.debug("REST request to update TopOnRoutes : {}", topOnRoutes);
        if (topOnRoutes.getId() == null) {
            return createTopOnRoutes(topOnRoutes);
        }
        TopOnRoutes result = topOnRoutesRepository.save(topOnRoutes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("topOnRoutes", topOnRoutes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /top-on-routes : get all the topOnRoutes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of topOnRoutes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/top-on-routes")
    @Timed
    public ResponseEntity<List<TopOnRoutes>> getAllTopOnRoutes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TopOnRoutes");
        Page<TopOnRoutes> page = topOnRoutesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/top-on-routes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /top-on-routes/:id : get the "id" topOnRoutes.
     *
     * @param id the id of the topOnRoutes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the topOnRoutes, or with status 404 (Not Found)
     */
    @GetMapping("/top-on-routes/{id}")
    @Timed
    public ResponseEntity<TopOnRoutes> getTopOnRoutes(@PathVariable Long id) {
        log.debug("REST request to get TopOnRoutes : {}", id);
        TopOnRoutes topOnRoutes = topOnRoutesRepository.findOne(id);
        return Optional.ofNullable(topOnRoutes)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /top-on-routes/:id : delete the "id" topOnRoutes.
     *
     * @param id the id of the topOnRoutes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/top-on-routes/{id}")
    @Timed
    public ResponseEntity<Void> deleteTopOnRoutes(@PathVariable Long id) {
        log.debug("REST request to delete TopOnRoutes : {}", id);
        topOnRoutesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("topOnRoutes", id.toString())).build();
    }

}
