package com.cbmiddleware.detector;


/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-09
 **/
public class OdpsDetectRequest extends AbstractDetectRequest {

    /**
     * odps accessId
     */
    private String accessId;
    /**
     * odps accessKey
     */
    private String accessKey;
    /**
     * odps url
     */
    private String url;
    /**
     * odps tunnelUrl
     */
    private String tunnelUrl;
    /**
     * odps project
     */
    private String project;


    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTunnelUrl() {
        return tunnelUrl;
    }

    public void setTunnelUrl(String tunnelUrl) {
        this.tunnelUrl = tunnelUrl;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
