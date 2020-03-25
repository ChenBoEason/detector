package com.cbmiddleware.detector.util;

import com.aliyun.odps.Odps;
import com.aliyun.odps.Partition;
import com.aliyun.odps.Table;
import com.aliyun.odps.account.AliyunAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-09
 **/
public class DetectorOdpsUtils {

    public static Odps createOdps(String accessId, String accessKey, String url, String project){
        Odps odps = new Odps(new AliyunAccount(accessId, accessKey));
        odps.setEndpoint(url);
        odps.setDefaultProject(project);
        return odps;
    }


    public static List<String> getTableAllPartitions(Table table) {
        List<Partition> tableAllPartitions = table.getPartitions();

        List<String> retPartitions = new ArrayList<String>();

        if (null != tableAllPartitions) {
            for (Partition partition : tableAllPartitions) {
                retPartitions.add(partition.getPartitionSpec().toString());
            }
        }

        return retPartitions;
    }
}
