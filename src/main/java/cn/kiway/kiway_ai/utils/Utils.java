package cn.kiway.kiway_ai.utils;

import java.util.ArrayList;

import cn.kiway.kiway_ai.entity.Language;
import cn.kiway.kiway_ai.entity.LanguageResponse;

public class Utils {
    public static ArrayList LanguageList(Language language) {
	ArrayList<LanguageResponse.LanguageRs> languages = new ArrayList<LanguageResponse.LanguageRs>();
	LanguageResponse.LanguageRs lrs = new LanguageResponse.LanguageRs();
	lrs.setName("中文分词");
	lrs.setStatus(language.getZwfc());
	languages.add(lrs);
	
	LanguageResponse.LanguageRs lrs1 = new LanguageResponse.LanguageRs();
	lrs1.setName("词性标注");
	lrs1.setStatus(language.getCxbz());
	languages.add(lrs1);
	
	LanguageResponse.LanguageRs lrs2 = new LanguageResponse.LanguageRs();
	lrs2.setName("命名实体识别");
	lrs2.setStatus(language.getMmstsb());
	languages.add(lrs2);
	
	LanguageResponse.LanguageRs lrs3 = new LanguageResponse.LanguageRs();
	lrs3.setName("关键词提取");
	lrs3.setStatus(language.getGjctq());
	languages.add(lrs3);
	
	LanguageResponse.LanguageRs lrs4 = new LanguageResponse.LanguageRs();
	lrs4.setName("自动摘要");
	lrs4.setStatus(language.getZdzy());
	languages.add(lrs4);
	
	LanguageResponse.LanguageRs lrs5 = new LanguageResponse.LanguageRs();
	lrs5.setName("短语提取");
	lrs5.setStatus(language.getDytq());
	languages.add(lrs5);
	
	LanguageResponse.LanguageRs lrs6 = new LanguageResponse.LanguageRs();
	lrs6.setName("拼音转换");
	lrs6.setStatus(language.getPyzh());
	languages.add(lrs6);
	
	LanguageResponse.LanguageRs lrs7 = new LanguageResponse.LanguageRs();
	lrs7.setName("文本推荐");
	lrs7.setStatus(language.getWbtj());
	languages.add(lrs7);
	
	LanguageResponse.LanguageRs lrs8 = new LanguageResponse.LanguageRs();
	lrs8.setName("文本分类");
	lrs8.setStatus(language.getWbfl());
	languages.add(lrs8);
	
	LanguageResponse.LanguageRs lrs9 = new LanguageResponse.LanguageRs();
	lrs9.setName("相似度");
	lrs9.setStatus(language.getXst());
	languages.add(lrs9);
	
	LanguageResponse.LanguageRs lrs10 = new LanguageResponse.LanguageRs();
	lrs10.setName("转简体字");
	lrs10.setStatus(language.getZjtz());
	languages.add(lrs10);
	
	LanguageResponse.LanguageRs lrs11 = new LanguageResponse.LanguageRs();
	lrs11.setName("转繁体字");
	lrs11.setStatus(language.getZftz());
	languages.add(lrs11);
	
	return languages;
    }
}
