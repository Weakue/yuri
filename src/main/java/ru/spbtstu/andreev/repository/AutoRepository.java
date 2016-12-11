package ru.spbtstu.andreev.repository;

import ru.spbtstu.andreev.domain.Auto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Auto entity.
 */
@SuppressWarnings("unused")
public interface AutoRepository extends JpaRepository<Auto,Long> {

}
