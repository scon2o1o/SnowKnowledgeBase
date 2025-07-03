package net.knowledgebase.springboot.model;

public class SoftwareVersionDto {
    private String platform;
    private String minVersion;
    private String latestVersion;
    private String updateUrl;

    public SoftwareVersionDto(String platform, String minVersion, String latestVersion, String updateUrl) {
        this.platform = platform;
        this.minVersion = minVersion;
        this.latestVersion = latestVersion;
        this.updateUrl = updateUrl;
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
