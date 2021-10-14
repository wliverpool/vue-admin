package wfm.example.back.activiti.config;

import cn.hutool.core.util.IdUtil;
import org.activiti.engine.impl.cfg.IdGenerator;

public class MyUUIDgenerator  implements IdGenerator {
    @Override
    public String getNextId() {
        String uuid = "act-"+IdUtil.randomUUID();
        return uuid;
    }
}
