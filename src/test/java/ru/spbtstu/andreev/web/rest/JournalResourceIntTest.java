package ru.spbtstu.andreev.web.rest;

import ru.spbtstu.andreev.YuriApp;

import ru.spbtstu.andreev.domain.Journal;
import ru.spbtstu.andreev.repository.JournalRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JournalResource REST controller.
 *
 * @see JournalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YuriApp.class)
public class JournalResourceIntTest {

    private static final LocalDate DEFAULT_TIME_OUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME_OUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TIME_IN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME_IN = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private JournalRepository journalRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restJournalMockMvc;

    private Journal journal;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JournalResource journalResource = new JournalResource();
        ReflectionTestUtils.setField(journalResource, "journalRepository", journalRepository);
        this.restJournalMockMvc = MockMvcBuilders.standaloneSetup(journalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Journal createEntity(EntityManager em) {
        Journal journal = new Journal()
                .timeOut(DEFAULT_TIME_OUT)
                .timeIn(DEFAULT_TIME_IN);
        return journal;
    }

    @Before
    public void initTest() {
        journal = createEntity(em);
    }

    @Test
    @Transactional
    public void createJournal() throws Exception {
        int databaseSizeBeforeCreate = journalRepository.findAll().size();

        // Create the Journal

        restJournalMockMvc.perform(post("/api/journals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(journal)))
                .andExpect(status().isCreated());

        // Validate the Journal in the database
        List<Journal> journals = journalRepository.findAll();
        assertThat(journals).hasSize(databaseSizeBeforeCreate + 1);
        Journal testJournal = journals.get(journals.size() - 1);
        assertThat(testJournal.getTimeOut()).isEqualTo(DEFAULT_TIME_OUT);
        assertThat(testJournal.getTimeIn()).isEqualTo(DEFAULT_TIME_IN);
    }

    @Test
    @Transactional
    public void getAllJournals() throws Exception {
        // Initialize the database
        journalRepository.saveAndFlush(journal);

        // Get all the journals
        restJournalMockMvc.perform(get("/api/journals?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(journal.getId().intValue())))
                .andExpect(jsonPath("$.[*].timeOut").value(hasItem(DEFAULT_TIME_OUT.toString())))
                .andExpect(jsonPath("$.[*].timeIn").value(hasItem(DEFAULT_TIME_IN.toString())));
    }

    @Test
    @Transactional
    public void getJournal() throws Exception {
        // Initialize the database
        journalRepository.saveAndFlush(journal);

        // Get the journal
        restJournalMockMvc.perform(get("/api/journals/{id}", journal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(journal.getId().intValue()))
            .andExpect(jsonPath("$.timeOut").value(DEFAULT_TIME_OUT.toString()))
            .andExpect(jsonPath("$.timeIn").value(DEFAULT_TIME_IN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJournal() throws Exception {
        // Get the journal
        restJournalMockMvc.perform(get("/api/journals/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJournal() throws Exception {
        // Initialize the database
        journalRepository.saveAndFlush(journal);
        int databaseSizeBeforeUpdate = journalRepository.findAll().size();

        // Update the journal
        Journal updatedJournal = journalRepository.findOne(journal.getId());
        updatedJournal
                .timeOut(UPDATED_TIME_OUT)
                .timeIn(UPDATED_TIME_IN);

        restJournalMockMvc.perform(put("/api/journals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedJournal)))
                .andExpect(status().isOk());

        // Validate the Journal in the database
        List<Journal> journals = journalRepository.findAll();
        assertThat(journals).hasSize(databaseSizeBeforeUpdate);
        Journal testJournal = journals.get(journals.size() - 1);
        assertThat(testJournal.getTimeOut()).isEqualTo(UPDATED_TIME_OUT);
        assertThat(testJournal.getTimeIn()).isEqualTo(UPDATED_TIME_IN);
    }

    @Test
    @Transactional
    public void deleteJournal() throws Exception {
        // Initialize the database
        journalRepository.saveAndFlush(journal);
        int databaseSizeBeforeDelete = journalRepository.findAll().size();

        // Get the journal
        restJournalMockMvc.perform(delete("/api/journals/{id}", journal.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Journal> journals = journalRepository.findAll();
        assertThat(journals).hasSize(databaseSizeBeforeDelete - 1);
    }
}
