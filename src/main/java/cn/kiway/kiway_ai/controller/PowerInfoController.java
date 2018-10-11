package cn.kiway.kiway_ai.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.kiway.kiway_ai.entity.Powerinfo;
import cn.kiway.kiway_ai.entity.ResponseResult;
import cn.kiway.kiway_ai.service.PowerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "权限信息")
@RestController
@RequestMapping("powerInfo")
public class PowerInfoController extends BaseController {
    private Class<?> clazz = DialogueController.class;
    @Autowired
    PowerInfoService service;

    @ApiOperation("分页查询全部")
    @GetMapping
    public ResponseResult findPage(Powerinfo entity, Page<Powerinfo> page) {
	ResponseResult re = null;
	EntityWrapper wrapper = new EntityWrapper();
	Page<Powerinfo> pagination = service.selectPage(page, wrapper);
	Map<String, Object> retMap = new HashMap<String, Object>();
	retMap.put("list", pagination.getRecords());
	retMap.put("totalRecord", pagination.getTotal());
	retMap.put("totalPage", pagination.getPages());
	re = new ResponseResult<>(200, retMap);
	return re;
    }

    @ApiOperation("根据ID删除权限")
    @DeleteMapping("{id}")
    public ResponseResult deleteById(@PathVariable("id") String id) {
	service.deleteById(id);
	return ResponseResult.ok();
    }

    @ApiOperation("新增一个权限")
    @PostMapping
    public ResponseResult create(@Valid Powerinfo entity, BindingResult result, HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	service.insert(entity);
	return ResponseResult.ok();
    }
}
