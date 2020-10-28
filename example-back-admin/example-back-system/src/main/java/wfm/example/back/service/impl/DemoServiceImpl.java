package wfm.example.back.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wfm.example.back.mapper.DemoMapper;
import wfm.example.back.model.Demo;
import wfm.example.back.query.QueryGenerator;
import wfm.example.back.service.IDemoService;
import wfm.example.common.constant.CacheConstant;

@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo> implements IDemoService {

    /**
     * 事务控制在service层面
     * 加上注解：@Transactional，声明的方法就是一个独立的事务（有异常DB操作全部回滚）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testTran() {
        Demo pp = new Demo();
        pp.setAge(1111);
        pp.setName("测试事务  小白兔 1");
        this.baseMapper.insert(pp);

        Demo pp2 = new Demo();
        pp2.setAge(2222);
        pp2.setName("测试事务  小白兔 2");
        this.baseMapper.insert(pp2);

        Integer.parseInt("hello");//自定义异常

        Demo pp3 = new Demo();
        pp3.setAge(3333);
        pp3.setName("测试事务  小白兔 3");
        this.baseMapper.insert(pp3);
        return ;
    }


    /**
     * 缓存注解测试： redis
     */
    @Override
    @Cacheable(cacheNames = CacheConstant.TEST_DEMO_CACHE, key = "#id")
    public Demo getByIdCacheable(String id) {
        Demo t = this.baseMapper.selectById(id);
        System.err.println("---未读缓存，读取数据库---");
        System.err.println(t);
        return t;
    }


    @Override
    public IPage<Demo> queryListWithPermission(int pageSize, int pageNo) {
        Page<Demo> page = new Page<>(pageNo, pageSize);
        //编程方式，获取当前请求的数据权限规则SQL片段
        String sql = QueryGenerator.installAuthJdbc(Demo.class);
        return this.baseMapper.queryListWithPermission(page, sql);
    }

}

