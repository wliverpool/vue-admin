package wfm.example.back.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * rsa加密验签帮助类
 * @author 吴福明
 */
@Slf4j
public class RSASignUtils {

    /**
     * 编码格式
     */
    private static final String CHARSET_UTF_8 = "utf-8";

    /**
     * buildRequestPara 方法
     *
     * @param sParaTemp  参数
     * @param privateKey 参数
     * @return
     */
    public static Map<String, String> buildRequestPara(Map<String, String> sParaTemp, String privateKey) {
        // 除去数组中的空值和签名参数
        Map<String, String> sPara = paraFilter(sParaTemp);
        // 生成签名结果
        String sign = buildRequestSign(sPara, privateKey);
        // 签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", sign);
        sPara.put("sign_type", "RSA");

        return sPara;
    }

    /**
     * buildRequestParaForTenPay 方法
     *
     * @param sParaTemp 参数
     * @return
     */
    public static Map<String, String> buildRequestParaForTenPay(Map<String, String> sParaTemp) {
        // 除去数组中的空值和签名参数
        return paraFilterForTenpay(sParaTemp);
    }

    /**
     * messageDigest 方法
     *
     * @param text      参数
     * @param publicKey 参数
     * @return
     */
    public static String messageDigest(String text, String publicKey) {
        StringBuilder sbText = new StringBuilder();
        sbText.append(text);
        sbText.append(publicKey);
        return DigestUtils.md5Hex(text.getBytes());
    }

    /**
     * buildRequestSign 方法
     *
     * @param sPara      参数
     * @param privateKey 参数
     * @return
     */
    public static String buildRequestSign(Map<String, String> sPara, String privateKey) {
        // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String prestr = createLinkString(sPara);
        log.debug("排序后的字符串：---------------------"+prestr);
        return sign(prestr, privateKey, CHARSET_UTF_8);
    }

    public static String buildRequestSignToPayment(Map<String, String> sPara, String privateKey){
        //过滤空值和签名参数
        Map<String, String> sParam = paraFilter(sPara);
        return buildRequestSign(sParam,privateKey);
    }

    /**
     * sign 方法
     *
     * @param content      参数
     * @param privateKey   参数
     * @param inputCharset 参数
     * @return
     */
    public static String sign(String content, String privateKey, String inputCharset) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            Signature signature = Signature.getInstance("SHA1WithRSA");

            signature.initSign(priKey);
            signature.update(content.getBytes(inputCharset));

            byte[] signed = signature.sign();

            return Base64.encodeBase64String(signed);
        } catch (InvalidKeyException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidKeySpecException e) {
            log.error(e.getMessage(), e);
        } catch (SignatureException e) {
            log.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * createLinkString 方法
     *
     * @param params 参数
     * @return
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuffer sbPreStr = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            // 拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                sbPreStr.append(key);
                sbPreStr.append("=");
                sbPreStr.append(value);
            } else {
                sbPreStr.append(key);
                sbPreStr.append("=");
                sbPreStr.append(value);
                sbPreStr.append("&");
            }
        }

        return String.valueOf(sbPreStr);
    }

    /**
     * paraFilter 方法
     *
     * @param sArray 参数
     * @return
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new LinkedHashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        Iterator<Map.Entry<String, String>> ite = sArray.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, String> entry = ite.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * paraFilterForTenpay 方法
     *
     * @param sArray 参数
     * @return
     */
    public static Map<String, String> paraFilterForTenpay(Map<String, String> sArray) {
        Map<String, String> result = new LinkedHashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        Iterator<Map.Entry<String, String>> ite = sArray.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, String> entry = ite.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null || value.equals("")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * verifySign 方法
     *
     * @param params    参数
     * @param publicKey 参数
     * @param sign      参数
     * @return
     */
    public static boolean verifySign(Map<String, String> params, String publicKey, String sign) {
        // 过滤空值、sign与sign_type参数
        Map<String, String> sParaNew = paraFilter(params);
        // 获取待签名字符串
        String preSignStr = createLinkString(sParaNew);
        // 获得签名验证结果
        return verify(preSignStr, sign, publicKey, CHARSET_UTF_8);

    }

    /**
     * RSA验签名检查
     *
     * @param content      待签名数据
     * @param sign         签名值
     * @param publicKey    公钥
     * @param inputCharset 编码格式
     * @return
     */
    public static boolean verify(String content, String sign, String publicKey, String inputCharset) {

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            Signature signature = Signature.getInstance("SHA1WithRSA");

            signature.initVerify(pubKey);
            signature.update(content.getBytes(inputCharset));

            return signature.verify(Base64.decodeBase64(sign));
        } catch (InvalidKeyException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidKeySpecException e) {
            log.error(e.getMessage(), e);
        } catch (SignatureException e) {
            log.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }


    /**
     * 公钥加密pin
     * @param PK
     * @param pin
     * @return
     */
    public static String pkEncPin(String PK, String pin) {
        //LOGGER.info("encryption pin={},publicKey={}",pin,PK);
        String contentText = "";
        contentText = pin + "FFFFFFFFFF";
        String pass = "";
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            BigInteger n = new BigInteger(PK, 16);
            BigInteger e = new BigInteger("03", 16);
            RSAPublicKeySpec spec = new RSAPublicKeySpec(n, e);
            //获取公钥
            PublicKey pubkey = keyFactory.generatePublic(spec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte plaintext[] = null;
            plaintext = (Hex.decodeHex(contentText.toCharArray()));
            byte[] enBytes = cipher.doFinal(plaintext);
            //pass = str2HexStr(enBytes);
            pass = new String(Hex.encodeHexString(enBytes));
        } catch (DecoderException de) {
            log.error(de.getMessage(), de);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidKeySpecException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            log.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            log.error(e.getMessage(), e);
        }
        return pass;
    }

}
