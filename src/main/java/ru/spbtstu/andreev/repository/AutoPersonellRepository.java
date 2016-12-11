package ru.spbtstu.andreev.repository;

import ru.spbtstu.andreev.domain.AutoPersonell;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AutoPersonell entity.
 */
@SuppressWarnings("unused")
public interface AutoPersonellRepository extends JpaRepository<AutoPersonell,Long> {

}
