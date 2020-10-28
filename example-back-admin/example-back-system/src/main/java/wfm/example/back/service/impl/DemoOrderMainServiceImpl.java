package wfm.example.back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wfm.example.back.mapper.DemoOrderCustomerMapper;
import wfm.example.back.mapper.DemoOrderMainMapper;
import wfm.example.back.mapper.DemoOrderTicketMapper;
import wfm.example.back.model.DemoOrderCustomer;
import wfm.example.back.model.DemoOrderMain;
import wfm.example.back.model.DemoOrderTicket;
import wfm.example.back.service.IDemoOrderMainService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Service
public class DemoOrderMainServiceImpl extends ServiceImpl<DemoOrderMainMapper, DemoOrderMain> implements IDemoOrderMainService {

    @Autowired
    private DemoOrderMainMapper orderMainMapper;
    @Autowired
    private DemoOrderCustomerMapper orderCustomerMapper;
    @Autowired
    private DemoOrderTicketMapper orderTicketMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(DemoOrderMain orderMain, List<DemoOrderCustomer> orderCustomerList, List<DemoOrderTicket> orderTicketList) {
        orderMainMapper.insert(orderMain);
        if (orderCustomerList != null) {
            for (DemoOrderCustomer entity : orderCustomerList) {
                entity.setOrderId(orderMain.getId());
                orderCustomerMapper.insert(entity);
            }
        }
        if (orderTicketList != null) {
            for (DemoOrderTicket entity : orderTicketList) {
                entity.setOrderId(orderMain.getId());
                orderTicketMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(DemoOrderMain orderMain, List<DemoOrderCustomer> orderCustomerList, List<DemoOrderTicket> orderTicketList) {
        orderMainMapper.updateById(orderMain);

        //1.先删除子表数据
        orderTicketMapper.deleteTicketsByMainId(orderMain.getId());
        orderCustomerMapper.deleteCustomersByMainId(orderMain.getId());

        //2.子表数据重新插入
        if (orderCustomerList != null) {
            for (DemoOrderCustomer entity : orderCustomerList) {
                entity.setOrderId(orderMain.getId());
                orderCustomerMapper.insert(entity);
            }
        }
        if (orderTicketList != null) {
            for (DemoOrderTicket entity : orderTicketList) {
                entity.setOrderId(orderMain.getId());
                orderTicketMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        orderMainMapper.deleteById(id);
        orderTicketMapper.deleteTicketsByMainId(id);
        orderCustomerMapper.deleteCustomersByMainId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for(Serializable id:idList) {
            orderMainMapper.deleteById(id);
            orderTicketMapper.deleteTicketsByMainId(id.toString());
            orderCustomerMapper.deleteCustomersByMainId(id.toString());
        }
    }
}
