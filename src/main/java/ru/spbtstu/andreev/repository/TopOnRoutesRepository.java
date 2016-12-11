package ru.spbtstu.andreev.repository;

import ru.spbtstu.andreev.domain.TopOnRoutes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TopOnRoutes entity.
 */
@SuppressWarnings("unused")
public interface TopOnRoutesRepository extends JpaRepository<TopOnRoutes,Long> {

}
