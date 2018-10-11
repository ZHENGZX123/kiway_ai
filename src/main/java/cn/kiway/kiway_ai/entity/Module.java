package cn.kiway.kiway_ai.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ym on Fri Jul 06 11:17:03 CST 2018.
 */
@Data
public class Module implements Serializable {
    private String id;
    private String moduleName;
    private java.sql.Timestamp createTime;
    private String isPublic;// 1私人 2 应用内共享 3租户共享
    private String userId;
    private String robotId;
    private String about;
}
