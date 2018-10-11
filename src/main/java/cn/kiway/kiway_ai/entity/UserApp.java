package cn.kiway.kiway_ai.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ym on Fri Jul 06 11:17:03 CST 2018.
 */
@Data
public class UserApp implements Serializable {

    private String id;
    private String userId;
    private String appName;
    private String appKey;
    private java.sql.Timestamp createTime;
    private String about;
    private String type;
}
