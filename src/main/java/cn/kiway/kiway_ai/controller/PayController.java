package cn.kiway.kiway_ai.controller;

import cn.kiway.kiway_ai.entity.Pay;
import cn.kiway.kiway_ai.service.PayService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.kiway.kiway_ai.controller.BaseController;
import cn.kiway.kiway_ai.entity.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

/**
 * Created by ym on 2018-07-06.
 */
@Api(tags = "支付订单")
@RestController
@RequestMapping("pay")
public class PayController extends BaseController {

    @Autowired
    private PayService service;

    @SuppressWarnings("rawtypes")
    @ApiOperation("分页查询")
    @GetMapping
    public ResponseResult findPage(Pay entity, Page<Pay> page) {
	ResponseResult re = null;
	EntityWrapper wrapper = new EntityWrapper();
	@SuppressWarnings("unchecked")
	Page<Pay> pagination = service.selectPage(page, wrapper);
	Map<String, Object> retMap = new HashMap<String, Object>();
	retMap.put("list", pagination.getRecords());
	retMap.put("totalRecord", pagination.getTotal());
	retMap.put("totalPage", pagination.getPages());
	re = new ResponseResult<>(200, retMap);
	return re;
    }

    @SuppressWarnings("rawtypes")
    @ApiOperation("根据ID一个支付信息")
    @DeleteMapping("{id}")
    public ResponseResult deleteById(@PathVariable("id") String id) {
	service.deleteById(id);
	return ResponseResult.ok();
    }

    @SuppressWarnings("rawtypes")
    @ApiOperation("根据ID查询支付详细信息")
    @GetMapping("{id}")
    public ResponseResult getById(@PathVariable("id") String id) {
	Pay entity = service.selectById(id);
	return ResponseResult.ok(200, entity);
    }	

    @SuppressWarnings("rawtypes")
    @ApiOperation("修改某一个支付信息")
    @PutMapping()
    public ResponseResult update(@Valid Pay entity, BindingResult result, HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	service.updateById(entity);
	return ResponseResult.ok();
    }

    @SuppressWarnings("rawtypes")
    @ApiOperation("新增一个支付")
    @PostMapping
    public ResponseResult create(@Valid Pay entity, BindingResult result, HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	entity.setPayTime(new Timestamp(System.currentTimeMillis()));
	service.insert(entity);
	return ResponseResult.ok();
    }
}
