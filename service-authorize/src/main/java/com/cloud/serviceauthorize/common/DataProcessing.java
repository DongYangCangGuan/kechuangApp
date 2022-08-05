package com.cloud.serviceauthorize.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 数据处理
 * author: tjs
 * date: 2021-08-13
 */
public class DataProcessing {

    /**
     * 将路径中的数据进行转义处理
     *
     * @param params
     * @return
     */
    public static String urlDecode(String params) {
        try {
            String newParams = URLDecoder.decode(params, "UTF-8");
            if (newParams.endsWith("=")) {
                newParams = newParams.substring(0, params.length() - 1);
            }
            if (newParams.contains(" ")) {
                newParams = newParams.replace(" ", "+");
            }
            return newParams;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
