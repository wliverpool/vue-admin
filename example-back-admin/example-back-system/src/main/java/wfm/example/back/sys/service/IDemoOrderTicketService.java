package wfm.example.back.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.sys.model.DemoOrderTicket;

import java.util.List;

/**
 * 订单机票服务接口
 * @author 吴福明
 */

public interface IDemoOrderTicketService extends IService<DemoOrderTicket> {

    public List<DemoOrderTicket> selectTicketsByMainId(String mainId);

}
