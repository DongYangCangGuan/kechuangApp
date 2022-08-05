const CryptoJS = require('crypto-js')
const CRYPTOJSKEY = "abcdefgabcdefg12";
/**
 * AES加密
 * 参数加密方式
 */
export function Encrypt(params) {
  const secretCrypto = CryptoJS.enc.Utf8.parse(CRYPTOJSKEY);
  if (params != null && params != '') {
    params.date = new Date().getTime();
    const paramsCrypto = CryptoJS.enc.Utf8.parse(JSON.stringify(params));
    const encrypt = CryptoJS.AES.encrypt(paramsCrypto, secretCrypto, {
      mode: CryptoJS.mode.ECB,
      padding: CryptoJS.pad.Pkcs7
    });
    return CryptoJS.enc.Base64.stringify(encrypt.ciphertext).toString();
  }
  return null;
}

/**
 * AES解密
 * 返回base64
 */
export function Decrypt(word) {
  const key = CryptoJS.enc.Utf8.parse(CRYPTOJSKEY);
  const decrypt = CryptoJS.AES.decrypt(word, key, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  });
  return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}
