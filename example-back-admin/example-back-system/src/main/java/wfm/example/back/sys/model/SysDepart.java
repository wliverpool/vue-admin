package wfm.example.back.sys.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import wfm.example.back.common.aspect.Dict;
import wfm.example.back.common.dto.DepartIdDTO;
import wfm.example.back.common.dto.SysDepartTreeDTO;
import wfm.example.back.common.model.BaseStringIDModel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 部门表
 * @author 吴福明
 */
@Data
@Accessors(chain = true)
@TableName("sys_depart")
public class SysDepart extends BaseStringIDModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**父机构ID*/
    private String parentId;

    /**机构/部门名称*/
    private String departName;

    /**英文名*/
    private String departNameEn;

    /**缩写*/
    private String departNameAbbr;

    /**排序*/
    private Integer departOrder;

    /**描述*/
    private String description;

    /**机构类别 1组织机构，2岗位*/
    private String orgCategory;

    /**机构类型*/
    private String orgType;

    /**机构编码*/
    private String orgCode;

    /**手机号*/
    private String mobile;

    /**传真*/
    private String fax;

    /**地址*/
    private String address;

    /**备注*/
    private String memo;

    /**状态（1启用，0不启用）*/
    @Dict(dicCode = "depart_status")
    private String status;

    /**删除状态（0，正常，1已删除）*/
    @Dict(dicCode = "del_flag")
    private String delFlag;

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SysDepart depart = (SysDepart) o;
        return Objects.equals(getId(), depart.getId()) &&
                Objects.equals(parentId, depart.parentId) &&
                Objects.equals(departName, depart.departName) &&
                Objects.equals(departNameEn, depart.departNameEn) &&
                Objects.equals(departNameAbbr, depart.departNameAbbr) &&
                Objects.equals(departOrder, depart.departOrder) &&
                Objects.equals(description, depart.description) &&
                Objects.equals(orgCategory, depart.orgCategory) &&
                Objects.equals(orgType, depart.orgType) &&
                Objects.equals(orgCode, depart.orgCode) &&
                Objects.equals(mobile, depart.mobile) &&
                Objects.equals(fax, depart.fax) &&
                Objects.equals(address, depart.address) &&
                Objects.equals(memo, depart.memo) &&
                Objects.equals(status, depart.status) &&
                Objects.equals(delFlag, depart.delFlag) &&
                Objects.equals(getCreateBy(), depart.getCreateBy()) &&
                Objects.equals(getCreateTime(), depart.getCreateTime()) &&
                Objects.equals(getUpdateBy(), depart.getUpdateBy()) &&
                Objects.equals(getUpdateTime(), depart.getUpdateTime());
    }

    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getId(), parentId, departName,
                departNameEn, departNameAbbr, departOrder, description,orgCategory,
                orgType, orgCode, mobile, fax, address, memo, status,
                delFlag, getCreateBy(), getCreateTime(), getUpdateBy(), getUpdateTime());
    }

    /**
     * 将SysDepart对象转换成SysDepartTreeDTO对象
     */
    public SysDepartTreeDTO transferToDTO() {
        SysDepartTreeDTO treeDTO = new SysDepartTreeDTO();
        BeanUtils.copyProperties(this,treeDTO);
        treeDTO.setKey(this.getId());
        treeDTO.setValue(this.getId());
        treeDTO.setTitle(this.getDepartName());
        return treeDTO;
    }

    /**
     * 该方法为用户部门的实现类所使用
     * @return
     */
    public DepartIdDTO convertByUserDepart() {
        DepartIdDTO dto = new DepartIdDTO();
        dto.setKey(this.getId());
        dto.setValue(this.getId());
        dto.setTitle(this.getDepartName());
        return dto;
    }

}