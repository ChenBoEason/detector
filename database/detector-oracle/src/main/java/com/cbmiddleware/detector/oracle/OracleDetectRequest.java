package com.cbmiddleware.detector.oracle;

import com.cbmiddleware.detector.AbstractDetectRequest;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-09
 **/
public class OracleDetectRequest extends AbstractDetectRequest {

    /**
     * 用户名
     *
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * ip地址
     */
    private String address;
    /**
     * 端口
     */
    private String port;
    /**
     * 项目名
     */
    private String project;
    /**
     * oracle sid
     */
    private String sid;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
