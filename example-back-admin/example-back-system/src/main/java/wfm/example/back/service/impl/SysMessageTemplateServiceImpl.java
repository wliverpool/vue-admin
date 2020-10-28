package wfm.example.back.service.impl;

import org.springframework.stereotype.Service;
import wfm.example.back.mapper.SysMessageTemplateMapper;
import wfm.example.back.model.SysMessageTemplate;
import wfm.example.back.service.ISysMessageTemplateService;

import java.util.List;

/**
 * 消息模板服务实现类
 * @Author: 吴福明
 */
@Service
public class SysMessageTemplateServiceImpl extends BaseServiceImpl<SysMessageTemplateMapper, SysMessageTemplate> implements ISysMessageTemplateService {

    @Override
    public List<SysMessageTemplate> selectByCode(String code) {
        return baseMapper.selectByCode(code);
    }

}
