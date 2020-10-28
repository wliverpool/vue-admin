package wfm.example.back.controller;


import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import wfm.example.back.model.SysUser;
import wfm.example.back.redis.RedisDao;
import wfm.example.back.service.ISysBaseAPI;
import wfm.example.back.service.ISysUserService;
import wfm.example.back.vo.JwtUser;
import wfm.example.common.constant.CommonConstant;
import wfm.example.common.enums.DySmsEnum;
import wfm.example.common.util.DySmsHelperUtils;
import wfm.example.common.util.MD5Utils;
import wfm.example.common.util.ObjectConvertUtils;
import wfm.example.common.util.RandImageUtils;
import wfm.example.common.vo.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 登录controller
 * @author 吴福明
 */
@RestController
@RequestMapping("/sys")
@Slf4j
public class LoginController {

    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisDao redisDao;

    @Value("${system.sms.server}")
    private String smsServer;
    @Value("${system.sms.port}")
    private Integer smsPort;


    /**
     * 后台生成图形验证码 ：有效
     * @param response
     * @param key
     */
    @GetMapping(value = "/randomImage/{key}")
    public Result<String> randomImage(HttpServletResponse response, @PathVariable String key){
        Result<String> res = new Result<String>();
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES,4);
            String lowerCaseCode = code.toLowerCase();
            String realKey = MD5Utils.MD5Encode(lowerCaseCode + key, "utf-8");
            redisDao.set(realKey, lowerCaseCode, 60);
            String base64 = RandImageUtils.generate(code);
            res.setSuccess(true);
            res.setResult(base64);
        } catch (Exception e) {
            res.error500("获取验证码出错"+e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取首页动态访问量图表信息
     * @return
     */
    @GetMapping("loginfo")
    public Result<JSONObject> loginfo() {
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        //update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
        // 获取一天的开始和结束时间
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dayStart = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date dayEnd = calendar.getTime();
        obj.put("totalVisitCount", 789412);
        obj.put("todayVisitCount", 2372);
        obj.put("todayIp", 1167);
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    /**
     * 获取首页动态访问量图表访问量
     * @return
     */
    @GetMapping("visitInfo")
    public Result<List<Map<String,Object>>> visitInfo() {
        Result<List<Map<String,Object>>> result = new Result<List<Map<String,Object>>>();
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date dayEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date dayStart = calendar.getTime();
        Map<String, Object> day1 = new HashMap<>();
        day1.put("tian","2020-10-09");
        day1.put("ip",1371);
        day1.put("visit",2657);
        day1.put("type","10-09");
        Map<String, Object> day2 = new HashMap<>();
        day2.put("tian","2020-10-10");
        day2.put("ip",2040);
        day2.put("visit",4014);
        day2.put("type","10-10");
        Map<String, Object> day3 = new HashMap<>();
        day3.put("tian","2020-10-11");
        day3.put("ip",704);
        day3.put("visit",1692);
        day3.put("type","10-11");
        Map<String, Object> day4 = new HashMap<>();
        day4.put("tian","2020-10-12");
        day4.put("ip",2261);
        day4.put("visit",4111);
        day4.put("type","10-12");
        Map<String, Object> day5 = new HashMap<>();
        day5.put("tian","2020-10-13");
        day5.put("ip",1895);
        day5.put("visit",3765);
        day5.put("type","10-13");
        Map<String, Object> day6 = new HashMap<>();
        day6.put("tian","2020-10-14");
        day6.put("ip",1712);
        day6.put("visit",3312);
        day6.put("type","10-14");
        Map<String, Object> day7 = new HashMap<>();
        day7.put("tian","2020-10-15");
        day7.put("ip",1328);
        day7.put("visit",2781);
        day7.put("type","10-15");
        List<Map<String,Object>> list = Arrays.asList(day1,day2,day3,day4,day5,day6,day7);
        result.setResult(ObjectConvertUtils.toLowerCasePageList(list));
        return result;
    }

    /**
     * 短信登录接口
     *
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/sms")
    public Result<String> sms(@RequestBody JSONObject jsonObject) {
        Result<String> result = new Result<String>();
        String mobile = jsonObject.get("mobile").toString();
        //手机号模式 登录模式: "2"  注册模式: "1"
        String smsmode=jsonObject.get("smsmode").toString();
        log.info(mobile);
        if(ObjectConvertUtils.isEmpty(mobile)){
            result.setMessage("手机号不允许为空！");
            result.setSuccess(false);
            return result;
        }
        Object object = redisDao.get(mobile);
        if (object != null) {
            result.setMessage("验证码10分钟内，仍然有效！");
            result.setSuccess(false);
            return result;
        }

        //随机数
        String captcha = RandomUtil.randomNumbers(6);
        JSONObject obj = new JSONObject();
        obj.put("code", captcha);
        try {
            boolean b = false;
            //注册模板
            if (CommonConstant.SMS_TPL_TYPE_1.equals(smsmode)) {
                SysUser sysUser = sysUserService.getUserByPhone(mobile);
                if(sysUser!=null) {
                    result.error500(" 手机号已经注册，请直接登录！");
                    log.error("手机号:{}已经注册，请直接登录！",mobile);
                    return result;
                }
                b = DySmsHelperUtils.sendSms(smsServer, smsPort, captcha, mobile, DySmsEnum.REGISTER_TEMPLATE_CODE.getTemplateCode());
            }else {
                //登录模式，校验用户有效性
                SysUser sysUser = sysUserService.getUserByPhone(mobile);
                result = sysUserService.checkUserIsEffective(sysUser);
                if(!result.isSuccess()) {
                    String message = result.getMessage();
                    if("该用户不存在，请注册".equals(message)){
                        result.error500("该用户不存在或未绑定手机号");
                    }
                    return result;
                }

                /**
                 * smsmode 短信模板方式  0 .登录模板、1.注册模板、2.忘记密码模板
                 */
                if (CommonConstant.SMS_TPL_TYPE_0.equals(smsmode)) {
                    //登录模板
                    b = DySmsHelperUtils.sendSms(smsServer, smsPort, captcha, mobile, DySmsEnum.LOGIN_TEMPLATE_CODE.getTemplateCode());
                } else if(CommonConstant.SMS_TPL_TYPE_2.equals(smsmode)) {
                    //忘记密码模板
                    b = DySmsHelperUtils.sendSms(smsServer, smsPort, captcha, mobile, DySmsEnum.FORGET_PASSWORD_TEMPLATE_CODE.getTemplateCode());
                }
            }

            if (!b) {
                result.setMessage("短信验证码发送失败,请稍后重试");
                result.setSuccess(false);
                return result;
            }
            //验证码10分钟内有效
            redisDao.set(mobile, captcha, 600);
            result.setSuccess(true);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500(" 短信接口未配置，请联系管理员！");
            return result;
        }
        return result;
    }

    /**
     * 登陆成功选择用户当前部门
     * @param user
     * @return
     */
    @RequestMapping(value = "/selectDepart", method = RequestMethod.PUT)
    public Result<JSONObject> selectDepart(@RequestBody SysUser user ,@AuthenticationPrincipal UserDetails userDetails) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = user.getUsername();
        if(ObjectConvertUtils.isEmpty(username)) {
            JwtUser sysUser = (JwtUser)userDetails;
            username = sysUser.getUsername();
        }
        String orgCode= user.getOrgCode();
        this.sysUserService.updateUserDepart(username, orgCode);
        SysUser sysUser = sysUserService.getUserByName(username);
        JSONObject obj = new JSONObject();
        obj.put("userInfo", sysUser);
        result.setResult(obj);
        return result;
    }

}
