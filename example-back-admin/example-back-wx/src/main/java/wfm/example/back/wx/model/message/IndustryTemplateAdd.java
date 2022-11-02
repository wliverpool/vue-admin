package wfm.example.back.wx.model.message;

import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.model.WeixinReqParam;

/**
 * 取多媒体文件
 *
 * @author 吴福明
 *
 */
@ReqType("industryTemplateAdd")
public class IndustryTemplateAdd extends WeixinReqParam {

    private String template_id_short;

    public String getTemplate_id_short() {
        return template_id_short;
    }

    public void setTemplate_id_short(String template_id_short) {
        this.template_id_short = template_id_short;
    }



}