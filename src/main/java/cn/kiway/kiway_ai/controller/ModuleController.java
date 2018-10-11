package cn.kiway.kiway_ai.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.kiway.kiway_ai.entity.Module;
import cn.kiway.kiway_ai.entity.ModuleRes;
import cn.kiway.kiway_ai.entity.ResponseMoudelResult;
import cn.kiway.kiway_ai.entity.ResponseResult;
import cn.kiway.kiway_ai.entity.Robot;
import cn.kiway.kiway_ai.entity.RobotModule;
import cn.kiway.kiway_ai.service.DialogueService;
import cn.kiway.kiway_ai.service.ModuleService;
import cn.kiway.kiway_ai.service.RobotModuleService;
import cn.kiway.kiway_ai.service.RobotService;
import cn.kiway.kiway_ai.service.UserAppService;
import cn.kiway.kiway_ai.service.UserService;
import cn.kiway.kiway_ai.utils.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by ym on 2018-07-06.
 */
@Api(tags = "对话模块")
@RestController
@RequestMapping("module")
public class ModuleController extends BaseController {

    private Class<?> clazz = ModuleController.class;

    @Autowired
    private ModuleService service;
    @Autowired
    private RobotModuleService robotModuleService;
    @Autowired
    private UserService userService;
    @Autowired
    private RobotService robotService;
    @Autowired
    private UserAppService userAppService;
    @Autowired
    private DialogueService dialogueService;

    @ApiOperation("查询个人创建的模块")
    @GetMapping
    public ResponseResult findPage(Module entity, Page<Module> page) {

	ResponseResult re = null;
	EntityWrapper wrapper = new EntityWrapper();
	if (entity.getUserId() != null && !entity.getUserId().equals("")) {
	    wrapper.eq("user_id", entity.getUserId());
	}
	if (entity.getIsPublic() != null) {
	    wrapper.eq("is_public", entity.getIsPublic());
	}
	if (entity.getUserId() != null && !entity.getUserId().equals("")) {
	    wrapper.eq("user_id", entity.getUserId());
	}
	if (entity.getRobotId() != null && !entity.getRobotId().equals("")) {
	    wrapper.eq("robot_id", entity.getRobotId());
	}
	Page<Module> pagination = service.selectPage(page, wrapper);
	List<Module> moudules = pagination.getRecords();
	List<ModuleRes> reModules = new ArrayList<>();
	for (Module module : moudules) {
	    ModuleRes moduleRes = new ModuleRes();
	    moduleRes.setUser(userService.selectById(module.getUserId()));
	    moduleRes.setModule(module);
	    Robot robot = robotService.selectById(module.getRobotId());
	    moduleRes.setRobot(robot);
	    moduleRes.setApp(userAppService.selectById(robot.getAppId()));
	    reModules.add(moduleRes);
	}
	Map<String, Object> retMap = new HashMap<String, Object>();
	retMap.put("list", reModules);
	retMap.put("totalRecord", pagination.getTotal());
	retMap.put("totalPage", pagination.getPages());
	re = new ResponseResult<>(200, retMap);
	return re;
    }

    @ApiOperation("查询共享的模块")
    @GetMapping("getCommentMoulde")
    public ResponseResult getCommentMoudle(Page<Module> page) {
	ResponseResult re = null;
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("is_public", 3);
	Page<Module> pagination = service.selectPage(page, wrapper);
	Map<String, Object> retMap = new HashMap<String, Object>();
	List<Module> moudules = pagination.getRecords();
	List<ResponseMoudelResult> reModules = new ArrayList<>();
	for (Module module : moudules) {
	    ResponseMoudelResult rMoudel = new ResponseMoudelResult();
	    rMoudel.setCreateTime(module.getCreateTime());
	    rMoudel.setIsPublic(module.getIsPublic());
	    rMoudel.setModuleName(module.getModuleName());
	    rMoudel.setMouduleId(module.getId());
	    rMoudel.setUser(userService.selectById(module.getUserId()));
	    reModules.add(rMoudel);
	}
	retMap.put("list", reModules);
	retMap.put("totalRecord", pagination.getTotal());
	retMap.put("totalPage", pagination.getPages());
	re = new ResponseResult<>(200, retMap);
	return re;
    }

    @ApiOperation("根据ID删除某一个模块")
    @DeleteMapping("{id}")
    public ResponseResult deleteById(@PathVariable("id") String id) {
	service.deleteById(id);
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("module_id", id);
	dialogueService.delete(wrapper);
	robotModuleService.delete(wrapper);
	return ResponseResult.ok();
    }

    @ApiOperation("根据ID查询某一个模块详细信息")
    @GetMapping("{id}")
    public ResponseResult getById(@PathVariable("id") String id) {
	Module entity = service.selectById(id);
	return ResponseResult.ok(200, entity);
    }

