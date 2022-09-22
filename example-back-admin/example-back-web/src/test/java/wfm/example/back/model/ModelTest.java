package wfm.example.back.model;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wfm.example.back.sys.model.SysUser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

@Slf4j
public class ModelTest {

    @Test
    public void testSerial() throws Exception{
        SysUser sysUser = new SysUser();
        sysUser.setId("dsafasdf");
        sysUser.setUsername("dasfasdf");
        sysUser.setCreateTime(new Date());
        sysUser.setCreateBy("1212121");
        String userString = JSON.toJSONString(sysUser);
        log.info(userString);
        SysUser user = JSON.parseObject(userString, SysUser.class);
        log.info(user.getId());
        log.info(user.getUsername());
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/Users/wufuming/Desktop/111.txt"));
        oos.writeObject(sysUser);
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/Users/wufuming/Desktop/111.txt"));
        SysUser user2= (SysUser) ois.readObject();
        log.info(JSON.toJSONString(user2));
    }

}
