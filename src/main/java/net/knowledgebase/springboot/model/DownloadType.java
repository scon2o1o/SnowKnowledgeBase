package net.knowledgebase.springboot.model;

import javax.persistence.*;

@Entity
@Table(name="download_type", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class DownloadType {
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
