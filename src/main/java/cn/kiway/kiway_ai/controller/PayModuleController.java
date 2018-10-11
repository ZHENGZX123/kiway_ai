package cn.kiway.kiway_ai.controller;

import cn.kiway.kiway_ai.entity.PayModule;
import cn.kiway.kiway_ai.service.PayModuleService;
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
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;


/**
 * Created by ym on 2018-07-06.
 */
@Api(tags="记录支付信息")
@RestController
@RequestMapping("payModule")
public class PayModuleController extends BaseController{

	private Class<?> clazz = PayModuleController.class;
	
	@Autowired
	private PayModuleService service;
	
	@ApiOperation("分页查询")
	@GetMapping
	public ResponseResult findPage(PayModule entity,Page<PayModule> page){
    	ResponseResult re = null;
        EntityWrapper wrapper = new EntityWrapper();
        Page<PayModule> pagination = service.selectPage(page,wrapper);

        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("list", pagination.getRecords());
        retMap.put("totalRecord", pagination.getTotal());
        retMap.put("totalPage", pagination.getPages());
        re = new ResponseResult<>(200, retMap);
    	return re;
    }
    
    @ApiOperation("根据ID某一个支付信息")
    @DeleteMapping("{id}")
    public ResponseResult deleteById(@PathVariable("id")String id){
    	service.deleteById(id);
    	return ResponseResult.ok();
    }
    
    @ApiOperation("根据ID查询一个支付详细信息")
    @GetMapping("{id}")
    public ResponseResult getById(@PathVariable("id")String id){
        PayModule entity = service.selectById(id);
    	return ResponseResult.ok(200,entity);
    }
    
    @ApiOperation("修改某一个支付")
    @PutMapping("{id}")
    public ResponseResult update(@PathVariable("id")String id,@Valid PayModule entity,BindingResult result,HttpServletRequest request) {
        ResponseResult re = null;
        re = super.hasError(result);
        if(re != null){
            return re;
        }
        entity.setId(id);	
        service.updateById(entity);	
        return ResponseResult.ok();	
    }
    	
    @ApiOperation("新增一个新的支付订单")
    @PostMapping
    public ResponseResult create(@Valid PayModule entity,BindingResult result,HttpServletRequest request) {
        ResponseResult re = null;
        re = super.hasError(result);
        if(re != null){
            return re;	
        }
        service.insert(entity);
    	return ResponseResult.ok();
    }    
}
