package wfm.example.back.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.sys.model.SysDictItem;

import java.util.List;

/**
 * 数据字典项服务接口
 * @author 吴福明
 */
public interface ISysDictItemService extends IService<SysDictItem> {

    public List<SysDictItem> selectItemsByMainId(String mainId);

}
