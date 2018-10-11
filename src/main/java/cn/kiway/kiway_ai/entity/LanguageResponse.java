package cn.kiway.kiway_ai.entity;

import java.util.ArrayList;

import lombok.Data;

@Data
public class LanguageResponse {
    private String id;
    private String appId;
    private ArrayList<LanguageRs> languages;

    @Data
   public static class LanguageRs {
	String name;
	int status;
    }
}
