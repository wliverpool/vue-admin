package wfm.example.back.controller.sys;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import wfm.example.back.model.SysDepartRole;
import wfm.example.back.model.SysDepartRolePermission;
import wfm.example.back.model.SysDepartRoleUser;
import wfm.example.back.model.SysPermissionDataRule;
import wfm.example.back.query.QueryGenerator;
import wfm.example.back.service.*;
import wfm.example.back.vo.JwtUser;
import wfm.example.common.util.ObjectConvertUtils;
import wfm.example.common.vo.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门角色controller
 * @author: 吴福明
 */
@Slf4j
@RestController
@RequestMapping("/sys/sysDepartRole")
public class SysDepartRoleController {

    @Autowired
    private ISysDepartRoleService sysDepartRoleService;

    @Autowired
    private ISysDepartRoleUserService departRoleUserService;

    @Autowired
    private ISysDepartPermissionService sysDepartPermissionService;

    @Autowired
    private ISysDepartRolePermissionService sysDepartRolePermissionService;

    @Autowired
    private ISysDepartService sysDepartService;

    /**
     * 分页列表查询
     *
     * @param sysDepartRole
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysDepartRole sysDepartRole,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   @RequestParam(name="deptId",required=false) String deptId,
                                   HttpServletRequest req,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        QueryWrapper<SysDepartRole> queryWrapper = QueryGenerator.initQueryWrapper(sysDepartRole, req.getParameterMap());
        Page<SysDepartRole> page = new Page<SysDepartRole>(pageNo, pageSize);
        JwtUser user = (JwtUser) userDetails;
        List<String> deptIds = null;
//		if(ObjectConvertUtils.isEmpty(deptId)){
//			if(ObjectConvertUtils.isNotEmpty(user.getUserIdentity()) && user.getUserIdentity().equals(CommonConstant.USER_IDENTITY_2) ){
//				deptIds = sysDepartService.getMySubDepIdsByDepId(user.getDepartIds());
//			}else{
//				return Result.ok(null);
//			}
//		}else{
//			deptIds = sysDepartService.getSubDepIdsByDepId(deptId);
//		}
//		queryWrapper.in("depart_id",deptIds);

        //我的部门，选中部门只能看当前部门下的角色
        queryWrapper.eq("depart_id",deptId);
        IPage<SysDepartRole> pageList = sysDepartRoleService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param sysDepartRole
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysDepartRole sysDepartRole) {
        sysDepartRoleService.save(sysDepartRole);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysDepartRole
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SysDepartRole sysDepartRole) {
        sysDepartRoleService.updateById(sysDepartRole);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        sysDepartRoleService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.sysDepartRoleService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        SysDepartRole sysDepartRole = sysDepartRoleService.getById(id);
        return Result.ok(sysDepartRole);
    }

    /**
     * 获取部门下角色
     * @param departId
     * @return
     */
    @RequestMapping(value = "/getDeptRoleList", method = RequestMethod.GET)
    public Result<List<SysDepartRole>> getDeptRoleList(@RequestParam(value = "departId") String departId,@RequestParam(value = "userId") String userId){
        Result<List<SysDepartRole>> result = new Result<>();
        //查询选中部门的角色
        List<SysDepartRole> deptRoleList = sysDepartRoleService.list(new LambdaQueryWrapper<SysDepartRole>().eq(SysDepartRole::getDepartId,departId));
        result.setSuccess(true);
        result.setResult(deptRoleList);
        return result;
    }

    /**
     * 设置
     * @param json
     * @return
     */
    @RequestMapping(value = "/deptRoleUserAdd", method = RequestMethod.POST)
    public Result<?> deptRoleAdd(@RequestBody JSONObject json) {
        String newRoleId = json.getString("newRoleId");
        String oldRoleId = json.getString("oldRoleId");
        String userId = json.getString("userId");
        departRoleUserService.deptRoleUserAdd(userId,newRoleId,oldRoleId);
        return Result.ok("添加成功！");
    }

