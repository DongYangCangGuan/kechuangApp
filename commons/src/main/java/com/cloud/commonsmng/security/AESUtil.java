package com.cloud.commonsmng.security;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Map;

@Component
public class AESUtil {

    public static final String MANAGE_KEY = "asdsdfs112323242";

    //实例化日志对象，引用jar包org.slf4j.Logger
    private static final Logger logger = LoggerFactory.getLogger(AESUtil.class);

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";

    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /*
     * AES解密
     * data 待解密数据
     * key 解密秘钥
     * return 解密后的string
     */
    public String desEncryt(String data, String key) {
        try {
            logger.info(String.format("需要解密的长度: [%s],", data.length()));
            byte[] encrypted = new BASE64Decoder().decodeBuffer(data);
            if (encrypted != null && !"".equals(encrypted)) {
                while (encrypted.length % 16 != 0) {
                    data = data + "=";
                    encrypted = new BASE64Decoder().decodeBuffer(data);
                }
                logger.info(String.format("base64解密的值: [%s] , [%s] , [%s]", encrypted, encrypted.length, encrypted.length % 16));

                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                kgen.init(128);

                Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), ALGORITHM));

                return new String(cipher.doFinal(encrypted), "UTF-8");
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
            return null;
        }
    }

    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[
     */
    public String aesEncryptToBytes(String content, String encryptKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);//根据指定算法ALGORITHM自成密码器
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), ALGORITHM));//初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为生成的AES密钥
            return Base64.encodeBase64String(cipher.doFinal(content.getBytes("utf-8")));//将加密后的数据转换为字符串返回
            //return new String(cipher.doFinal(content.getBytes("utf-8")));
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
            return null;
        }
    }


    /**
     * AES加密(map加密)
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return
     */
    public String aesEncryptToBytes(Map<String, Object> map, String encryptKey) {
        String content = JSONObject.toJSONString(map);
        String encrypt = aesEncryptToBytes(content, encryptKey);
        return encrypt;
    }
}
