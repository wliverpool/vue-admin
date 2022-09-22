package wfm.example.back.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wfm.example.back.sys.mapper.SysDictItemMapper;
import wfm.example.back.sys.model.SysDictItem;
import wfm.example.back.sys.service.ISysDictItemService;

import java.util.List;

/**
 * 字典项服务实现类
 * @author 吴福明
 */
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {

    @Autowired
    private SysDictItemMapper sysDictItemMapper;

    @Override
    public List<SysDictItem> selectItemsByMainId(String mainId) {
        return sysDictItemMapper.selectItemsByMainId(mainId);
    }
}