    /**
     * 根据用户id获取已设置部门角色
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getDeptRoleByUserId", method = RequestMethod.GET)
    public Result<List<SysDepartRoleUser>> getDeptRoleByUserId(@RequestParam(value = "userId") String userId, @RequestParam(value = "departId") String departId){
        Result<List<SysDepartRoleUser>> result = new Result<>();
        //查询部门下角色
        List<SysDepartRole> roleList = sysDepartRoleService.list(new QueryWrapper<SysDepartRole>().eq("depart_id",departId));
        List<String> roleIds = roleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
        QueryWrapper queryWrapper = new QueryWrapper<SysDepartRoleUser>().eq("user_id",userId);
        if (CollectionUtils.isNotEmpty(roleIds)){
            queryWrapper.in("drole_id",roleIds);
        }
        //根据角色id,用户id查询已授权角色
        List<SysDepartRoleUser> roleUserList = departRoleUserService.list(queryWrapper);
        result.setSuccess(true);
        result.setResult(roleUserList);
        return result;
    }

    /**
     * 查询数据规则数据
     */
    @GetMapping(value = "/datarule/{permissionId}/{departId}/{roleId}")
    public Result<?> loadDatarule(@PathVariable("permissionId") String permissionId,@PathVariable("departId") String departId,@PathVariable("roleId") String roleId) {
        //查询已授权的部门规则
        List<SysPermissionDataRule> list = sysDepartPermissionService.getPermRuleListByDeptIdAndPermId(departId,permissionId);
        if(list==null || list.size()==0) {
            return Result.error("未找到权限配置信息");
        }else {
            Map<String,Object> map = new HashMap<>();
            map.put("datarule", list);
            LambdaQueryWrapper<SysDepartRolePermission> query = new LambdaQueryWrapper<SysDepartRolePermission>()
                    .eq(SysDepartRolePermission::getPermissionId, permissionId)
                    .eq(SysDepartRolePermission::getRoleId,roleId);
            SysDepartRolePermission sysRolePermission = sysDepartRolePermissionService.getOne(query);
            if(sysRolePermission==null) {
                //return Result.error("未找到角色菜单配置信息");
            }else {
                String drChecked = sysRolePermission.getDataRuleIds();
                if(ObjectConvertUtils.isNotEmpty(drChecked)) {
                    map.put("drChecked", drChecked.endsWith(",")?drChecked.substring(0, drChecked.length()-1):drChecked);
                }
            }
            return Result.ok(map);
            //TODO 以后按钮权限的查询也走这个请求 无非在map中多加两个key
        }
    }

    /**
     * 保存数据规则至角色菜单关联表
     */
    @PostMapping(value = "/datarule")
    public Result<?> saveDatarule(@RequestBody JSONObject jsonObject) {
        try {
            String permissionId = jsonObject.getString("permissionId");
            String roleId = jsonObject.getString("roleId");
            String dataRuleIds = jsonObject.getString("dataRuleIds");
            log.info("保存数据规则>>"+"菜单ID:"+permissionId+"角色ID:"+ roleId+"数据权限ID:"+dataRuleIds);
            LambdaQueryWrapper<SysDepartRolePermission> query = new LambdaQueryWrapper<SysDepartRolePermission>()
                    .eq(SysDepartRolePermission::getPermissionId, permissionId)
                    .eq(SysDepartRolePermission::getRoleId,roleId);
            SysDepartRolePermission sysRolePermission = sysDepartRolePermissionService.getOne(query);
            if(sysRolePermission==null) {
                return Result.error("请先保存角色菜单权限!");
            }else {
                sysRolePermission.setDataRuleIds(dataRuleIds);
                this.sysDepartRolePermissionService.updateById(sysRolePermission);
            }
        } catch (Exception e) {
            log.error("SysRoleController.saveDatarule()发生异常：" + e.getMessage(),e);
            return Result.error("保存失败");
        }
        return Result.ok("保存成功!");
    }

}
