package ru.spbtstu.andreev.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Routes.
 */
@Entity
@Table(name = "routes")
public class Routes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "routes", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Journal> journals = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Routes name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Journal> getJournals() {
        return journals;
    }

    public Routes journals(Set<Journal> journals) {
        this.journals = journals;
        return this;
    }

    public Routes addJournal(Journal journal) {
        journals.add(journal);
        journal.setRoutes(this);
        return this;
    }

    public Routes removeJournal(Journal journal) {
        journals.remove(journal);
        journal.setRoutes(null);
        return this;
    }

    public void setJournals(Set<Journal> journals) {
        this.journals = journals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Routes routes = (Routes) o;
        if(routes.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, routes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Routes{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
