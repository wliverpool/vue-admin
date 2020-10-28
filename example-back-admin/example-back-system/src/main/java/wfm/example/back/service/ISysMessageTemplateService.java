package wfm.example.back.service;

import wfm.example.back.model.SysMessageTemplate;

import java.util.List;

/**
 * 消息模板服务接口
 * @author: 吴福明
 */
public interface ISysMessageTemplateService extends IBaseService<SysMessageTemplate> {

    List<SysMessageTemplate> selectByCode(String code);

}
