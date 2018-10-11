package cn.kiway.kiway_ai.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * Created by ym on Fri Jul 06 11:17:03 CST 2018.
 */
 @Data
public class Pay implements Serializable{
    private String createTime;
    private String userId;
    private String id;
    private String payId;
    private java.sql.Timestamp payTime;
}
