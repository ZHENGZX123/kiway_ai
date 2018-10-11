package cn.kiway.kiway_ai.entity;

import lombok.Data;

@Data
public class Robot {
    private String robotName;
    private java.sql.Timestamp createTime;
    private String robotAbout;
    private String id;
    private String userId;
    private String appId;
}
