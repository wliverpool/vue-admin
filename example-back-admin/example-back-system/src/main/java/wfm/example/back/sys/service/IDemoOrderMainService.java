package wfm.example.back.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.sys.model.DemoOrderCustomer;
import wfm.example.back.sys.model.DemoOrderMain;
import wfm.example.back.sys.model.DemoOrderTicket;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 订单服务接口
 * @author 吴福明
 */

public interface IDemoOrderMainService extends IService<DemoOrderMain> {

    /**
     * 添加一对多
     *
     */
    public void saveMain(DemoOrderMain orderMain,List<DemoOrderCustomer> orderCustomerList,List<DemoOrderTicket> orderTicketList) ;

    /**
     * 修改一对多
     *
     */
    public void updateMain(DemoOrderMain orderMain, List<DemoOrderCustomer> orderCustomerList, List<DemoOrderTicket> orderTicketList);

    /**
     * 删除一对多
     * @param id
     */
    public void delMain (String id);

    /**
     * 批量删除一对多
     * @param idList
     */
    public void delBatchMain (Collection<? extends Serializable> idList);

}
