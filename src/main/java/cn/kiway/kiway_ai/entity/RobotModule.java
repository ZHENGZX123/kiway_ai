package cn.kiway.kiway_ai.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ym on Fri Jul 06 11:17:03 CST 2018.
 */
@Data
public class RobotModule implements Serializable {
    private String id;
    private String moduleId;
    private String moduleName;
    private java.sql.Timestamp createTime;
    private String robotId;
    private String isactive;
    private String staute;
}
