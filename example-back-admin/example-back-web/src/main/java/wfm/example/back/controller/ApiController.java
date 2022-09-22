package wfm.example.back.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import wfm.example.back.common.vo.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试controller
 * @author 吴福明
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    private final String JSON_PATH = "classpath:json";

    /**
     * 通用json访问接口
     * @param filename
     * @return
     */
    @RequestMapping(value = "/json/{filename}", method = RequestMethod.GET)
    public String getJsonData(@PathVariable String filename) {
        String jsonPath = JSON_PATH + "/"+filename+".json";
        return readJson(jsonPath);
    }

    @GetMapping(value = "/asynTreeList")
    public String asynTreeList(String id) {
        return readJson(JSON_PATH + "/asyn_tree_list_" + id + ".json");
    }

    @GetMapping(value = "/user")
    public String user() {
        return readJson(JSON_PATH + "/user.json");
    }

    /**
     * 老的登录获取用户信息接口
     * @return
     */
    @GetMapping(value = "/user/info")
    public String userInfo() {
        return readJson(JSON_PATH + "/user_info.json");
    }

    @GetMapping(value = "/role")
    public String role() {
        return readJson(JSON_PATH + "/role.json");
    }

    @GetMapping(value = "/service")
    public String service() {
        return readJson(JSON_PATH + "/service.json");
    }

    @GetMapping(value = "/permission")
    public String permission() {
        return readJson(JSON_PATH + "/permission.json");
    }

    @GetMapping(value = "/permission/no-pager")
    public String permission_no_page() {
        return readJson(JSON_PATH + "/permission_no_page.json");
    }

    /**
     * 省市县
     */
    @GetMapping(value = "/area")
    public String area() {
        return readJson(JSON_PATH + "/area.json");
    }

    /**
     * 测试报表数据
     */
    @GetMapping(value = "/report/getYearCountInfo")
    public String getYearCountInfo() {
        return readJson(JSON_PATH + "/getCntrNoCountInfo.json");
    }
    @GetMapping(value = "/report/getMonthCountInfo")
    public String getMonthCountInfo() {
        return readJson(JSON_PATH + "/getCntrNoCountInfo.json");
    }
    @GetMapping(value = "/report/getCntrNoCountInfo")
    public String getCntrNoCountInfo() {
        return readJson(JSON_PATH + "/getCntrNoCountInfo.json");
    }
    @GetMapping(value = "/report/getCabinetCountInfo")
    public String getCabinetCountInfo() {
        return readJson(JSON_PATH + "/getCntrNoCountInfo.json");
    }

    /**
     * 实时磁盘监控
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/queryDiskInfo")
    public Result<List<Map<String,Object>>> queryDiskInfo(HttpServletRequest request, HttpServletResponse response){
        Result<List<Map<String,Object>>> res = new Result<>();
        try {
            // 当前文件系统类
            FileSystemView fsv = FileSystemView.getFileSystemView();
            // 列出所有windows 磁盘
            File[] fs = File.listRoots();
            log.info("查询磁盘信息:"+fs.length+"个");
            List<Map<String,Object>> list = new ArrayList<>();

            for (int i = 0; i < fs.length; i++) {
                if(fs[i].getTotalSpace()==0) {
                    continue;
                }
                Map<String,Object> map = new HashMap<>();
                map.put("name", fsv.getSystemDisplayName(fs[i]));
                map.put("max", fs[i].getTotalSpace());
                map.put("rest", fs[i].getFreeSpace());
                map.put("restPPT", fs[i].getFreeSpace()*100/fs[i].getTotalSpace());
                list.add(map);
                log.info(map.toString());
            }
            res.setResult(list);
            res.success("查询成功");
        } catch (Exception e) {
            res.error500("查询失败"+e.getMessage());
        }
        return res;
    }

    //-------------------------------------------------------------------------------------------
    /**
     * 工作台首页的数据
     * @return
     */
    @GetMapping(value = "/list/search/projects")
    public String projects() {
        return readJson(JSON_PATH + "/workplace_projects.json");
    }

    @GetMapping(value = "/workplace/activity")
    public String activity() {
        return readJson(JSON_PATH + "/workplace_activity.json");
    }

    @GetMapping(value = "/workplace/teams")
    public String teams() {
        return readJson(JSON_PATH + "/workplace_teams.json");
    }

    @GetMapping(value = "/workplace/radar")
    public String radar() {
        return readJson(JSON_PATH + "/workplace_radar.json");
    }

    @GetMapping(value = "/task/process")
    public String taskProcess() {
        return readJson(JSON_PATH + "/task_process.json");
    }
    //-------------------------------------------------------------------------------------------

    public String sysDataLogJson() {
        return readJson(JSON_PATH + "/sysdatalog.json");
    }
    
    /**
     * 读取json格式文件
     * @param jsonSrc
     * @return
     */
    private String readJson(String jsonSrc) {
        String json = "";
        try {
            //File jsonFile = ResourceUtils.getFile(jsonSrc);
            //json = FileUtils.re.readFileToString(jsonFile);
            //换个写法，解决springboot读取jar包中文件的问题
            InputStream stream = getClass().getClassLoader().getResourceAsStream(jsonSrc.replace("classpath:", ""));
            json = IOUtils.toString(stream,"UTF-8");
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return json;
    }
    
}
