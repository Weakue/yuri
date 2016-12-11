package ru.spbtstu.andreev.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Auto.
 */
@Entity
@Table(name = "auto")
public class Auto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "num")
    private Integer num;

    @Column(name = "color")
    private String color;

    @Column(name = "mark")
    private String mark;

    @OneToOne
    @JoinColumn(unique = true)
    private AutoPersonell autoPersonell;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public Auto num(Integer num) {
        this.num = num;
        return this;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getColor() {
        return color;
    }

    public Auto color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMark() {
        return mark;
    }

    public Auto mark(String mark) {
        this.mark = mark;
        return this;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public AutoPersonell getAutoPersonell() {
        return autoPersonell;
    }

    public Auto autoPersonell(AutoPersonell autoPersonell) {
        this.autoPersonell = autoPersonell;
        return this;
    }

    public void setAutoPersonell(AutoPersonell autoPersonell) {
        this.autoPersonell = autoPersonell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Auto auto = (Auto) o;
        if(auto.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, auto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Auto{" +
            "id=" + id +
            ", num='" + num + "'" +
            ", color='" + color + "'" +
            ", mark='" + mark + "'" +
            '}';
    }
}
