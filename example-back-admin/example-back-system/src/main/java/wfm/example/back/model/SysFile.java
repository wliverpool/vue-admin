package wfm.example.back.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统文件上传
 * @author 吴福明
 */

@Data
@TableName("sys_file")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysFile extends BaseModel {

    private String fileName;

    private String url;

}

