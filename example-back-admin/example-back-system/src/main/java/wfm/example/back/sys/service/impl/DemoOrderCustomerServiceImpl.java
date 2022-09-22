package wfm.example.back.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wfm.example.back.sys.mapper.DemoOrderCustomerMapper;
import wfm.example.back.sys.model.DemoOrderCustomer;
import wfm.example.back.sys.service.IDemoOrderCustomerService;

import java.util.List;

/**
 * 订单客户服务实现类
 * @author 吴福明
 */

@Service
public class DemoOrderCustomerServiceImpl extends ServiceImpl<DemoOrderCustomerMapper, DemoOrderCustomer> implements IDemoOrderCustomerService {

    @Autowired
    private DemoOrderCustomerMapper orderCustomerMapper;

    @Override
    public List<DemoOrderCustomer> selectCustomersByMainId(String mainId) {
        return orderCustomerMapper.selectCustomersByMainId(mainId);
    }

}
