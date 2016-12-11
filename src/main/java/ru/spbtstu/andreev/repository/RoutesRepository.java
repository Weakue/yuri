package ru.spbtstu.andreev.repository;

import ru.spbtstu.andreev.domain.Routes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Routes entity.
 */
@SuppressWarnings("unused")
public interface RoutesRepository extends JpaRepository<Routes,Long> {

}
