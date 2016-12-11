package ru.spbtstu.andreev.repository;

import ru.spbtstu.andreev.domain.Journal;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Journal entity.
 */
@SuppressWarnings("unused")
public interface JournalRepository extends JpaRepository<Journal,Long> {

}
