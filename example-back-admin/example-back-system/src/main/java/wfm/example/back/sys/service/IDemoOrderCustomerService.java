package wfm.example.back.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.sys.model.DemoOrderCustomer;

import java.util.List;

/**
 * 订单客户服务接口
 * @author 吴福明
 */

public interface IDemoOrderCustomerService extends IService<DemoOrderCustomer> {

    public List<DemoOrderCustomer> selectCustomersByMainId(String mainId);

}
