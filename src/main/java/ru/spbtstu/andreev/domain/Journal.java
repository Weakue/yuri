package ru.spbtstu.andreev.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Journal.
 */
@Entity
@Table(name = "journal")
public class Journal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "time_out")
    private LocalDate timeOut;

    @Column(name = "time_in")
    private LocalDate timeIn;

    @ManyToOne
    private Auto auto;

    @ManyToOne
    private Routes routes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTimeOut() {
        return timeOut;
    }

    public Journal timeOut(LocalDate timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public void setTimeOut(LocalDate timeOut) {
        this.timeOut = timeOut;
    }

    public LocalDate getTimeIn() {
        return timeIn;
    }

    public Journal timeIn(LocalDate timeIn) {
        this.timeIn = timeIn;
        return this;
    }

    public void setTimeIn(LocalDate timeIn) {
        this.timeIn = timeIn;
    }

    public Auto getAuto() {
        return auto;
    }

    public Journal auto(Auto auto) {
        this.auto = auto;
        return this;
    }

    public void setAuto(Auto auto) {
        this.auto = auto;
    }

    public Routes getRoutes() {
        return routes;
    }

    public Journal routes(Routes routes) {
        this.routes = routes;
        return this;
    }

    public void setRoutes(Routes routes) {
        this.routes = routes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Journal journal = (Journal) o;
        if(journal.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, journal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Journal{" +
            "id=" + id +
            ", timeOut='" + timeOut + "'" +
            ", timeIn='" + timeIn + "'" +
            '}';
    }
}
