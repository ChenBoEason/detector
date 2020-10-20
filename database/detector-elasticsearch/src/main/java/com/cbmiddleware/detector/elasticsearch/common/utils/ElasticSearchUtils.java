package com.cbmiddleware.detector.elasticsearch.common.utils;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 * @email chenboeason@gmail.com
 * @date 2020-10-20
 **/
public class ElasticSearchUtils {

    public static RestHighLevelClient getEsClient(String address, String scheme, String username, String password,
                                                  Integer connectTimeout, Integer socketTimeout, Integer connectRequestTimeout) {

        List<String> addressList = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(address);
        HttpHost[] httpHosts = new HttpHost[addressList.size()];

        String[] servers = addressList.toArray(new String[0]);

        for (int i = 0, len = servers.length; i < len; i++) {
            String[] confs = servers[i].trim().split(":");
            httpHosts[i] = new HttpHost(confs[0], Integer.parseInt(confs[1]), scheme);
        }


        RestClientBuilder restClientBuilder = RestClient.builder(httpHosts).setRequestConfigCallback(builder -> builder
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(connectRequestTimeout));

        /**
         * 密码设置
         */
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));

            restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }

        try {
            return new RestHighLevelClient(restClientBuilder);
        } catch (Exception e) {
            throw new RuntimeException("create elasticsearch client error", e);
        }
    }

    public static RestHighLevelClient getEsClient(String address, String scheme, String username, String password) {
        return getEsClient(address, scheme, username, password, 6000, 6000, 6000);
    }
}
