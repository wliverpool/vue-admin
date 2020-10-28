package wfm.example.back.vo;

import lombok.Data;
import wfm.example.back.model.DemoOrderCustomer;
import wfm.example.back.model.DemoOrderTicket;

import java.util.Date;
import java.util.List;

/**
 * 订单分页列表vo
 * @author 吴福明
 */

@Data
public class DemoOrderMainPage {

    /**主键*/
    private String id;
    
    /**订单号*/
    private String orderCode;
    
    /**订单类型*/
    private String ctype;
    
    /**订单日期*/
    private java.util.Date orderDate;
    
    /**订单金额*/
    private Double orderMoney;
    
    /**订单备注*/
    private String content;
    
    /**创建人*/
    private String createBy;
    
    /**创建时间*/
    private Date createTime;
    
    /**修改人*/
    private String updateBy;
    
    /**修改时间*/
    private Date updateTime;

    private List<DemoOrderCustomer> orderCustomerList;
    
    private List<DemoOrderTicket> orderTicketList;
    
}
