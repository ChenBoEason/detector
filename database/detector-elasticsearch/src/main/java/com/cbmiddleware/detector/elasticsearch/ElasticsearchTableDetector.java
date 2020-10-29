package com.cbmiddleware.detector.elasticsearch;

import com.cbmiddleware.detector.AbstractTableDetector;
import com.cbmiddleware.detector.DetectRequest;
import com.cbmiddleware.detector.DetectResponse;
import com.cbmiddleware.detector.DetectorActionListener;
import com.cbmiddleware.detector.constant.DataBaseType;
import com.cbmiddleware.detector.constant.DetectorType;
import com.cbmiddleware.detector.elasticsearch.common.utils.ElasticSearchUtils;
import com.cbmiddleware.detector.elasticsearch.common.version.ElasticSearchVersion;
import com.cbmiddleware.detector.entity.ColumnInfo;
import com.cbmiddleware.detector.entity.TableInfo;
import com.cbmiddleware.detector.entity.Threshold;
import com.cbmiddleware.detector.exception.DetectorException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.cbmiddleware.detector.elasticsearch.common.constant.ElasticSearchCoreConstants.*;

/**
 * @author Eason(bo.chenb)
 * @description es 索引结构字段探测器
 * @date 2020-03-13
 **/
public class ElasticsearchTableDetector extends AbstractTableDetector {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchTableDetector.class);

    @Override
    public DetectResponse detect(DetectRequest detectRequest) throws DetectorException {
        ElasticsearchDetectRequest request = (ElasticsearchDetectRequest) detectRequest;

        String address = request.getAddress();
        String scheme = request.getScheme();
        String username = request.getUsername();
        String password = request.getPassword();

        log.info("Begin get elasticsearch index info,address:[{}], ...");
        long startTableInfoTime = System.currentTimeMillis();
        RestHighLevelClient esClient = null;
        try {

            GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
            //  如果传了 tableNames 也就是索引名称 则进行单独查询
            if (request.getTableNames() != null && !request.getTableNames().isEmpty()) {
                //指定索引
                getMappingsRequest.indices(request.getTableNames().toArray(new String[0]));
            }
            esClient = ElasticSearchUtils.getEsClient(address, scheme, username, password);
            //调用获取
            GetMappingsResponse getMappingResponse = esClient.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);
            //处理数据
            Map<String, MappingMetadata> mappings = getMappingResponse.mappings();

            Map<String, TableInfo> tableInfoMap = new HashMap<>();
            mappings.forEach((indexName, indexInfo) -> {
                TableInfo tableInfo = new TableInfo();
                tableInfoMap.put(indexName, tableInfo);
                Map<String, Object> sourceAsMap = indexInfo.getSourceAsMap();
                // 过滤调mapping为空的表
                List<ColumnInfo> columnInfos = new ArrayList<>();
                if (null != sourceAsMap && sourceAsMap.containsKey(PROPERTIES)) {
                    Map<String, Object> properties = (Map<String, Object>) sourceAsMap.get(PROPERTIES);
                    final Integer[] positionIndex = {1};
                    properties.forEach((fieldName, fieldValue) -> {
                        Map<String, Object> value = (Map<String, Object>) fieldValue;
                        ColumnInfo columnInfo = new ColumnInfo();
                        columnInfo.setTableName(indexName);
                        columnInfo.setColumnName(fieldName);
                        columnInfo.setColumnType((String) value.get(TYPE));
                        columnInfo.setDataType((String) value.get(TYPE));
                        columnInfo.setOrdinalPosition(positionIndex[0]++);
                        columnInfos.add(columnInfo);
                    });
                }
                tableInfo.setFieldInfos(columnInfos);
            });
            ElasticsearchDetectResponse response = new ElasticsearchDetectResponse();
            response.setTableInfos(tableInfoMap);
            log.info("End get elasticsearch index info..., It takes time:{}", System.currentTimeMillis() - startTableInfoTime);
            return response;
        } catch (Exception e) {
            log.error("className: ElasticsearchTableDetector, methodName:detect, error:", e);
            throw new DetectorException("探测出现异常", e);
        } finally {
            if (esClient != null) {
                try {
                    esClient.close();
                } catch (IOException e) {

                }
            }
        }
    }

    @Override
    public void detect(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException {
        super.detect(detectRequest, threshold, listener);
    }

    @Override
    public void detectAsync(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException {
        super.detectAsync(detectRequest, threshold, listener);
    }

    @Override
    public boolean isConnect(DetectRequest detectRequest) throws DetectorException {

        ElasticsearchDetectRequest elasticsearchDetectRequest = (ElasticsearchDetectRequest) detectRequest;


        try (RestHighLevelClient esClient = ElasticSearchUtils.getEsClient(
                elasticsearchDetectRequest.getAddress(),
                elasticsearchDetectRequest.getScheme(),
                elasticsearchDetectRequest.getUsername(),
                elasticsearchDetectRequest.getPassword())
        ) {
            String version = elasticsearchDetectRequest.getVersion();
            ElasticSearchVersion searchVersion = ElasticSearchVersion.parse(version);
            String indexName = "detect_test_connect_" + UUID.randomUUID().toString().replace("-", "");
            switch (searchVersion) {
                case VERSION_5:
                case VERSION_6:
                    // todo
                    return false;
                case VERSION_7:
                    //对远程Elasticsearch集群执行ping操作，如果ping成功，则返回true，否则返回false
                    return esClient.ping(RequestOptions.DEFAULT);
                default:
                    throw new RuntimeException("无效的es版本");
            }

        } catch (Exception e) {
            log.error("className: ElasticsearchTableDetector, methodName:isConnect, error:", e);
            throw new RuntimeException("el isConnect error", e);
        }
    }

    @Override
    public DetectResponse detectTableSize(DetectRequest detectRequest, List<String> tableNames) throws DetectorException {
        return super.detectTableSize(detectRequest, tableNames);
    }

    @Override
    public DetectResponse executeSql(DetectRequest detectRequest, String sql) throws DetectorException {
        return super.executeSql(detectRequest, sql);
    }

    @Override
    public DetectorType detectorType() {
        return DetectorType.table;
    }

    @Override
    public DataBaseType databaseType() {
        return DataBaseType.elasticsearch;
    }
}
