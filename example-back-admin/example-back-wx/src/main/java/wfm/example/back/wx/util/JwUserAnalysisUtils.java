package wfm.example.back.wx.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import wfm.example.back.wx.exception.WexinReqException;
import wfm.example.back.wx.model.dataCube.WxDataCubeStreamUserCumulateParam;
import wfm.example.back.wx.model.dataCube.WxDataCubeStreamUserSummaryParam;
import wfm.example.back.wx.model.useranalysis.UserAnalysisRtnInfo;
import wfm.example.back.wx.service.WeiXinReqService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户分析数据接口
 * @author 吴福明
 */

@Slf4j
public class JwUserAnalysisUtils {

    private static Long getDays(String Date1,String Date2){
        return Date.valueOf(Date1).getTime() /86400000L - Date.valueOf(Date2).getTime() / 86400000L;
    }

    /**
     * 获取用户增减数据,传入的日期最大间隔为7天
     * @param @param accesstoken
     * @param @return    设定文件 
     * @return UserAnalysisRtnInfo    返回类型 
     * @throws
     */
    public static List<UserAnalysisRtnInfo> getUserSummary(String accesstoken, String begin_date, String end_date) throws WexinReqException{
        if(accesstoken != null){
            if(getDays(end_date,begin_date) >= 7){
                log.error("传入的日期间隔大于7天");
                return null;
            }else{
                WxDataCubeStreamUserSummaryParam userAnalysis = new WxDataCubeStreamUserSummaryParam();
                userAnalysis.setAccess_token(accesstoken);
                userAnalysis.setBegin_date(begin_date);
                userAnalysis.setEnd_date(end_date);

                JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(userAnalysis);
                List<UserAnalysisRtnInfo> userAnalysisRtnInfoList = new ArrayList<UserAnalysisRtnInfo>();
                Object error = result.get("errcode");

                JSONArray array = null;
                UserAnalysisRtnInfo userAnalysisRtnInfo = null;
                array = (JSONArray) result.get("list");
                for (Object object : array) {
                    userAnalysisRtnInfo = (UserAnalysisRtnInfo) JSONObject
                            .toBean((JSONObject) object,
                                    UserAnalysisRtnInfo.class);
                    userAnalysisRtnInfoList.add(userAnalysisRtnInfo);
                }
                return userAnalysisRtnInfoList;
            }
        }
        return null;
    }

    /**
     * 获取累计用户数据
     * @param accesstoken
     * @param begin_date
     * @param end_date
     * @return
     * @throws WexinReqException
     */
    public static List<UserAnalysisRtnInfo> getUserCumulate(String accesstoken,String begin_date,String end_date) throws WexinReqException {
        if(accesstoken != null){
            if(getDays(end_date,begin_date) >= 7){
                log.error("传入的日期间隔大于7天");
                return null;
            }else{
                WxDataCubeStreamUserCumulateParam userCumulate = new WxDataCubeStreamUserCumulateParam();
                userCumulate.setAccess_token(accesstoken);
                userCumulate.setBegin_date(begin_date);
                userCumulate.setEnd_date(end_date);

                JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(userCumulate);
                List<UserAnalysisRtnInfo> userAnalysisRtnInfoList = new ArrayList<UserAnalysisRtnInfo>();
                Object error = result.get("errcode");

                JSONArray array = null;
                UserAnalysisRtnInfo userAnalysisRtnInfo = null;
                array = (JSONArray) result.get("list");
                for (Object object : array) {
                    userAnalysisRtnInfo = (UserAnalysisRtnInfo) JSONObject
                            .toBean((JSONObject) object,
                                    UserAnalysisRtnInfo.class);
                    userAnalysisRtnInfoList.add(userAnalysisRtnInfo);
                }
                return userAnalysisRtnInfoList;
            }
        }
        return null;
    }
    
}
