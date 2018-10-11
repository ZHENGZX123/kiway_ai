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
import cn.kiway.kiway_ai.entity.ResponseResult;
import cn.kiway.kiway_ai.entity.RobotModule;
import cn.kiway.kiway_ai.entity.RobotModuleList;
import cn.kiway.kiway_ai.service.DialogueService;
import cn.kiway.kiway_ai.service.ModuleService;
import cn.kiway.kiway_ai.service.RobotModuleService;
import cn.kiway.kiway_ai.service.RobotService;
import cn.kiway.kiway_ai.utils.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by ym on 2018-07-06.
 */
@Api(tags = "机器人对话模块")
@RestController
@RequestMapping("userModule")
public class RobotModuleController extends BaseController {

    private Class<?> clazz = RobotModuleController.class;

    @Autowired
    private RobotModuleService service;
    @Autowired
    private DialogueService dialogueService;
    @Autowired
    private RobotService robotService;
    @Autowired
    private ModuleService moduleService;

    @ApiOperation("查询某个机器人，所使用的模块")
    @GetMapping
    public ResponseResult findPage(RobotModule entity, Page<RobotModule> page) {
	ResponseResult re = null;
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("robot_id", entity.getRobotId());
	Page<RobotModule> pagination = service.selectPage(page, wrapper);
	ArrayList<RobotModule> list = (ArrayList<RobotModule>) pagination.getRecords();
	ArrayList<RobotModuleList> list1 = new ArrayList<RobotModuleList>();
	RobotModuleList item = null;
	Module module = null;
	for (RobotModule robotModule : list) {
	    item = new RobotModuleList();
	    item.setCreateTime(robotModule.getCreateTime());
	    item.setId(robotModule.getId());
	    item.setIsactive(robotModule.getIsactive());
	    item.setModuleId(robotModule.getModuleId());
	    item.setModuleName(robotModule.getModuleName());
	    item.setRobotId(robotModule.getRobotId());
	    item.setStaute(robotModule.getStaute());
	    module = moduleService.selectById(robotModule.getModuleId());
	    if (module != null) {
		if (module.getRobotId().equals(robotModule.getRobotId())) {
		    item.setIsMy(1);
		} else {
		    item.setIsMy(0);
		}
		item.setAbout(module.getAbout());
	    }	
	    list1.add(item);
	}
	Map<String, Object> retMap = new HashMap<String, Object>();
	retMap.put("list", list1);
	retMap.put("totalRecord", pagination.getTotal());
	retMap.put("totalPage", pagination.getPages());
	re = new ResponseResult<>(200, retMap);
	return re;
    }

    @ApiOperation("根据ID删除某个机器人配置的模块")
    @DeleteMapping("{id}")
    public ResponseResult deleteById(@PathVariable("id") String id) {
	service.deleteById(id);
	return ResponseResult.ok();
    }

    @ApiOperation("根据ID查询模块详细信息")
    @GetMapping("{id}")
    public ResponseResult getById(@PathVariable("id") String id) {
	RobotModule entity = service.selectById(id);
	return ResponseResult.ok(200, entity);
    }

    @ApiOperation("修改一个模块")
    @PutMapping
    public ResponseResult update(@Valid RobotModule entity, BindingResult result, HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	if (service.selectById(entity).getId() == null) {
	    return ResponseResult.ok(StatusCode._500, "id错误");
	}
	service.updateById(entity);
	return ResponseResult.ok();
    }

    @ApiOperation("新增机器人配置模块")
    @PostMapping
    public ResponseResult create(@Valid RobotModule entity, BindingResult result, HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	if (robotService.selectById(entity.getRobotId()) == null) {
	    return ResponseResult.ok(StatusCode._500, "机器人id错误");
	}
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("module_name", entity.getModuleName());
	wrapper.eq("robot_id", entity.getRobotId());
	List<RobotModule> list1 = service.selectList(wrapper);
	if (list1.size() > 0) {
	    return ResponseResult.ok(StatusCode._500, "已存在同名模块");
	}
	entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
	service.insert(entity);
	return ResponseResult.ok();
    }
}
