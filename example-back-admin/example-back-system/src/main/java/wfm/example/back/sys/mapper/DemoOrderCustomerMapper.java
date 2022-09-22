package wfm.example.back.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import wfm.example.back.sys.model.DemoOrderCustomer;

import java.util.List;

/**
 * 订单客户mapper
 * @author 吴福明
 */

public interface DemoOrderCustomerMapper extends BaseMapper<DemoOrderCustomer> {

    /**
     *  通过主表外键批量删除客户
     * @param mainId
     * @return
     */
    @Delete("DELETE FROM DEMO_ORDER_CUSTOMER WHERE ORDER_ID = #{mainId}")
    public boolean deleteCustomersByMainId(String mainId);

    @Select("SELECT * FROM DEMO_ORDER_CUSTOMER WHERE ORDER_ID = #{mainId}")
    public List<DemoOrderCustomer> selectCustomersByMainId(String mainId);

}
