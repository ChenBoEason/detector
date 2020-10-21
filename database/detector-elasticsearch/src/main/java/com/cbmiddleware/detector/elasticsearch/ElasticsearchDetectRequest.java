package com.cbmiddleware.detector.elasticsearch;


import com.cbmiddleware.detector.AbstractDetectRequest;
import com.cbmiddleware.detector.elasticsearch.common.version.ElasticSearchVersion;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-13
 **/
public class ElasticsearchDetectRequest extends AbstractDetectRequest {

    /**
     * es indexName
     */
    private String indexName;

    /**
     * type
     */
    private String type;

    /***
     * 版本
     */
    private ElasticSearchVersion version = ElasticSearchVersion.VERSION_7;


    /**
     * es连接地址  多个以','号隔开
     * ex  :  192.168.116:9200,192.168.1.117:9200
     */
    private String address;
    /**
     * 连接协议
     */
    private String scheme = "http";

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 连接超时时间
     */
    private Integer connectTimeout;
    /**
     * socket超时时间
     */
    private Integer socketTimeout;
    /**
     * 连接请求超时时间
     */
    private Integer connectRequestTimeout;


    public String getType() {
        return type;
    }

    public ElasticsearchDetectRequest setType(String type) {
        this.type = type;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public ElasticsearchDetectRequest setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public ElasticsearchDetectRequest setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getScheme() {
        return scheme;
    }

    public ElasticsearchDetectRequest setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ElasticsearchDetectRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ElasticsearchDetectRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public ElasticsearchDetectRequest setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public ElasticsearchDetectRequest setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public Integer getConnectRequestTimeout() {
        return connectRequestTimeout;
    }

    public ElasticsearchDetectRequest setConnectRequestTimeout(Integer connectRequestTimeout) {
        this.connectRequestTimeout = connectRequestTimeout;
        return this;
    }

    @Override
    public String getVersion() {
        return version.name();
    }

    public void setVersion(ElasticSearchVersion version) {
        this.version = version;
    }

}