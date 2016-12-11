package ru.spbtstu.andreev.web.rest;

import ru.spbtstu.andreev.YuriApp;

import ru.spbtstu.andreev.domain.TopOnRoutes;
import ru.spbtstu.andreev.repository.TopOnRoutesRepository;

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
 * Test class for the TopOnRoutesResource REST controller.
 *
 * @see TopOnRoutesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YuriApp.class)
public class TopOnRoutesResourceIntTest {

    private static final String DEFAULT_ROUTE_NAME = "AAAAA";
    private static final String UPDATED_ROUTE_NAME = "BBBBB";

    private static final Integer DEFAULT_COUNT_ON_ROUTE = 1;
    private static final Integer UPDATED_COUNT_ON_ROUTE = 2;

    @Inject
    private TopOnRoutesRepository topOnRoutesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTopOnRoutesMockMvc;

    private TopOnRoutes topOnRoutes;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TopOnRoutesResource topOnRoutesResource = new TopOnRoutesResource();
        ReflectionTestUtils.setField(topOnRoutesResource, "topOnRoutesRepository", topOnRoutesRepository);
        this.restTopOnRoutesMockMvc = MockMvcBuilders.standaloneSetup(topOnRoutesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopOnRoutes createEntity(EntityManager em) {
        TopOnRoutes topOnRoutes = new TopOnRoutes()
                .routeName(DEFAULT_ROUTE_NAME)
                .countOnRoute(DEFAULT_COUNT_ON_ROUTE);
        return topOnRoutes;
    }

    @Before
    public void initTest() {
        topOnRoutes = createEntity(em);
    }

    @Test
    @Transactional
    public void createTopOnRoutes() throws Exception {
        int databaseSizeBeforeCreate = topOnRoutesRepository.findAll().size();

        // Create the TopOnRoutes

        restTopOnRoutesMockMvc.perform(post("/api/top-on-routes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(topOnRoutes)))
                .andExpect(status().isCreated());

        // Validate the TopOnRoutes in the database
        List<TopOnRoutes> topOnRoutes = topOnRoutesRepository.findAll();
        assertThat(topOnRoutes).hasSize(databaseSizeBeforeCreate + 1);
        TopOnRoutes testTopOnRoutes = topOnRoutes.get(topOnRoutes.size() - 1);
        assertThat(testTopOnRoutes.getRouteName()).isEqualTo(DEFAULT_ROUTE_NAME);
        assertThat(testTopOnRoutes.getCountOnRoute()).isEqualTo(DEFAULT_COUNT_ON_ROUTE);
    }

    @Test
    @Transactional
    public void getAllTopOnRoutes() throws Exception {
        // Initialize the database
        topOnRoutesRepository.saveAndFlush(topOnRoutes);

        // Get all the topOnRoutes
        restTopOnRoutesMockMvc.perform(get("/api/top-on-routes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(topOnRoutes.getId().intValue())))
                .andExpect(jsonPath("$.[*].routeName").value(hasItem(DEFAULT_ROUTE_NAME.toString())))
                .andExpect(jsonPath("$.[*].countOnRoute").value(hasItem(DEFAULT_COUNT_ON_ROUTE)));
    }

    @Test
    @Transactional
    public void getTopOnRoutes() throws Exception {
        // Initialize the database
        topOnRoutesRepository.saveAndFlush(topOnRoutes);

        // Get the topOnRoutes
        restTopOnRoutesMockMvc.perform(get("/api/top-on-routes/{id}", topOnRoutes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(topOnRoutes.getId().intValue()))
            .andExpect(jsonPath("$.routeName").value(DEFAULT_ROUTE_NAME.toString()))
            .andExpect(jsonPath("$.countOnRoute").value(DEFAULT_COUNT_ON_ROUTE));
    }

    @Test
    @Transactional
    public void getNonExistingTopOnRoutes() throws Exception {
        // Get the topOnRoutes
        restTopOnRoutesMockMvc.perform(get("/api/top-on-routes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTopOnRoutes() throws Exception {
        // Initialize the database
        topOnRoutesRepository.saveAndFlush(topOnRoutes);
        int databaseSizeBeforeUpdate = topOnRoutesRepository.findAll().size();

        // Update the topOnRoutes
        TopOnRoutes updatedTopOnRoutes = topOnRoutesRepository.findOne(topOnRoutes.getId());
        updatedTopOnRoutes
                .routeName(UPDATED_ROUTE_NAME)
                .countOnRoute(UPDATED_COUNT_ON_ROUTE);

        restTopOnRoutesMockMvc.perform(put("/api/top-on-routes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTopOnRoutes)))
                .andExpect(status().isOk());

        // Validate the TopOnRoutes in the database
        List<TopOnRoutes> topOnRoutes = topOnRoutesRepository.findAll();
        assertThat(topOnRoutes).hasSize(databaseSizeBeforeUpdate);
        TopOnRoutes testTopOnRoutes = topOnRoutes.get(topOnRoutes.size() - 1);
        assertThat(testTopOnRoutes.getRouteName()).isEqualTo(UPDATED_ROUTE_NAME);
        assertThat(testTopOnRoutes.getCountOnRoute()).isEqualTo(UPDATED_COUNT_ON_ROUTE);
    }

    @Test
    @Transactional
    public void deleteTopOnRoutes() throws Exception {
        // Initialize the database
        topOnRoutesRepository.saveAndFlush(topOnRoutes);
        int databaseSizeBeforeDelete = topOnRoutesRepository.findAll().size();

        // Get the topOnRoutes
        restTopOnRoutesMockMvc.perform(delete("/api/top-on-routes/{id}", topOnRoutes.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TopOnRoutes> topOnRoutes = topOnRoutesRepository.findAll();
        assertThat(topOnRoutes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
