package cn.kiway.kiway_ai.entity;

import lombok.Data;

@Data
public class ResponseMoudelResult {

    private String mouduleId;
    private String moduleName;
    private java.sql.Timestamp createTime;
    private String isPublic;// 1私人 2 应用内共享 3租户共享
    private User user;
}
