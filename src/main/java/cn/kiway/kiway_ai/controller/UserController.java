package cn.kiway.kiway_ai.controller;

import java.sql.Timestamp;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.kiway.kiway_ai.entity.ResponseResult;
import cn.kiway.kiway_ai.entity.User;
import cn.kiway.kiway_ai.service.UserService;
import cn.kiway.kiway_ai.utils.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by ym on 2018-07-06.
 */
@Api(tags = "User")
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    private Class<?> clazz = UserController.class;

    @Autowired
    private UserService service;

    @ApiOperation("分页查询")
    @GetMapping
    public ResponseResult findPage(User entity, Page<User> page) {
	ResponseResult re = null;
	EntityWrapper wrapper = new EntityWrapper();
	Page<User> pagination = service.selectPage(page, wrapper);

	Map<String, Object> retMap = new HashMap<String, Object>();
	retMap.put("list", pagination.getRecords());
	retMap.put("totalRecord", pagination.getTotal());
	retMap.put("totalPage", pagination.getPages());
	re = new ResponseResult<>(200, retMap);
	return re;
    }

    @ApiOperation("根据ID删除纪录")
    @DeleteMapping("{id}")
    public ResponseResult deleteById(@PathVariable("id") String id) {
	service.deleteById(id);
	return ResponseResult.ok();
    }

    @ApiOperation("根据ID查询详细信息")
    @GetMapping("{id}")
    public ResponseResult getById(@PathVariable("id") String id) {
	User entity = service.selectById(id);
	return ResponseResult.ok(200, entity);
    }

    @ApiOperation("修改用戶信息")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseResult update(@Valid User entity, HttpServletRequest request) {
	service.updateById(entity);
	return ResponseResult.ok();
    }

    @ApiOperation("注册")
    @PostMapping
    public ResponseResult create(@Valid User entity, BindingResult result, HttpServletRequest request) {

	if (entity.getUsername() == null || entity.getUsername().equals("")) {
	    return ResponseResult.error(StatusCode._500, "用户名不能为空");
	}
	if (entity.getPassword() == null || entity.getPassword().equals("")) {
	    return ResponseResult.error(StatusCode._500, "密码不能为空");
	}
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("username", entity.getUsername()); // select * from user
						      // where username = ?
	List<User> list = service.selectList(wrapper);
	if (list.size() > 0) {
	    return ResponseResult.error(StatusCode._500, "用户已存在");
	}
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	entity.setCreatTime(new Timestamp(System.currentTimeMillis()));
	service.insert(entity);
	return new ResponseResult(StatusCode._200, entity, "注册成功");
    }

    @ApiOperation("登录")
    @PostMapping("login")
    public ResponseResult login(@Valid User entity, BindingResult result, HttpServletRequest request) {

	if (entity.getUsername() == null || entity.getUsername().equals("")) {
	    return ResponseResult.error(StatusCode._500, "用户名不能为空");
	}
	if (entity.getPassword() == null || entity.getPassword().equals("")) {
	    return ResponseResult.error(StatusCode._500, "密码不能为空");
	}
	
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("username", entity.getUsername()); // select * from user
						      // where phone = ?
	List<User> list = service.selectList(wrapper);
	if (list.size() > 0) {
	    User user = list.get(0);
	    if (user.getPassword().equals(entity.getPassword()))
		return ResponseResult.ok(StatusCode._200, user);
	    else
		return ResponseResult.error(StatusCode._500, "用户名或者密码错误");
	} else {
	    return ResponseResult.error(StatusCode._500, "用户名或者密码错误");
	}
    }
}
