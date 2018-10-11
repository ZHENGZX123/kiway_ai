package cn.kiway.kiway_ai.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class RobotModuleList implements Serializable{
    private String id;
    private String moduleId;
    private String moduleName;
    private java.sql.Timestamp createTime;
    private String robotId;
    private String isactive;
    private String staute;
    private int isMy;
    private String about;
}
