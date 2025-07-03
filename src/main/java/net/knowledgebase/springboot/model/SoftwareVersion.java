package net.knowledgebase.springboot.model;

import javax.persistence.*;

@Entity
public class SoftwareVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String platform;

    @Column
    private String minVersion;

    @Column
    private String latestVersion;

    @Column
    private String updateUrl;

    public SoftwareVersion() {
    }

    public SoftwareVersion(long id, String platform, String minVersion, String latestVersion, String updateUrl) {
        this.id = id;
        this.platform = platform;
        this.minVersion = minVersion;
        this.latestVersion = latestVersion;
        this.updateUrl = updateUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
}
