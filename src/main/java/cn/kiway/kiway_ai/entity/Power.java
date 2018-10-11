package cn.kiway.kiway_ai.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Power implements Serializable{
    private String id;
    private String userId;
    private String powerId;
    private String powerActive;
}
