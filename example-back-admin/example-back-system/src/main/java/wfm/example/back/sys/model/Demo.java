package wfm.example.back.sys.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import wfm.example.back.common.model.BaseModel;
import wfm.example.back.common.model.BaseStringIDModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 测试demo
 * @author: 吴福明
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("demo")
public class Demo extends BaseStringIDModel {
    
    /** 部门编码 */
    private String sysOrgCode;
    
    /** 姓名 */
    private String name;
    
    /** 关键词 */
    private String keyWord;
    
    /** 打卡时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date punchTime;
    
    /** 工资 */
    private BigDecimal salaryMoney;
    
    /** 奖金 */
    private Double bonusMoney;
    
    /** 性别 {男:1,女:2} */
    private String sex;
    
    /** 年龄 */
    private Integer age;
    
    /** 生日 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    
    /** 邮箱 */
    private String email;
    
    /** 个人简介 */
    private String content;
}
