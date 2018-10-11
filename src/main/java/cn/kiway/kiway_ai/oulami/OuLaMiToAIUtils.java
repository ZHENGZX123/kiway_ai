package cn.kiway.kiway_ai.oulami;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import ai.olami.ids.BaikeData;
import ai.olami.ids.CookingData;
import ai.olami.ids.ExchangeRateData;
import ai.olami.ids.IDSResult;
import ai.olami.ids.JokeData;
import ai.olami.ids.MathData;
import ai.olami.ids.NewsData;
import ai.olami.ids.OpenWebData;
import ai.olami.ids.PoemData;
import ai.olami.ids.StockMarketData;
import ai.olami.ids.TVProgramData;
import ai.olami.ids.UnitConvertData;
import ai.olami.ids.WeatherData;
import ai.olami.nli.NLIResult;
import cn.kiway.kiway_ai.utils.IContant;

public class OuLaMiToAIUtils {

    public static <T> JSONObject returnOULaMiToAI(NLIResult nLiResult) {

	String reply = nLiResult.getDescObject().getReplyAnswer();
	String type = nLiResult.getType();
	String arrays = "";

	if (nLiResult.hasDataObjects()) {
	    if (type.equals(IDSResult.Types.WEATHER.getName())) {// 天气
		ArrayList<WeatherData> dataArray = nLiResult.getDataObjects();
		arrays = new Gson().toJson(dataArray);
	    } else if (type.equals(IDSResult.Types.BAIKE.getName())) {// 百科
		ArrayList<BaikeData> dataArray = nLiResult.getDataObjects();
		arrays = new Gson().toJson(dataArray);
	    } else if (type.equals(IDSResult.Types.NEWS.getName())) {// 新闻
		ArrayList<NewsData> dataArray = nLiResult.getDataObjects();
		arrays = new Gson().toJson(dataArray);
	    } else if (type.equals(IDSResult.Types.TV_PROGRAM.getName())) {// 电视节目
		ArrayList<TVProgramData> dataArray = nLiResult.getDataObjects();
		arrays = new Gson().toJson(dataArray);
	    } else if (type.equals(IDSResult.Types.POEM.getName())) {// 诗句
		ArrayList<PoemData> dataArray = nLiResult.getDataObjects();
		arrays = new Gson().toJson(dataArray);
	    } else if (type.equals(IDSResult.Types.JOKE.getName())) {// 笑话、故事
		ArrayList<JokeData> dataArray = nLiResult.getDataObjects();
		arrays = new Gson().toJson(dataArray);
	    } else if (type.equals(IDSResult.Types.STOCK_MARKET.getName())) {// 股票市场
		ArrayList<StockMarketData> dataArray = nLiResult.getDataObjects();
		arrays = new Gson().toJson(dataArray);
	    } else if (type.equals(IDSResult.Types.MATH.getName())) {
		ArrayList<MathData> dataArray = nLiResult.getDataObjects();
		arrays = new Gson().toJson(dataArray);
	    } else if (type.equals(IDSResult.Types.UNIT_CONVERT.getName())) {// 换算
		ArrayList<UnitConvertData> dataArray = nLiResult.getDataObjects();
		arrays = new Gson().toJson(dataArray);
	    } else if (type.equals(IDSResult.Types.EXCHANGE_RATE.getName())) {// 汇率
		ArrayList<ExchangeRateData> dataArray = nLiResult.getDataObjects();
		arrays = new Gson().toJson(dataArray);
	    } else if (type.equals(IDSResult.Types.COOKING.getName())) {// 烹饪
		ArrayList<CookingData> dataArray = nLiResult.getDataObjects();
		arrays = new Gson().toJson(dataArray);
	    } else if (type.equals(IDSResult.Types.OPEN_WEB.getName())) {// 打开网页
		ArrayList<OpenWebData> dataArray = nLiResult.getDataObjects();
		arrays = new Gson().toJson(dataArray);
	    } else {
		arrays = new Gson().toJson(nLiResult.getDataObjects());
	    }
	}else{
	    return kiwayAiReply(reply, JSONArray.parseArray(arrays));
	}

	JSONObject jsonObject = new JSONObject();
	jsonObject.put("reply", reply);
	jsonObject.put("type", type);
	jsonObject.put("replyDetails", JSONArray.parseArray(arrays));
	return jsonObject;
    }

    public static JSONObject kiwayAiReply(String reply) {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("reply", reply);
	jsonObject.put("type", "kiwayAi");
	jsonObject.put("replyDetails", JSONArray.parseArray("[]"));
	return jsonObject;
    }

    public static JSONObject kiwayAiReply(String reply, JSONArray array) {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("reply", reply);
	jsonObject.put("type", "kiwayAi");
	jsonObject.put("replyDetails", array);
	return jsonObject;
    }

    public static JSONObject aiUnAnswer() {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("reply", IContant.ReplyContent);
	jsonObject.put("type", "UnAnswer");
	jsonObject.put("replyDetails", JSONArray.parseArray("[]"));
	return jsonObject;
    }

    public static JSONObject aiUnAnswer(String reply) {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("reply", reply);
	jsonObject.put("type", "UnAnswer");
	jsonObject.put("replyDetails", JSONArray.parseArray("[]"));
	return jsonObject;
    }
}
