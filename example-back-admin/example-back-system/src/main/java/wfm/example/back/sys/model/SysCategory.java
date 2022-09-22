package wfm.example.back.sys.model;

import java.io.Serializable;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import wfm.example.back.common.model.BaseStringIDModel;

/**
 * 分类字典
 * @author: 吴福明
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_category")
public class SysCategory extends BaseStringIDModel implements Serializable,Comparable<SysCategory>{

    private static final long serialVersionUID = 1L;

    /**父级节点*/
    private String pid;

    /**类型名称*/
    private String name;

    /**类型编码*/
    private String code;

    /**所属部门*/
    private String sysOrgCode;

    /**是否有子节点*/
    private String hasChild;

    @Override
    public int compareTo(SysCategory o) {
        //比较条件我们定的是按照code的长度升序
        // <0：当前对象比传入对象小。
        // =0：当前对象等于传入对象。
        // >0：当前对象比传入对象大。
        int	 s = this.code.length() - o.code.length();
        return s;
    }

    @Override
    public String toString() {
        return "SysCategory [code=" + code + ", name=" + name + "]";
    }
}
