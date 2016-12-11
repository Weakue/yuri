package ru.spbtstu.andreev.web.rest;

import ru.spbtstu.andreev.YuriApp;

import ru.spbtstu.andreev.domain.Auto;
import ru.spbtstu.andreev.repository.AutoRepository;

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
 * Test class for the AutoResource REST controller.
 *
 * @see AutoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YuriApp.class)
public class AutoResourceIntTest {

    private static final Integer DEFAULT_NUM = 1;
    private static final Integer UPDATED_NUM = 2;

    private static final String DEFAULT_COLOR = "AAAAA";
    private static final String UPDATED_COLOR = "BBBBB";

    private static final String DEFAULT_MARK = "AAAAA";
    private static final String UPDATED_MARK = "BBBBB";

    @Inject
    private AutoRepository autoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAutoMockMvc;

    private Auto auto;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AutoResource autoResource = new AutoResource();
        ReflectionTestUtils.setField(autoResource, "autoRepository", autoRepository);
        this.restAutoMockMvc = MockMvcBuilders.standaloneSetup(autoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auto createEntity(EntityManager em) {
        Auto auto = new Auto()
                .num(DEFAULT_NUM)
                .color(DEFAULT_COLOR)
                .mark(DEFAULT_MARK);
        return auto;
    }

    @Before
    public void initTest() {
        auto = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuto() throws Exception {
        int databaseSizeBeforeCreate = autoRepository.findAll().size();

        // Create the Auto

        restAutoMockMvc.perform(post("/api/autos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auto)))
                .andExpect(status().isCreated());

        // Validate the Auto in the database
        List<Auto> autos = autoRepository.findAll();
        assertThat(autos).hasSize(databaseSizeBeforeCreate + 1);
        Auto testAuto = autos.get(autos.size() - 1);
        assertThat(testAuto.getNum()).isEqualTo(DEFAULT_NUM);
        assertThat(testAuto.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testAuto.getMark()).isEqualTo(DEFAULT_MARK);
    }

    @Test
    @Transactional
    public void getAllAutos() throws Exception {
        // Initialize the database
        autoRepository.saveAndFlush(auto);

        // Get all the autos
        restAutoMockMvc.perform(get("/api/autos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(auto.getId().intValue())))
                .andExpect(jsonPath("$.[*].num").value(hasItem(DEFAULT_NUM)))
                .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
                .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK.toString())));
    }

    @Test
    @Transactional
    public void getAuto() throws Exception {
        // Initialize the database
        autoRepository.saveAndFlush(auto);

        // Get the auto
        restAutoMockMvc.perform(get("/api/autos/{id}", auto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(auto.getId().intValue()))
            .andExpect(jsonPath("$.num").value(DEFAULT_NUM))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()))
            .andExpect(jsonPath("$.mark").value(DEFAULT_MARK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuto() throws Exception {
        // Get the auto
        restAutoMockMvc.perform(get("/api/autos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuto() throws Exception {
        // Initialize the database
        autoRepository.saveAndFlush(auto);
        int databaseSizeBeforeUpdate = autoRepository.findAll().size();

        // Update the auto
        Auto updatedAuto = autoRepository.findOne(auto.getId());
        updatedAuto
                .num(UPDATED_NUM)
                .color(UPDATED_COLOR)
                .mark(UPDATED_MARK);

        restAutoMockMvc.perform(put("/api/autos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAuto)))
                .andExpect(status().isOk());

        // Validate the Auto in the database
        List<Auto> autos = autoRepository.findAll();
        assertThat(autos).hasSize(databaseSizeBeforeUpdate);
        Auto testAuto = autos.get(autos.size() - 1);
        assertThat(testAuto.getNum()).isEqualTo(UPDATED_NUM);
        assertThat(testAuto.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testAuto.getMark()).isEqualTo(UPDATED_MARK);
    }

    @Test
    @Transactional
    public void deleteAuto() throws Exception {
        // Initialize the database
        autoRepository.saveAndFlush(auto);
        int databaseSizeBeforeDelete = autoRepository.findAll().size();

        // Get the auto
        restAutoMockMvc.perform(delete("/api/autos/{id}", auto.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Auto> autos = autoRepository.findAll();
        assertThat(autos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
