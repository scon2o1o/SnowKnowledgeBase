package net.knowledgebase.springboot.web.dto;

import java.util.Date;

public class DownloadDto {
    private long id;
    private String name;
    private String type;
    private Date dateAdded;
    private long size;

    public DownloadDto(long id, String name, String type, Date dateAdded, long size) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.dateAdded = dateAdded;
        this.size = size;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
