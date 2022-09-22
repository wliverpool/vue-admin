package wfm.example.back.sys.service;

import wfm.example.back.sys.model.SysMessageTemplate;

import java.util.List;

/**
 * 消息模板服务接口
 * @author: 吴福明
 */
public interface ISysMessageTemplateService extends IBaseService<SysMessageTemplate> {

    List<SysMessageTemplate> selectByCode(String code);

}
