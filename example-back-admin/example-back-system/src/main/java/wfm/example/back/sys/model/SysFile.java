package wfm.example.back.sys.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import wfm.example.back.common.model.BaseModel;
import wfm.example.back.common.model.BaseStringIDModel;

/**
 * 系统文件上传
 * @author 吴福明
 */

@Data
@TableName("sys_file")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysFile extends BaseStringIDModel {

    private String fileName;

    private String url;

}

