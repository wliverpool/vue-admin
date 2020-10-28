package wfm.example.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 部门用户vo
 * @author 吴福明
 */

@Data
public class SysDepartUsersVo implements Serializable {

    /**部门id*/
    private String depId;
    /**对应的用户id集合*/
    private List<String> userIdList;

    public SysDepartUsersVo(String depId, List<String> userIdList) {
        super();
        this.depId = depId;
        this.userIdList = userIdList;
    }

    public SysDepartUsersVo(){

    }

}
