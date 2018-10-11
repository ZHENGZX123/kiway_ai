package cn.kiway.kiway_ai.entity;

import lombok.Data;

/**
 * Created by ym on Fri Jul 06 11:17:03 CST 2018.
 */
 @Data
public class ResponseResult<T> {
    private int statusCode = 200;
    private T data;
    private String errorMsg;

    public ResponseResult(T data) {
        this.data = data;
    }

    public ResponseResult(int statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }
    public ResponseResult(int statusCode, T data,String errorMsg) {
        this.statusCode = statusCode;
        this.data = data;
        this.errorMsg=errorMsg;
    }
    public ResponseResult(int statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseResult(int statusCode, Throwable error) {
        this.statusCode = statusCode;
        this.errorMsg = error.getMessage();
    }

    //================Static Method============================//

    public static<T> ResponseResult<T> ok(int statusCode, T data){
        return new ResponseResult<T>(statusCode, data);
    }

    public static<T> ResponseResult<T> ok(){
        return new ResponseResult<T>(200);
    }

    public static<T> ResponseResult<T> error(int statusCode, String errorMsg){
        ResponseResult<T> result = new ResponseResult<>(statusCode);
        result.setErrorMsg(errorMsg);
        return result;
    }
    public static<T> ResponseResult<T> error(int statusCode, Throwable throwable){
        ResponseResult<T> result = new ResponseResult<>(statusCode, throwable);
        return result;
    }

}
