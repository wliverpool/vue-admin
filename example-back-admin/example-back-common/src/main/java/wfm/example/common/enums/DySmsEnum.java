package wfm.example.common.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 短信模版枚举
 * @author 吴福明
 */

public enum DySmsEnum {

    // SMS_175435174
    LOGIN_TEMPLATE_CODE("SMS_175435174","BACK_ADMIN","code"),
    // SMS_175435174
    FORGET_PASSWORD_TEMPLATE_CODE("10040","BACK_ADMIN","code"),
    // SMS_175430166
    REGISTER_TEMPLATE_CODE("10040","BACK_ADMIN","code"),
    SMS_MESSAGE_TEMPLATE_CODE("10040","BACK_ADMIN","code");

    /**
     * 短信模板编码
     */
    private String templateCode;
    /**
     * 签名
     */
    private String signName;
    /**
     * 短信模板必需的数据名称，多个key以逗号分隔，此处配置作为校验
     */
    private String keys;

    private DySmsEnum(String templateCode,String signName,String keys) {
        this.templateCode = templateCode;
        this.signName = signName;
        this.keys = keys;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public static DySmsEnum toEnum(String templateCode) {
        if(StringUtils.isEmpty(templateCode)){
            return null;
        }
        for(DySmsEnum item : DySmsEnum.values()) {
            if(item.getTemplateCode().equals(templateCode)) {
                return item;
            }
        }
        return null;
    }
}

