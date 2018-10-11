//package cn.kiway.kiway_ai.controller;
//
//import cn.kiway.kiway_ai.entity.App;
//import cn.kiway.kiway_ai.service.AppService;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import cn.kiway.kiway_ai.controller.BaseController;
//import cn.kiway.kiway_ai.entity.ResponseResult;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import java.util.HashMap;
//import java.util.Map;
//import javax.validation.Valid;
//import javax.servlet.http.HttpServletRequest;
//import com.baomidou.mybatisplus.plugins.Page;
//import com.baomidou.mybatisplus.mapper.EntityWrapper;
//
//
///**
// * Created by ym on 2018-07-06.
// */
//@Api(tags="App信息")
//@RestController
//@RequestMapping("app")
//public class AppController extends BaseController{
//
//	private Class<?> clazz = AppController.class;
//	
//	@Autowired
//	private AppService service;
//	
//	@ApiOperation("分页查询APP应用")
//	@GetMapping
//	public ResponseResult findPage(App entity,Page<App> page){
//    	ResponseResult re = null;
//        EntityWrapper wrapper = new EntityWrapper();
//        Page<App> pagination = service.selectPage(page,wrapper);
//
//        Map<String, Object> retMap = new HashMap<String, Object>();
//        retMap.put("list", pagination.getRecords());
//        retMap.put("totalRecord", pagination.getTotal());
//        retMap.put("totalPage", pagination.getPages());
//        re = new ResponseResult<>(200, retMap);
//    	return re;
//    }
//    
//    @ApiOperation("根据ID删除APP")
//    @DeleteMapping("{id}")
//    public ResponseResult deleteById(@PathVariable("id")String id){
//    	service.deleteById(id);
//    	return ResponseResult.ok();
//    }
//    
//    @ApiOperation("根据ID查询APP信息")
//    @GetMapping("{id}")
//    public ResponseResult getById(@PathVariable("id")String id){
//        App entity = service.selectById(id);
//    	return ResponseResult.ok(200,entity);
//    }
//    
//    @ApiOperation("根据ID更新应用信息")
//    @PutMapping("{id}")
//    public ResponseResult update(@PathVariable("id")Long id,@Valid App entity,BindingResult result,HttpServletRequest request) {
//        ResponseResult re = null;
//        re = super.hasError(result);
//        if(re != null){
//            return re;
//        }
//        entity.setId(id);
//        service.updateById(entity);
//        return ResponseResult.ok();
//    }  
//    
//    @ApiOperation("添加应用")
//    @PostMapping
//    public ResponseResult create(@Valid App entity,BindingResult result,HttpServletRequest request) {
//        ResponseResult re = null;	
//        re = super.hasError(result);
//        if(re != null){
//            return re;
//        }
//        service.insert(entity);
//    	return ResponseResult.ok();
//    }    
//}
