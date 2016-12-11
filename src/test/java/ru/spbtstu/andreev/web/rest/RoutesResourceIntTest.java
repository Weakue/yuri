package ru.spbtstu.andreev.web.rest;

import ru.spbtstu.andreev.YuriApp;

import ru.spbtstu.andreev.domain.Routes;
import ru.spbtstu.andreev.repository.RoutesRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RoutesResource REST controller.
 *
 * @see RoutesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YuriApp.class)
public class RoutesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private RoutesRepository routesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRoutesMockMvc;

    private Routes routes;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RoutesResource routesResource = new RoutesResource();
        ReflectionTestUtils.setField(routesResource, "routesRepository", routesRepository);
        this.restRoutesMockMvc = MockMvcBuilders.standaloneSetup(routesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Routes createEntity(EntityManager em) {
        Routes routes = new Routes()
                .name(DEFAULT_NAME);
        return routes;
    }

    @Before
    public void initTest() {
        routes = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoutes() throws Exception {
        int databaseSizeBeforeCreate = routesRepository.findAll().size();

        // Create the Routes

        restRoutesMockMvc.perform(post("/api/routes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(routes)))
                .andExpect(status().isCreated());

        // Validate the Routes in the database
        List<Routes> routes = routesRepository.findAll();
        assertThat(routes).hasSize(databaseSizeBeforeCreate + 1);
        Routes testRoutes = routes.get(routes.size() - 1);
        assertThat(testRoutes.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);

        // Get all the routes
        restRoutesMockMvc.perform(get("/api/routes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(routes.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);

        // Get the routes
        restRoutesMockMvc.perform(get("/api/routes/{id}", routes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(routes.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoutes() throws Exception {
        // Get the routes
        restRoutesMockMvc.perform(get("/api/routes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);
        int databaseSizeBeforeUpdate = routesRepository.findAll().size();

        // Update the routes
        Routes updatedRoutes = routesRepository.findOne(routes.getId());
        updatedRoutes
                .name(UPDATED_NAME);

        restRoutesMockMvc.perform(put("/api/routes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRoutes)))
                .andExpect(status().isOk());

        // Validate the Routes in the database
        List<Routes> routes = routesRepository.findAll();
        assertThat(routes).hasSize(databaseSizeBeforeUpdate);
        Routes testRoutes = routes.get(routes.size() - 1);
        assertThat(testRoutes.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteRoutes() throws Exception {
        // Initialize the database
        routesRepository.saveAndFlush(routes);
        int databaseSizeBeforeDelete = routesRepository.findAll().size();

        // Get the routes
        restRoutesMockMvc.perform(delete("/api/routes/{id}", routes.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Routes> routes = routesRepository.findAll();
        assertThat(routes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
