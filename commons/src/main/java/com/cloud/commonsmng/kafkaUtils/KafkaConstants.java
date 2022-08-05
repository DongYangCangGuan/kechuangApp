package com.cloud.commonsmng.kafkaUtils;

/**
 * kafka配置参数
 */
public class KafkaConstants {
    // kafka状态标志
    public static final String STATE_KEY = "STATE";
    // kafka状态：开启
    public static final String STATE_ON_VALUE = "on";
    // kafka状态：关闭
    public static final String STATE_OFF_VALUE = "off";
    // kafka开启控制,默认为false
    public static boolean IS_KAFKAACCESS = true;

}
