package wfm.example.back.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import wfm.example.back.sys.model.DemoOrderTicket;

import java.util.List;

/**
 * 订单机票mapper
 * @author 吴福明
 */

public interface DemoOrderTicketMapper extends BaseMapper<DemoOrderTicket> {

    /**
     *  通过主表外键批量删除客户
     * @param mainId
     * @return
     */
    @Delete("DELETE FROM DEMO_ORDER_TICKET WHERE ORDER_ID = #{mainId}")
    public boolean deleteTicketsByMainId(String mainId);


    @Select("SELECT * FROM DEMO_ORDER_TICKET WHERE ORDER_ID = #{mainId}")
    public List<DemoOrderTicket> selectTicketsByMainId(String mainId);

}
