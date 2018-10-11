package cn.kiway.kiway_ai.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.kiway.kiway_ai.entity.Power;
import cn.kiway.kiway_ai.entity.ResponseResult;
import cn.kiway.kiway_ai.service.PowerInfoService;
import cn.kiway.kiway_ai.service.PowerService;
import cn.kiway.kiway_ai.utils.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "权限管理")
@RestController
@RequestMapping("power")
public class PowerController extends BaseController {
    private Class<?> clazz = DialogueController.class;
    @Autowired
    PowerService service;
    @Autowired
    PowerInfoService powerInfoService;

    @ApiOperation("新增一个用戶的权限信息")
    @PostMapping
    public ResponseResult create(@Valid Power entity, BindingResult result, HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	if (powerInfoService.selectById(entity.getPowerId()) == null) {
	    return ResponseResult.ok(StatusCode._500, "权限id错误");
	}
	service.insert(entity);
	return ResponseResult.ok();
    }

    @ApiOperation("修改某一个权限信息")
    @PutMapping("{id}")
    public ResponseResult update(@PathVariable("id") String id, @Valid Power entity, BindingResult result,
	    HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	entity.setId(id);
	service.updateById(entity);
	return ResponseResult.ok();
    }

    @ApiOperation("获取是否有该权限")
    @RequestMapping(value = "/getPower", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult getDialogue(@RequestParam(value = "powerId") String powerId,
	    @RequestParam(value = "userId") String userId) {
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("user_id", userId);
	wrapper.eq("power_id", powerId);
	List<Power> userPowerList = service.selectList(wrapper);
	if (userPowerList.size() > 0) {
	    return ResponseResult.ok(StatusCode._200, userPowerList.get(0));
	}
	return ResponseResult.ok(StatusCode._500, "查询没有该权限的状态");
    }
}
