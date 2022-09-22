package wfm.example.back.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wfm.example.back.sys.mapper.DemoOrderTicketMapper;
import wfm.example.back.sys.model.DemoOrderTicket;
import wfm.example.back.sys.service.IDemoOrderTicketService;

import java.util.List;

/**
 * 订单机票服务实现类
 * @author 吴福明
 */

@Service
public class DemoOrderTicketServiceImpl extends ServiceImpl<DemoOrderTicketMapper, DemoOrderTicket> implements IDemoOrderTicketService {

    @Autowired
    private DemoOrderTicketMapper orderTicketMapper;

    @Override
    public List<DemoOrderTicket> selectTicketsByMainId(String mainId) {
        return orderTicketMapper.selectTicketsByMainId(mainId);
    }

}
