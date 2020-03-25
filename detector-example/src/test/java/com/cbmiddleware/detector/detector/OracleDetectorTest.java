package com.cbmiddleware.detector.detector;

import com.alibaba.fastjson.JSON;
import com.cbmiddleware.detector.DetectResponse;
import com.cbmiddleware.detector.Detector;
import com.cbmiddleware.detector.DetectorActionListener;
import com.cbmiddleware.detector.entity.Threshold;
import com.cbmiddleware.detector.exception.DetectorException;
import com.cbmiddleware.detector.oracle.OracleDetectExecuteSqlResponse;
import com.cbmiddleware.detector.oracle.OracleDetectRequest;
import com.cbmiddleware.detector.oracle.OracleDetectResponse;
import com.cbmiddleware.detector.oracle.OracleTableDetector;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-09
 **/
public class OracleDetectorTest {

    private static Detector detector = null;
    private static OracleDetectRequest request = null;

    @Before
    public void init() {
        detector = new OracleTableDetector();
        request = new OracleDetectRequest();
        request.setUsername("sys as sysdba");
        request.setPassword("system");
        request.setProject("");
        request.setAddress("");
        request.setPort("1521");
        request.setSid("orcl");
        request.setTestConnection(false);
    }

    @Test
    public void detectorTest() {
        try {
            OracleDetectResponse response = (OracleDetectResponse) detector.detect(request);


            System.out.println(JSON.toJSONString(response));
        } catch (DetectorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void detectorListenerTest() {
        Threshold threshold = new Threshold();
        threshold.setTableCount(1);
        threshold.setColumnCount(10);
        try {
            detector.detect(request, threshold, new DetectorActionListener<DetectResponse>() {
                @Override
                public void onResponse(DetectResponse detectResponse) {
                    OracleDetectResponse response = (OracleDetectResponse) detectResponse;

                    System.out.println(JSON.toJSONString(response));
                }

                @Override
                public void onFailure(Exception e) {

                }
            });


        } catch (DetectorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sqlTest(){
        String sql = "SELECT\n" +
                "\t* \n" +
                "FROM\n" +
                "\tDATAQULITY.MZ_GUAHAO1 mz \n" +
                "LEFT JOIN DATAQULITY.SYS_USER s on s.ID != mz.BINGRENID";

        try {
           OracleDetectExecuteSqlResponse response = (OracleDetectExecuteSqlResponse) detector.executeSql(request, sql);
            System.out.println(JSON.toJSONString(response));
        } catch (DetectorException e) {
            e.printStackTrace();
        }
    }
}
