package cn.kiway.kiway_ai.controller;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import cn.kiway.kiway_ai.entity.ResponseResult;

/**
 * Created by ym on Fri Jul 06 11:17:03 CST 2018.
 */
public class BaseController {

    /**
     * 获取session中的参数
     * @param request
     * @param name
     * @return
     */
    public Object getSessionParam(HttpServletRequest request,String name){
        return request.getSession().getAttribute(name);
    }

    /**
     * 获取session中的参数
     * @param request
     * @param name
     * @return
     */
    public String getSessionParamStr(HttpServletRequest request,String name){
        return request.getSession().getAttribute(name).toString();
    }

    /***
     * @description 判断是否有错误
     * @author yimin
     * @date 2017年7月24日
     * @param result
     * @return
     */
    public ResponseResult hasError(BindingResult result) {
        ResponseResult re = null;
        if (result.hasErrors()) {
            StringBuilder errMsg = new StringBuilder();
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error : allErrors) {
                String msg = error.getDefaultMessage();
                errMsg.append(msg).append(";");
            }
            re = new ResponseResult(400);
            re.setErrorMsg(errMsg.toString());
            return re;
        }
        return re;
    }

    /***
         * @description 转换前端传的timstamp数据类型
         * @author yimin
         * @date 2017年8月21日
         * @param binder
         */
        @InitBinder
    protected void initBinder(WebDataBinder binder) {
            binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
                @Override
                public void setAsText(String value) {
                    setValue(new Timestamp(Long.valueOf(value)));
                }
            });
        }

        /***
         * 验证List参数
         * @param voList
         * @return
         */
        public ResponseResult paramsError(List voList){
            List<Map<String,String>> errorList = new ArrayList<Map<String,String>>();
            voList.forEach(item ->{
                ValidationResult validationResult = ValidationUtils.validateEntity(item);
                boolean flag = validationResult.isHasErrors();
                if(flag){
                    errorList.add(validationResult.getErrorMsg());
                }
            });
            if(errorList != null && errorList.size() > 0){
                return new ResponseResult(400,errorList);
            }else{
                return null;
            }
        }
}
class ValidationUtils {
    private static Validator validator =  Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ValidationResult validateEntity(T obj){
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validate(obj,Default.class);
        if( !CollectionUtils.isEmpty(set)) {
            result.setHasErrors(true);
            Map<String,String> errorMsg = new HashMap<String,String>();
            for(ConstraintViolation<T> cv : set){
                errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }
}

class ValidationResult {

    private boolean hasErrors;

    private Map<String,String> errorMsg;

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Map<String, String> errorMsg) {
        this.errorMsg = errorMsg;
    }
}
