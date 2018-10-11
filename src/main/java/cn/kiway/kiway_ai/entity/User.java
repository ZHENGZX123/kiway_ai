package cn.kiway.kiway_ai.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ym on Fri Jul 06 11:17:03 CST 2018.
 */
@Data
public class User implements Serializable {
    private String id;
    private String username;
    private String password;
    private String nickname;
    private String company;
    private String avater;
    private int sex;
    private String email;
    private String phone;
    private String type;
    private java.sql.Timestamp creatTime;
}
