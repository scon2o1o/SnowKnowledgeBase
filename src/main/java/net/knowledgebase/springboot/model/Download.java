package net.knowledgebase.springboot.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Download {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String type;

    @Column(name = "date_added")
    private Date dateAdded;

    @Lob
    @Column(columnDefinition="LONGBLOB")
    private byte[] content;

    @Column
    private long size;

    @Column
    private String category;

    public Download() {
    }

    public Download(String name, String type, byte[] content, long size) {
        this.name = name;
        this.type = type;
        this.content = content;
        this.size = size;
        this.dateAdded = new Date();
    }

    public Download(String name, String type, byte[] content, long size, String category) {
        this.name = name;
        this.type = type;
        this.content = content;
        this.size = size;
        this.dateAdded = new Date();
        this.category = category;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
