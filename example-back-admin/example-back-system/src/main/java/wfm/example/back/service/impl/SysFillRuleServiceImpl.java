package wfm.example.back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wfm.example.back.mapper.SysFillRuleMapper;
import wfm.example.back.model.SysFillRule;
import wfm.example.back.service.ISysFillRuleService;

/**
 * 填值规则
 * @author: 吴福明
 */
@Service("sysFillRuleServiceImpl")
public class SysFillRuleServiceImpl extends ServiceImpl<SysFillRuleMapper, SysFillRule> implements ISysFillRuleService {

}
