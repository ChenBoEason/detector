package com.cbmiddleware.detector.detector;

import com.alibaba.fastjson.JSON;
import com.cbmiddleware.detector.entity.Threshold;
import com.cbmiddleware.detector.exception.DetectorException;
import com.cbmiddleware.detector.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-09
 **/
@Slf4j
public class OdpsDetectorTest {

    private static Detector detector = null;

    private static OdpsDetectRequest request = null;

    @Before
    public void init(){
        detector = new OdpsTableDetector();
        OdpsDetectRequest request = new OdpsDetectRequest();
        request.setAccessId("");
        request.setAccessKey("");
        request.setProject("");
        request.setUrl("");
        request.setTunnelUrl("");
    }


    @Test
    public void connectTest(){
        try {
            doConnect(detector, request);
        } catch (DetectorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void detectTest(){
        try {
            detect(detector, request);
        } catch (DetectorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tableSizetest(){
        try {
            detectTableSize(detector, request);
        } catch (DetectorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void detectByListenerTest(){
        try {
            detectByListener(detector, request);
        } catch (DetectorException e) {
            e.printStackTrace();
        }
    }



    private static void detectByListener(Detector detector, OdpsDetectRequest request) throws DetectorException{
        Threshold threshold = new Threshold();

        threshold.setTableCount(10);

        detector.detect(request, threshold, new DetectorActionListener<DetectResponse>() {
            @Override
            public void onResponse(DetectResponse detectResponse) {
                OdpsDetectResponse response = (OdpsDetectResponse) detectResponse;
                log.info("listener response result:{}", JSON.toJSONString(response));
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private static void executeSql(Detector detector, OdpsDetectRequest request) throws DetectorException {
        OdpsDetectExecuteSqlResponse response = (OdpsDetectExecuteSqlResponse)detector.executeSql(request, "select * from ods_mh_yhqdyrmyy_zy_feiyong1_his limit 20;");
        log.info("result :{}",JSON.toJSONString(response));
    }

    private static void detectTableSize(Detector detector, OdpsDetectRequest request) throws DetectorException {
        List<String> tableNames = new ArrayList<>();
        //tableNames.add("ods_mh_yhqdyrmyy_zy_feiyong1_his");
        //tableNames.add("ods_mh_yhqd_mz_chufang2_his");
        //tableNames.add("stg_mh_yhqdermyy_ods_hz_fyfsmxb");
        request.setTableNames(tableNames);
        OdpsDetectTableSizeResponse response = (OdpsDetectTableSizeResponse)detector.detectTableSize(request, tableNames);
        log.info("result :{}",JSON.toJSONString(response));
    }

    private static void detect(Detector detector, OdpsDetectRequest request) throws DetectorException {
        OdpsDetectResponse response = (OdpsDetectResponse) detector.detect(request);
        log.info("result :{}",JSON.toJSONString(response));
    }

    private static void doConnect(Detector detector, OdpsDetectRequest request) throws DetectorException {

        boolean connect = detector.isConnect(request);
        log.info("connect status :{}", connect);
    }
}
