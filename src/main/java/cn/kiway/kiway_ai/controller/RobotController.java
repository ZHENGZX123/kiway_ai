package cn.kiway.kiway_ai.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.kiway.kiway_ai.entity.Dialogue;
import cn.kiway.kiway_ai.entity.ResponseResult;
import cn.kiway.kiway_ai.entity.Robot;
import cn.kiway.kiway_ai.entity.RobotAndModule;
import cn.kiway.kiway_ai.entity.RobotModule;
import cn.kiway.kiway_ai.service.DialogueService;
import cn.kiway.kiway_ai.service.RobotModuleService;
import cn.kiway.kiway_ai.service.RobotService;
import cn.kiway.kiway_ai.utils.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "机器人")
@RestController
@RequestMapping("robot")
public class RobotController extends BaseController {

    private Class<?> clazz = ModuleController.class;

    @Autowired
    private RobotService service;
    @Autowired
    private RobotModuleService userModuleService;
    @Autowired
    private DialogueService dialogueService;
    @Autowired
    private RobotModuleService robotModuleService;

    @ApiOperation("新增一个新的机器人")
    @PostMapping
    public ResponseResult create(@Valid Robot entity, BindingResult result, HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	if (entity.getAppId() == null) {
	    return new ResponseResult(StatusCode._500, null, "AppId 不能为空");
	}
	if (entity.getUserId() == null) {
	    return new ResponseResult(StatusCode._500, null, "用户Id 不能为空");
	}
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("app_id", entity.getAppId());
	wrapper.eq("robot_name", entity.getRobotName());
	List<Robot> list = service.selectList(wrapper);
	if (list.size() > 0) {
	    return ResponseResult.ok(StatusCode._500, "已存在同名机器人名字");
	}
	entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
	service.insert(entity);
	return new ResponseResult(StatusCode._200, entity, "创建成功");
    }

    @ApiOperation("获取用户机器人")
    @GetMapping
    public ResponseResult select(@RequestParam(value = "appId") String appId) {
	if (appId.equals("")) {
	    return new ResponseResult(StatusCode._500, null, "AppId 不能为空");
	}
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("app_id", appId);
	List<Robot> robots = service.selectList(wrapper);
	return ResponseResult.ok(StatusCode._200, robots);
    }

    @ApiOperation("获取用户机器人包括模块信息")
    @GetMapping("/getRobotAndModule")
    public ResponseResult selectRobotAndModule(@RequestParam(value = "appId") String appId) {
	if (appId.equals("")) {
	    return new ResponseResult(StatusCode._500, null, "AppId 不能为空");
	}
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("app_id", appId);
	List<Robot> robots = service.selectList(wrapper);
	List<RobotAndModule> robotAndModules = new ArrayList<RobotAndModule>();
	RobotAndModule robotAndModule = null;
	EntityWrapper wrapper1 = null;
	for (Robot robot : robots) {
	    robotAndModule=new RobotAndModule();
	    wrapper1=new EntityWrapper();
	    wrapper1.eq("robot_id", robot.getId());
	    robotAndModule.setRobot(robot);
	    robotAndModule.setRobotModules(robotModuleService.selectList(wrapper1));
	    robotAndModules.add(robotAndModule);
	}
	return ResponseResult.ok(StatusCode._200, robotAndModules);
    }

    @ApiOperation("修改机器人信息")
    @PutMapping
    public ResponseResult update(@Valid Robot entity, BindingResult result, HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	entity.setId(entity.getId());
	service.updateById(entity);
	return ResponseResult.ok();
    }

    @ApiOperation("根据ID删除某一个机器人")
    @DeleteMapping("{id}")
    public ResponseResult deleteById(@PathVariable("id") String id) {
	EntityWrapper<Robot> w = new EntityWrapper<Robot>();
	if (service.selectList(w).size() < 0) {
	    return new ResponseResult(StatusCode._500, null, "机器人id错误");
	}
	service.deleteById(id);
	EntityWrapper<RobotModule> wrapper = new EntityWrapper<RobotModule>();
	wrapper.eq("robot_id", id);
	List<RobotModule> userModules = userModuleService.selectList(wrapper);
	for (RobotModule userModule : userModules) {
	    EntityWrapper<Dialogue> wrapper1 = new EntityWrapper<Dialogue>();
	    wrapper.eq("module_id", userModule.getModuleId());
	    dialogueService.delete(wrapper1);
	}
	userModuleService.delete(wrapper);
	return ResponseResult.ok();
    }
}
