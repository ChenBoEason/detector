package com.cbmiddleware.detector.mysql;

import com.cbmiddleware.detector.AbstractDetectRequest;


/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-06
 **/
public class MysqlDetectRequest extends AbstractDetectRequest {

    private static final long serialVersionUID = -4340566764260269708L;
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
}
