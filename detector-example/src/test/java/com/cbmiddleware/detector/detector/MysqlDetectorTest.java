package com.cbmiddleware.detector.detector;

import com.alibaba.fastjson.JSON;
import com.cbmiddleware.detector.Detector;
import com.cbmiddleware.detector.exception.DetectorException;
import com.cbmiddleware.detector.mysql.MysqlDetectExecuteSqlResponse;
import com.cbmiddleware.detector.mysql.MysqlDetectRequest;
import com.cbmiddleware.detector.mysql.MysqlDetectResponse;
import com.cbmiddleware.detector.mysql.MysqlTableDetector;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-06
 **/
@Slf4j
public class MysqlDetectorTest {

    private static Detector detector = new MysqlTableDetector();
    private static MysqlDetectRequest request = new MysqlDetectRequest();

    @Before
    public void init(){
        request.setUsername("用户名");
        request.setPassword("密码");
        request.setProject("项目名");
        request.setAddress("连接地址");
        request.setPort("3306");
    }

    @Test
    public void connectTest(){
        boolean connect = false;
        try {
            connect = detector.isConnect(request);
        } catch (DetectorException e) {
            e.printStackTrace();
        }

        System.out.println(connect);
    }

    @Test
    public void detect(){
        List<String> tableNames = new ArrayList<>();
        tableNames.add("ads_appointment_info");
        tableNames.add("rule_template");
        tableNames.add("ads_rt_pay_info");
        tableNames.add("standard_union_copy1");
        /* 设置后将会探测指定表名信息 */
        //request.setTableNames(tableNames);

        for (int i = 0; i < 1; i++) {
            new Thread(() -> {
                try {


                    MysqlDetectResponse response = (MysqlDetectResponse) detector.detect(request);

                    System.out.println(JSON.toJSONString(response));
                    System.out.println(response.getTableInfos().keySet());;
                } catch (DetectorException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Test
    public void sqlTest(){
        String sql = "SELECT * FROM validation_dic_group g\n" +
                "INNER JOIN validation_dic_data d on d.dic_key = g.dic_grp_key\n" +
                "where g.dic_grp_key = 'CVX_Right' limit 0,10";

        try {
            MysqlDetectExecuteSqlResponse response = (MysqlDetectExecuteSqlResponse)detector.executeSql(request, sql);
            System.out.println();
        } catch (DetectorException e) {
            e.printStackTrace();
        }
    }

}
