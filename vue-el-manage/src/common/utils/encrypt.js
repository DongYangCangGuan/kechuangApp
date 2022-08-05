import CryptoJS from 'crypto-js'
import JsEncrypt from 'jsencrypt'

const jsEncrypt = new JsEncrypt()
import {getSecret} from "@COM/utils/auth";


/**
 * AES加密
 * 参数加密方式
 */
export function Encrypt(params) {
    var secretCrypto = CryptoJS.enc.Utf8.parse("asdsdfs112323242")
    if (params != null && params != '') {
        params.date = new Date().getTime();
        var paramsCrypto = CryptoJS.enc.Utf8.parse(JSON.stringify(params));
        var encrypt = CryptoJS.AES.encrypt(paramsCrypto, secretCrypto, {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7
        });
        return CryptoJS.enc.Base64.stringify(encrypt.ciphertext);
    }
    return null;
}

/**
 * AES解密
 * 参数解密方式
 */
export function Decrypt(params) {
    var key = CryptoJS.enc.Utf8.parse("asdsdfs112323242");
    var decrypt = CryptoJS.AES.decrypt(params, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}

/**
 * 生成随机数（前端密钥）
 */
const keys = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
export const randomWord = (flag, min, max) => {
    let strKey = ""
    let range = min

    if (flag) {
        range = Math.round(Math.random() * (max - min)) + min;
    }

    for (let i = 0; i < range; i++) {
        let pos = Math.round(Math.random() * (keys.length - 1));
        strKey += keys[pos];
    }

    return strKey;
}

/***
 * 给随机数（前端密钥）加密（rsa加密）
 */
export const encrypt = (key, value) => {
    jsEncrypt.setPublicKey(key)
    return jsEncrypt.encrypt(value)
}
