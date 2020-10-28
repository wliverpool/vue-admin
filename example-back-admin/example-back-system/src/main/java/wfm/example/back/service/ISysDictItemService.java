package wfm.example.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.model.SysDictItem;

import java.util.List;

/**
 * 数据字典项服务接口
 * @author 吴福明
 */
public interface ISysDictItemService extends IService<SysDictItem> {

    public List<SysDictItem> selectItemsByMainId(String mainId);

}
