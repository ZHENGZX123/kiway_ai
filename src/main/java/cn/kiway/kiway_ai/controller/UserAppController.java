package cn.kiway.kiway_ai.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.kiway.kiway_ai.entity.App;
import cn.kiway.kiway_ai.entity.Language;
import cn.kiway.kiway_ai.entity.ResponseResult;
import cn.kiway.kiway_ai.entity.UserApp;
import cn.kiway.kiway_ai.service.AppService;
import cn.kiway.kiway_ai.service.LanguageService;
import cn.kiway.kiway_ai.service.RobotModuleService;
import cn.kiway.kiway_ai.service.RobotService;
import cn.kiway.kiway_ai.service.UserAppService;
import cn.kiway.kiway_ai.utils.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by ym on 2018-07-06.
 */
@Api(tags = "用户APP")
@RestController
@RequestMapping("userApp")
public class UserAppController extends BaseController {

    private Class<?> clazz = UserAppController.class;

    @Autowired
    private UserAppService service;
    @Autowired
    private AppService appservice;
    @Autowired
    private RobotService robotService;
    @Autowired
    private RobotModuleService userModuleService;
    @Autowired
    private LanguageService languageService;

    @ApiOperation("查询用户创建的APP")
    @GetMapping
    public ResponseResult findPage(@RequestParam(value = "userId") String userId, Page<UserApp> page) {
	ResponseResult re = null;
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("user_id", userId);
	Page<UserApp> pagination = service.selectPage(page, wrapper);
	Map<String, Object> retMap = new HashMap<String, Object>();
	retMap.put("list", pagination.getRecords());
	retMap.put("totalRecord", pagination.getTotal());
	retMap.put("totalPage", pagination.getPages());
	re = new ResponseResult<>(200, retMap);
	return re;
    }
     	
    @ApiOperation("根据ID删除某一个APP")
    @DeleteMapping("{id}")
    public ResponseResult deleteById(@PathVariable("id") String id) {

	UserApp entity = service.selectById(id);
	if (entity == null) {
	    return ResponseResult.ok(StatusCode._500, "APPID 不存在");
	}
	service.deleteById(id);
	return ResponseResult.ok(StatusCode._200, "删除成功");
    }

    @ApiOperation("根据ID查询某一个App详细信息")	
    @GetMapping("{id}")
    public ResponseResult getById(@PathVariable("id") String id) {
	UserApp entity = service.selectById(id);
	return ResponseResult.ok(200, entity);
    }

    @ApiOperation("修改一个APP")
    @PutMapping("{id}")
    public ResponseResult update(@PathVariable("id") String id, @Valid UserApp entity, BindingResult result,
	    HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	UserApp entity1 = service.selectById(id);
	if (entity1 == null) {
	    return ResponseResult.ok(StatusCode._500, "APPID 不存在");
	}
	entity.setId(id);
	service.updateById(entity);
	return ResponseResult.ok();
    }

    @ApiOperation("更新APPKEY")
    @PutMapping("updateAppkey/{id}")
    public ResponseResult updateAppKey(@PathVariable("id") String id) {

	UserApp entity1 = service.selectById(id);
	if (entity1 == null) {
	    return ResponseResult.ok(StatusCode._500, "APPID 不存在");
	}
	String appKey = UUID.randomUUID().toString().replaceAll("-", "") + new Random().nextLong();
	entity1.setAppKey(appKey);
	service.updateById(entity1);
	return new ResponseResult(200,appKey, "更新成功");
    }

    @ApiOperation("新增APP")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult create(@RequestParam(value = "appName") String appName,
	    @RequestParam(value = "userId") String userId, @RequestParam(value = "about") String about,
	    @RequestParam(value = "type") String type, HttpServletRequest request) {

	if (userId.equals("") || userId == null) {
	    return new ResponseResult(StatusCode._500, null, "用户id不能为空");
	}
	if (type.equals("") || type == null) {
	    return new ResponseResult(StatusCode._500, null, "类型不能为空");
	}
	if (appName.equals("") || appName == null) {
	    return new ResponseResult(StatusCode._500, null, "app名字不能为空");
	}
	UserApp entity = new UserApp();
	//String appKey = UUID.randomUUID().toString().replaceAll("-", "") + new Random().nextLong();
	String appKey = UUID.randomUUID().toString().replaceAll("-", "");
	entity.setAppKey(appKey);
	entity.setAppName(appName);
	entity.setUserId(userId);
	entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
	entity.setAbout(about);
	entity.setType(type);
	service.insert(entity);
	Language language = new Language();
	language.setAppId(entity.getId());
	languageService.insert(language);
	return new ResponseResult(StatusCode._200, entity, "创建成功");
    }

    @ApiOperation("获取App类型")
    @RequestMapping(value = "/apptype", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult getAppTypeList() {	
	Page<App> page = new Page<App>();
	page.setTotal(1);
	page.setTotal(10);
	Page<App> pagination = appservice.selectPage(page);
	if (pagination.getRecords().size() > 0) {
	    return ResponseResult.ok(StatusCode._200, pagination.getRecords());
	}
	return ResponseResult.ok(StatusCode._500, "获取类型列表失败");
    }
}
