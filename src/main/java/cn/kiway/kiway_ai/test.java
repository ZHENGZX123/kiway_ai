package cn.kiway.kiway_ai;

import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;

public class test {
public static void main(String[] args) {
    String text = "重载不是重任";
    List<Pinyin> pinyinList = HanLP.convertToPinyinList(text);
    System.out.println(pinyinList.toString());
}
}
