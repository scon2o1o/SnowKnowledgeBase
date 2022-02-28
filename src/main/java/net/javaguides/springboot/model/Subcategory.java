package net.javaguides.springboot.model;

import javax.persistence.*;

@Entity
@Table(name =  "subcategory", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
