package ru.spbtstu.andreev.web.rest;

import ru.spbtstu.andreev.YuriApp;

import ru.spbtstu.andreev.domain.AutoPersonell;
import ru.spbtstu.andreev.repository.AutoPersonellRepository;

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
 * Test class for the AutoPersonellResource REST controller.
 *
 * @see AutoPersonellResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YuriApp.class)
public class AutoPersonellResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final String DEFAULT_FATHER_NAME = "AAAAA";
    private static final String UPDATED_FATHER_NAME = "BBBBB";

    @Inject
    private AutoPersonellRepository autoPersonellRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAutoPersonellMockMvc;

    private AutoPersonell autoPersonell;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AutoPersonellResource autoPersonellResource = new AutoPersonellResource();
        ReflectionTestUtils.setField(autoPersonellResource, "autoPersonellRepository", autoPersonellRepository);
        this.restAutoPersonellMockMvc = MockMvcBuilders.standaloneSetup(autoPersonellResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutoPersonell createEntity(EntityManager em) {
        AutoPersonell autoPersonell = new AutoPersonell()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .fatherName(DEFAULT_FATHER_NAME);
        return autoPersonell;
    }

    @Before
    public void initTest() {
        autoPersonell = createEntity(em);
    }

    @Test
    @Transactional
    public void createAutoPersonell() throws Exception {
        int databaseSizeBeforeCreate = autoPersonellRepository.findAll().size();

        // Create the AutoPersonell

        restAutoPersonellMockMvc.perform(post("/api/auto-personells")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(autoPersonell)))
                .andExpect(status().isCreated());

        // Validate the AutoPersonell in the database
        List<AutoPersonell> autoPersonells = autoPersonellRepository.findAll();
        assertThat(autoPersonells).hasSize(databaseSizeBeforeCreate + 1);
        AutoPersonell testAutoPersonell = autoPersonells.get(autoPersonells.size() - 1);
        assertThat(testAutoPersonell.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAutoPersonell.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAutoPersonell.getFatherName()).isEqualTo(DEFAULT_FATHER_NAME);
    }

    @Test
    @Transactional
    public void getAllAutoPersonells() throws Exception {
        // Initialize the database
        autoPersonellRepository.saveAndFlush(autoPersonell);

        // Get all the autoPersonells
        restAutoPersonellMockMvc.perform(get("/api/auto-personells?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(autoPersonell.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAutoPersonell() throws Exception {
        // Initialize the database
        autoPersonellRepository.saveAndFlush(autoPersonell);

        // Get the autoPersonell
        restAutoPersonellMockMvc.perform(get("/api/auto-personells/{id}", autoPersonell.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(autoPersonell.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.fatherName").value(DEFAULT_FATHER_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAutoPersonell() throws Exception {
        // Get the autoPersonell
        restAutoPersonellMockMvc.perform(get("/api/auto-personells/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAutoPersonell() throws Exception {
        // Initialize the database
        autoPersonellRepository.saveAndFlush(autoPersonell);
        int databaseSizeBeforeUpdate = autoPersonellRepository.findAll().size();

        // Update the autoPersonell
        AutoPersonell updatedAutoPersonell = autoPersonellRepository.findOne(autoPersonell.getId());
        updatedAutoPersonell
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .fatherName(UPDATED_FATHER_NAME);

        restAutoPersonellMockMvc.perform(put("/api/auto-personells")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAutoPersonell)))
                .andExpect(status().isOk());

        // Validate the AutoPersonell in the database
        List<AutoPersonell> autoPersonells = autoPersonellRepository.findAll();
        assertThat(autoPersonells).hasSize(databaseSizeBeforeUpdate);
        AutoPersonell testAutoPersonell = autoPersonells.get(autoPersonells.size() - 1);
        assertThat(testAutoPersonell.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAutoPersonell.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAutoPersonell.getFatherName()).isEqualTo(UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    public void deleteAutoPersonell() throws Exception {
        // Initialize the database
        autoPersonellRepository.saveAndFlush(autoPersonell);
        int databaseSizeBeforeDelete = autoPersonellRepository.findAll().size();

        // Get the autoPersonell
        restAutoPersonellMockMvc.perform(delete("/api/auto-personells/{id}", autoPersonell.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AutoPersonell> autoPersonells = autoPersonellRepository.findAll();
        assertThat(autoPersonells).hasSize(databaseSizeBeforeDelete - 1);
    }
}
