package ru.spbtstu.andreev.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TopOnRoutes.
 */
@Entity
@Table(name = "top_on_routes")
public class TopOnRoutes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "route_name")
    private String routeName;

    @Column(name = "count_on_route")
    private Integer countOnRoute;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public TopOnRoutes routeName(String routeName) {
        this.routeName = routeName;
        return this;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Integer getCountOnRoute() {
        return countOnRoute;
    }

    public TopOnRoutes countOnRoute(Integer countOnRoute) {
        this.countOnRoute = countOnRoute;
        return this;
    }

    public void setCountOnRoute(Integer countOnRoute) {
        this.countOnRoute = countOnRoute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TopOnRoutes topOnRoutes = (TopOnRoutes) o;
        if(topOnRoutes.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, topOnRoutes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TopOnRoutes{" +
            "id=" + id +
            ", routeName='" + routeName + "'" +
            ", countOnRoute='" + countOnRoute + "'" +
            '}';
    }
}
