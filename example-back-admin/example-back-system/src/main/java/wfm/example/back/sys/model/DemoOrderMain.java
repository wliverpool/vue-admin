package wfm.example.back.sys.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单
 * @Author: 吴福明
 */
@Data
@TableName("demo_order_main")
public class DemoOrderMain implements Serializable {
    
    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**订单号*/
    private String orderCode;
    
    /**订单类型*/
    private String ctype;
    
    /**订单日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date orderDate;
    
    /**订单金额*/
    private Double orderMoney;
    
    /**订单备注*/
    private String content;
    
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
