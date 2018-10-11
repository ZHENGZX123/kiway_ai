package cn.kiway.kiway_ai.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import cn.kiway.kiway_ai.config.MySimHash;
import cn.kiway.kiway_ai.entity.Language;
import cn.kiway.kiway_ai.entity.LanguageResponse;
import cn.kiway.kiway_ai.entity.ResponseResult;
import cn.kiway.kiway_ai.entity.UserApp;
import cn.kiway.kiway_ai.service.LanguageService;
import cn.kiway.kiway_ai.service.UserAppService;
import cn.kiway.kiway_ai.utils.StatusCode;
import cn.kiway.kiway_ai.utils.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "HanLP")
@RestController
@RequestMapping("HanLp")
public class HanLpController extends BaseController {
    @Autowired
    UserAppService userAppService;
    @Autowired
    LanguageService languageService;

    @ApiOperation("自然语言处理")
    @RequestMapping(value = "/analysis", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult getDialogue(@RequestParam(value = "type") String type, @RequestParam(value = "rq") String rq,
	    @RequestParam(value = "appId") String appId, @RequestParam(value = "appkey") String appkey) {
	UserApp userApp = userAppService.selectById(appId);
	if (userApp == null) {
	    return ResponseResult.ok(StatusCode._500, "AppID 错误");
	}
	if (!userApp.getAppKey().equals(appkey)) {
	    return ResponseResult.ok(StatusCode._500, "AppID与AppKey不匹配");
	}
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("app_id", appId);
	Language language = languageService.selectOne(wrapper);
	if (language == null) {
	    return new ResponseResult(StatusCode._500, null, "获取权限识别失败");
	}
	int types1 = Integer.parseInt(type);
	String replaceContent = "";
	switch (types1) {
	case 0:// 中文分词
	    if (language.getZwfc() == 1) {
		List<Term> termList = StandardTokenizer.segment(rq);
		replaceContent = termList.toString();
	    } else {
		replaceContent = " 中文分词权限未开通";
	    }
	    break;
	case 1:// 词性标注
	       // TODO 暂时不知道怎么做
	    if (language.getCxbz() == 1) {
		replaceContent = "未做该功能";
	    } else {
		replaceContent = " 词性标注权限未开通";
	    }
	    break;
	case 2:// 命名实体识别
	    if (language.getMmstsb() == 1) {
		replaceContent = "未做该功能";
	    } else {
		replaceContent = "命名实体识别权限未开通";
	    }
	    break;
	case 3:// 关键词提取
	    if (language.getGjctq() == 1) {
		List<String> keywordList = HanLP.extractKeyword(rq, 5);
		replaceContent = keywordList.toString();
	    } else {
		replaceContent = "关键词提取权限未开通";
	    }
	    break;
	case 4:// 自动摘要
	    if (language.getZdzy() == 1) {
		List<String> sentenceList = HanLP.extractSummary(rq, 3);
		replaceContent = sentenceList.toString();
	    } else {
		replaceContent = "自动摘要权限未开通";
	    }
	    break;
	case 5:// 短语提取
	    if (language.getDytq() == 1) {
		List<String> phraseList = HanLP.extractPhrase(rq, 10);
		replaceContent = phraseList.toString();
	    } else {
		replaceContent = "短语提取权限未开通";
	    }
	    break;
	case 6:// 拼音转换
	    if (language.getPyzh() == 1) {
		List<Pinyin> pinyinList = HanLP.convertToPinyinList(rq);
		replaceContent = pinyinList.toString();
	    } else {
		replaceContent = "拼音转换权限未开通";
	    }
	    break;
	case 7:// 文本推荐
	    if (language.getWbtj() == 1) {
		replaceContent = "未做该功能";
	    } else {
		replaceContent = "文本推荐权限未开通";
	    }
	    break;
	case 8:// 文本分类
	    if (language.getWbfl() == 1) {
		replaceContent = "未做该功能";
	    } else {
		replaceContent = " 文本分类权限未开通";
	    }
	    break;
	case 9://
	    if (language.getXst() == 1) {
		if (rq.split("ands").length >= 2) {
		    MySimHash testHash = new MySimHash(rq.split("ands")[0], 64);
		    MySimHash simhash = new MySimHash(rq.split("ands")[1], 64);
		    replaceContent = simhash.getSemblance(testHash) + "";
		} else {
		    replaceContent = "获取相似度失败";
		}
	    } else {
		replaceContent = "相似度权限未开通";
	    }
	    break;
	case 10:// 转简体字
	    if (language.getZjtz() == 1) {
		replaceContent = HanLP.convertToSimplifiedChinese(rq);
	    } else {
		replaceContent = " 转简体字权限未开通";
	    }
	    break;
	case 11:// 转繁体字
	    if (language.getZftz() == 1) {
		replaceContent = HanLP.convertToTraditionalChinese(rq);
	    } else {
		replaceContent = "转繁体字权限未开通";
	    }
	    break;
	default:
	    replaceContent = "未知请求参数，不知道你要做什么";
	    break;
	}
	return ResponseResult.ok(StatusCode._200, replaceContent);
    }

    @ApiOperation("获取自然语言权限")
    @GetMapping("{appId}")
    public ResponseResult getlanguage(@PathVariable("appId") String appId) {
	if (userAppService.selectById(appId) == null) {
	    return new ResponseResult(500, null, "appId错误");
	}
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("app_id", appId);
	Language language = languageService.selectOne(wrapper);
	if (language == null) {
	    return new ResponseResult(200, null, "获取自然语言权限失败");
	}
	LanguageResponse languageResponse = new LanguageResponse();
	languageResponse.setAppId(language.getAppId());
	languageResponse.setId(language.getId());
	languageResponse.setLanguages(Utils.LanguageList(language));
	return new ResponseResult(200, languageResponse, "请求成功");
    }

    @ApiOperation("修改自然语言权限")
    @GetMapping("updateLanguage")	
    public ResponseResult getlanguge(@Valid Language language) {
	if (languageService.selectById(language.getId()) == null) {
	    return new ResponseResult(500, null, "appId错误");
	}
	languageService.updateById(language);
	return new ResponseResult(200, null, "请求成功");
    }
}
