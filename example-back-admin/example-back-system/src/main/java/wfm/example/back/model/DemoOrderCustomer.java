package wfm.example.back.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单客户
 * @Author: 吴福明
 */

@Data
@TableName("demo_order_customer")
public class DemoOrderCustomer implements Serializable {
    
    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**客户名*/
    private String name;
    
    /**性别*/
    private String sex;
    
    /**身份证号码*/
    private String idcard;
    
    /**身份证扫描件*/
    private String idcardPic;
    
    /**电话1*/
    private String telphone;
    
    /**外键*/
    private String orderId;
    
    /**创建人*/
    private String createBy;
    
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**修改人*/
    private String updateBy;
    
    /**修改时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
