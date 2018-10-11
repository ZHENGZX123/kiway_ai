package cn.kiway.kiway_ai.entity;

import lombok.Data;

@Data
public class Language {
    private String id;
    private String appId;
    private int zwfc = 1;// 中文分词
    private int cxbz = 1;// 词性标注
    private int mmstsb = 1;// 命名实体识别
    private int gjctq = 1;// 关键词提取
    private int zdzy = 1;// 自动摘要
    private int dytq = 1;// 短语提取
    private int pyzh = 1;// 拼音转换
    private int wbtj = 1;// 文本推荐
    private int wbfl = 1;// 文本分类
    private int xst = 1;// 相似度
    private int zjtz = 1;// 转简体字
    private int zftz = 1;// 转繁体字
}