    @ApiOperation("根据ID修改某个模块的信息")
    @PutMapping()
    public ResponseResult update(@Valid Module entity, BindingResult result, HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	if (entity.getId() == null || entity.getId().equals("")) {
	    return ResponseResult.ok(StatusCode._500, "id不能为空");
	}
	if (service.selectById(entity.getId()) == null) {
	    return ResponseResult.ok(StatusCode._500, "id错误");
	}
	service.updateById(entity);
	if (entity.getModuleName() != null && !entity.getId().equals("")) {
	    RobotModule robotModule = new RobotModule();
	    robotModule.setModuleName(entity.getModuleName());
	    EntityWrapper wrapper = new EntityWrapper();
	    wrapper.eq("module_id", entity.getId());
	    robotModuleService.update(robotModule, wrapper);
	}
	return ResponseResult.ok();
    }

    @ApiOperation("设置模块是否公用")
    @PutMapping("/updatePublic")
    public ResponseResult update(String moduleId, String isPublic) {
	if (moduleId == null || moduleId.equals("")) {
	    return ResponseResult.ok(StatusCode._500, "id不能为空");
	}
	if (service.selectById(moduleId) == null) {
	    return ResponseResult.ok(StatusCode._500, "id错误");
	}
	String stautes = isPublic;
	Module module = service.selectById(moduleId);
	module.setIsPublic(stautes);
	service.updateById(module);
	if (stautes.equals("2") || stautes.equals("3")) {
	    EntityWrapper wrapper = new EntityWrapper();
	    wrapper.eq("user_id", module.getUserId());
	    List<Robot> robots = robotService.selectList(wrapper);
	    for (Robot robot : robots) {
		EntityWrapper wrapper1 = new EntityWrapper();
		wrapper1.eq("robot_id", robot.getId());
		wrapper1.eq("module_id", moduleId);
		if (robotModuleService.selectList(wrapper1).size() > 0) {
		    RobotModule robotModule = new RobotModule();
		    robotModule.setStaute(stautes);
		    robotModule.setIsactive("1");
		    robotModuleService.update(robotModule, wrapper1);
		} else {
		    RobotModule robotModule = new RobotModule();
		    robotModule.setStaute(stautes);
		    robotModule.setCreateTime(new Timestamp(System.currentTimeMillis()));
		    robotModule.setModuleId(moduleId);
		    robotModule.setIsactive("1");
		    robotModule.setModuleName(module.getModuleName());
		    robotModule.setRobotId(robot.getId());
		    robotModuleService.insert(robotModule);
		}
	    }
	} else {
	    EntityWrapper wrapper1 = new EntityWrapper();
	    RobotModule robotModule = new RobotModule();
	    robotModule.setStaute(stautes);
	    robotModule.setIsactive("1");
	    wrapper1.eq("module_id", module.getId());
	    if (robotModuleService.selectList(wrapper1).size() > 0) {
		robotModuleService.update(robotModule, wrapper1);
	    }
	    wrapper1.eq("robot_id", module.getRobotId());
	    robotModule.setIsactive("1");
	    if (robotModuleService.selectList(wrapper1).size() > 0) {
		robotModuleService.update(robotModule, wrapper1);
	    }
	}
	return new ResponseResult(200, module, null);
    }

    @ApiOperation("新增一个新的模块")
    @PostMapping("create")
    public ResponseResult create(@Valid Module entity, BindingResult result, HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	if (robotService.selectById(entity.getRobotId()) == null) {
	    return new ResponseResult(500, null, "机器人id错误");
	}
	if (entity.getRobotId() == null) {
	    return new ResponseResult(500, null, "机器人id不能为空");
	}
	if (entity.getUserId() == null) {
	    return new ResponseResult(500, null, "用户id不能为空");
	}
	if (entity.getModuleName() == null) {
	    return new ResponseResult(500, null, "模块名字不能为空");
	}
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("module_name", entity.getModuleName());
	wrapper.eq("user_id", entity.getUserId());
	if (service.selectList(wrapper).size() > 0) {
	    return new ResponseResult(500, null, "已存在同名模块");
	}
	Timestamp times = new Timestamp(System.currentTimeMillis());
	entity.setCreateTime(times);
	entity.setIsPublic("1");
	service.insert(entity);

	RobotModule robotModule = new RobotModule();
	robotModule.setCreateTime(new Timestamp(System.currentTimeMillis()));
	robotModule.setIsactive("1");
	robotModule.setModuleId(entity.getId());
	robotModule.setModuleName(entity.getModuleName());
	robotModule.setRobotId(entity.getRobotId());
	robotModuleService.insert(robotModule);
	return new ResponseResult(200, entity, "创建成功");
    }
}
