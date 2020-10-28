package wfm.example.back.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import wfm.example.back.model.BaseModel;
import wfm.example.back.service.IBaseService;

/**
 * ServiceImpl基础实现类
 * @Author: 吴福明
 * @param <M>
 * @param <T>
 */
@Slf4j
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseModel> extends ServiceImpl<M, T> implements IBaseService<T> {
}
