package cn.kiway.kiway_ai.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * Created by ym on Fri Jul 06 11:17:03 CST 2018.
 */
 @Data
public class PayModule implements Serializable{
    private String payPayment;
    private String payName;
    private String id;
    private String payId;
}
