package com.cbmiddleware.detector.util;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @Author: peng.zhup
 * @DateTime: 2019-10-11 11:36
 * @Description: 雪花算法
 * @Readme:
 */
public class DetectorSnowFlakeUtils {

    private DetectorSnowFlakeUtils() {
    }

    private static DetectorSnowFlake bqSnowFlake;

    private static final int DEFAULT_LENGTH = 32;

    /**
     * 32位 20191011115117380001473842655232
     */
    public static String generate() {

        return generate(32);
    }

    /**
     * 通过雪花算法生成指定长度id
     *  String nextId = bqSnowFlake.nextId(true); //32位带时间格式的字符串
     *   length <= 32
     *      nextId.substring(DEFAULT_LENGTH - length, DEFAULT_LENGTH)
     *   length > 32
     *      nextId + (length-32)/18 个 bqSnowFlake.nextId(false) +  bqSnowFlake.nextId(true).substring(DEFAULT_LENGTH - ((length-32)%18), DEFAULT_LENGTH)
     * @param length 长度
     * @return
     */
    public static String generate(int length) {
        if (null == bqSnowFlake) {
            synchronized (DetectorSnowFlakeUtils.class) {
                if (null == bqSnowFlake) {
                    bqSnowFlake = genBQSnowFlake();
                }
            }
        }
        String nextId = bqSnowFlake.nextId(true);
        // 小于32时
        if (length <= DEFAULT_LENGTH) {
            return nextId.substring(DEFAULT_LENGTH - length, DEFAULT_LENGTH);
        }

        // 大于32时
        StringBuilder randomId = new StringBuilder(nextId);
        length -= DEFAULT_LENGTH;

        while (length >= 18) {
            // 18位
            String shortId = bqSnowFlake.nextId(false);
            randomId.append(shortId);
            length -= 18;
        }

        if (length > 0) {
            randomId.append(bqSnowFlake.nextId(true).substring(DEFAULT_LENGTH - length, DEFAULT_LENGTH));
        }
        return randomId.toString();
    }

    public static DetectorSnowFlake getBqSnowFlake() {
        return bqSnowFlake;
    }

    public static void setSnowFlake(DetectorSnowFlake bqSnowFlake) {
        DetectorSnowFlakeUtils.bqSnowFlake = bqSnowFlake;
    }

    private static DetectorSnowFlake genBQSnowFlake() {
        String replace = getLocalIpAddr();
        if (null == replace) {
            replace = "127.0.0.1";
        }

        int hashCode = replace.hashCode() < 0 ? ~replace.hashCode() + 1 : replace.hashCode();
        hashCode = hashCode % 31;

        return new DetectorSnowFlake(hashCode, hashCode + 1);
    }

    /**
     * 获取主机 IP ,去除存在虚拟机情况
     */
    public static String getLocalIpAddr() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        // 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
