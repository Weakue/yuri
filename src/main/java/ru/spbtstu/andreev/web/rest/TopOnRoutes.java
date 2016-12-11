package ru.spbtstu.andreev.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.spbtstu.andreev.domain.Journal;
import ru.spbtstu.andreev.domain.Routes;
import ru.spbtstu.andreev.repository.JournalRepository;
import ru.spbtstu.andreev.repository.RoutesRepository;
import ru.spbtstu.andreev.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexey Puks on 11.12.16.
 */
@RestController
@RequestMapping("/api")
public class TopOnRoutes {

    @Inject
    private RoutesRepository routesRepository;

    @Inject
    private JournalRepository journalRepository;



    private final Logger log = LoggerFactory.getLogger(TopOnRoutes.class);

    @GetMapping("/top-on-routes")
    @Timed
    public ResponseEntity<List<TopOnRoutesPojo>> getAll(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Routes");
        List<Routes> routes = routesRepository.findAll();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(new PageImpl(routes), "/api/top-on-routes");
        HashMap<Long, Integer> topAutoOnRoute = new HashMap<>();
        for (Routes route : routes) {
            for (Journal journal : route.getJournals()) {
                if (journal.getTimeIn() == null) {
                    Integer c = topAutoOnRoute.get(route.getId());
                    if (c == null) {
                        topAutoOnRoute.put(route.getId(), 1);
                    } else {
                        topAutoOnRoute.put(route.getId(), c + 1);
                    }
                }
            }
        }

        List<TopOnRoutesPojo> result = new ArrayList<>();



        for (Map.Entry<Long, Integer> entry : topAutoOnRoute.entrySet()) {
            Long routeId = entry.getKey();
            Integer countOnRoute = entry.getValue();
            result.add(new TopOnRoutesPojo(routesRepository.findOne(routeId).getName()
               ,countOnRoute, LocalDate.now()));
        }


        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    private class TopOnRoutesPojo {
        String routeName;
        LocalDate timeIn;
        Integer countOnRoute;

        public TopOnRoutesPojo(String routeName, Integer count, LocalDate timeIn) {
            this.routeName = routeName;
            this.countOnRoute = count;
            this.timeIn = timeIn;
        }

        public String getRouteName() {
            return routeName;
        }

        public void setRouteName(String routeName) {
            this.routeName = routeName;
        }

        public Integer getCountOnRoute() {
            return countOnRoute;
        }

        public void setCountOnRoute(Integer count) {
            this.countOnRoute = count;
        }
    }
}
