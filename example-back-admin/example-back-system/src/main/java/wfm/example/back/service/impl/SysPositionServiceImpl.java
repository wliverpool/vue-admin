package wfm.example.back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wfm.example.back.mapper.SysPositionMapper;
import wfm.example.back.model.SysPosition;
import wfm.example.back.service.ISysPositionService;

/**
 * 职务服务实现类
 * @Author: 吴福明
 */
@Service
public class SysPositionServiceImpl extends ServiceImpl<SysPositionMapper, SysPosition> implements ISysPositionService {

}