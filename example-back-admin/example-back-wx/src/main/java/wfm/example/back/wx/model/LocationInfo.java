package wfm.example.back.wx.model;

import wfm.example.back.wx.annotation.ReqType;

@ReqType("getLocationInfo")
public class LocationInfo extends WeixinReqParam {
    // 图片地址
    private String filePathName;

    public String getFilePathName() {
        return filePathName;
    }

    public void setFilePathName(String filePathName) {
        this.filePathName = filePathName;
    }



}
