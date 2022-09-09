package net.knowledgebase.springboot.model;

import javax.persistence.*;

@Entity
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String url;

    @Column
    private boolean email;

    @Column
    private String title;

    @Column
    private String sfuser;

    @Column
    private String sfpass;

    public Settings() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSfuser() {
        return sfuser;
    }

    public void setSfuser(String sfuser) {
        this.sfuser = sfuser;
    }

    public String getSfpass() {
        return sfpass;
    }

    public void setSfpass(String sfpass) {
        this.sfpass = sfpass;
    }
}
