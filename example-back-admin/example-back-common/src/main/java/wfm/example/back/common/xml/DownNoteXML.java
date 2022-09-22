package wfm.example.back.common.xml;


import javax.xml.bind.annotation.*;

/**
 * 短信报文类
 * @author 吴福明
 */

/**
 * 表示使用这个类中的 private 非静态字段作为 XML 的序列
 */
@XmlAccessorType(XmlAccessType.FIELD)//
@XmlRootElement(name="DATA")
/**
 * xml格式数据的显示的顺序名字要和定义变量的一样，而不是@XmlElement中的name
 */
@XmlType(propOrder={"orgId","smsNo","smsType","mobile","cv","cardNo","amounts","balance","sms","respFlag","respDetail","requestTime"})
public class DownNoteXML {

    //定义xml中显示的数据
    /**
     * 机构号
     */
    @XmlElement(name="ORG_ID",required=true)
    private  String orgId;

    /**
     * 流水号
     */
    @XmlElement(name="SMS_NO",required=true)
    private String smsNo;

    /**
     * 短信类型
     */
    @XmlElement(name="SMS_TYPE",required=true)
    private String smsType;

    /**
     * 号码
     */
    @XmlElement(name="MOBILE",required=true)
    private String mobile;

    /**
     * 校验码
     */
    @XmlElement(name="CV",required=true)
    private String cv;

    /**
     * 卡号
     */
    @XmlElement(name="CARDNO",required=true)
    private String cardNo;

    /**
     * 交易金额
     */
    @XmlElement(name="AMOUNTS",required=true)
    private String amounts;

    /**
     * 余额
     */
    @XmlElement(name="BALANCE",required=true)
    private String balance;

    /**
     * 短信模版号
     */
    @XmlElement(name="SMS",required=true)
    private String sms;

    /**
     * 返回标志
     */
    @XmlElement(name="RESP_FLAG",required=true)
    private String respFlag;

    /**
     * 返回描述
     */
    @XmlElement(name="RESP_DETAIL",required=true)
    private String respDetail;

    /**
     * 返回描述
     */
    @XmlElement(name="REQUEST_TIME",required=true)
    private String requestTime;

    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    public String getSmsNo() {
        return smsNo;
    }
    public void setSmsNo(String smsNo) {
        this.smsNo = smsNo;
    }
    public String getSmsType() {
        return smsType;
    }
    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getCv() {
        return cv;
    }
    public void setCv(String cv) {
        this.cv = cv;
    }
    public String getCardNo() {
        return cardNo;
    }
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public String getAmounts() {
        return amounts;
    }
    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }
    public String getBalance() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }
    public String getSms() {
        return sms;
    }
    public void setSms(String sms) {
        this.sms = sms;
    }
    public String getRespFlag() {
        return respFlag;
    }
    public void setRespFlag(String respFlag) {
        this.respFlag = respFlag;
    }
    public String getRespDetail() {
        return respDetail;
    }
    public void setRespDetail(String respDetail) {
        this.respDetail = respDetail;
    }
    public String getRequestTime() {
        return requestTime;
    }
    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
    @Override
    public String toString() {
        return "DownNoteXML [orgId=" + orgId + ", smsNo=" + smsNo
                + ", smsType=" + smsType + ", mobile=" + mobile + ", cv=" + cv
                + ", cardNo=" + cardNo + ", amounts=" + amounts + ", balance="
                + balance + ", sms=" + sms + ", respFlag=" + respFlag
                + ", respDetail=" + respDetail + ", requestTime=" + requestTime
                + "]";
    }
}
