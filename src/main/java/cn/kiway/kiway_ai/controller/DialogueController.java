package cn.kiway.kiway_ai.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import ai.olami.cloudService.APIConfiguration;
import ai.olami.cloudService.APIResponse;
import ai.olami.cloudService.TextRecognizer;
import ai.olami.nli.NLIResult;
import cn.kiway.kiway_ai.config.MySimHash;
import cn.kiway.kiway_ai.entity.Dialogue;
import cn.kiway.kiway_ai.entity.ResponseResult;
import cn.kiway.kiway_ai.entity.RobotModule;
import cn.kiway.kiway_ai.oulami.OuLaMiToAIUtils;
import cn.kiway.kiway_ai.service.DialogueService;
import cn.kiway.kiway_ai.service.ModuleService;
import cn.kiway.kiway_ai.service.RobotModuleService;
import cn.kiway.kiway_ai.service.RobotService;
import cn.kiway.kiway_ai.utils.IContant;
import cn.kiway.kiway_ai.utils.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by ym on 2018-07-06.
 */
@Api(tags = "对话信息")
@RestController
@RequestMapping("dialogue")
public class DialogueController extends BaseController {

    private Class<?> clazz = DialogueController.class;

    @Autowired
    private DialogueService service;
    @Autowired
    private RobotModuleService userModuleService;
    @Autowired
    private RobotService robotService;
    @Autowired
    private ModuleService moduleService;

    @ApiOperation("分页查询对话系统")
    @GetMapping
    public ResponseResult findPage(Dialogue entity, Page<Dialogue> page) {
	ResponseResult re = null;
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("module_id", entity.getModuleId());
	Page<Dialogue> pagination = service.selectPage(page, wrapper);

	Map<String, Object> retMap = new HashMap<String, Object>();
	retMap.put("list", pagination.getRecords());
	retMap.put("totalRecord", pagination.getTotal());
	retMap.put("totalPage", pagination.getPages());
	re = new ResponseResult<>(200, retMap);
	return re;
    }

    @ApiOperation("根据ID删除对话")
    @DeleteMapping("{id}")
    public ResponseResult deleteById(@PathVariable("id") String id) {
	service.deleteById(id);
	return ResponseResult.ok();
    }

    @ApiOperation("根据ID查询对话详细信息")
    @GetMapping("{id}")
    public ResponseResult getById(@PathVariable("id") String id) {
	Dialogue entity = service.selectById(id);
	return ResponseResult.ok(200, entity);
    }

    @ApiOperation("修改纪录")
    @PutMapping("{id}")
    public ResponseResult update(@Valid Dialogue entity, BindingResult result, HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	service.updateById(entity);
	return ResponseResult.ok();
    }

    @ApiOperation("新增纪录")
    @PostMapping
    public ResponseResult create(@Valid Dialogue entity, BindingResult result, HttpServletRequest request) {
	ResponseResult re = null;
	re = super.hasError(result);
	if (re != null) {
	    return re;
	}
	if (entity.getModuleId() == null) {
	    return new ResponseResult(StatusCode._500, null, "muodleId不能为空");
	}
	if (entity.getDialogueAnswer() == null) {
	    return new ResponseResult(StatusCode._500, null, "答案不能为空");
	}
	if (entity.getDialogueKey() == null) {
	    return new ResponseResult(StatusCode._500, null, "内容不能为空");
	}
	if (moduleService.selectById(entity.getModuleId()) == null) {
	    return new ResponseResult(StatusCode._500, null, "muodleId错误");
	}
	EntityWrapper warpper = new EntityWrapper();
	warpper.eq("module_id", entity.getModuleId());
	if (userModuleService.selectList(warpper).size() < 0) {
	    return ResponseResult.ok(StatusCode._500, "muodleId错误");
	}
	entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
	warpper.eq("dialogue_key", entity.getDialogueKey());
	if (service.selectList(warpper).size() > 0) {
	    List<Dialogue> list = service.selectList(warpper);
	    for (int i = 0; i < list.size(); i++) {
		Dialogue dialogue = list.get(i);
		dialogue.setDialogueAnswer(entity.getDialogueAnswer());
		service.updateById(dialogue);
	    }
	    return ResponseResult.ok(StatusCode._200, "存在键对值，已更新");
	}
	service.insert(entity);
	return ResponseResult.ok();
    }

    @ApiOperation("获取对话信息")
    @RequestMapping(value = "/getDialogue", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult getDialogue(@RequestParam(value = "whatUserSays") String whatUserSays,
	    @RequestParam(value = "robotId") String robotId) {
	if (robotService.selectById(robotId) == null) {
	    return ResponseResult.ok(StatusCode._500, "机器Id错误");
	}
	EntityWrapper userModuleEntity = new EntityWrapper();// 查找机器人的模块
	userModuleEntity.eq("robot_id", robotId).eq("isactive", "1");
	List<RobotModule> userModuleList = userModuleService.selectList(userModuleEntity);

	if (userModuleList.size() < 0) {// 一个自定义模块都没有
	    return ResponseResult.ok(StatusCode._500, OuLaMiToAIUtils.aiUnAnswer());
	}
	// TODO 从module
	List<Dialogue> dialogueList = null;
	for (RobotModule userModule : userModuleList) {// 先全匹配找法
	    EntityWrapper wrapper = new EntityWrapper();
	    wrapper.eq("dialogue_key", whatUserSays);
	    wrapper.eq("module_id", userModule.getModuleId());
	    if (dialogueList == null) {
		dialogueList = service.selectList(wrapper);
	    } else {
		dialogueList.addAll(service.selectList(wrapper));
	    }
	}

	if (dialogueList != null && dialogueList.size() > 0) {
	    return ResponseResult.ok(StatusCode._200,
		    OuLaMiToAIUtils.kiwayAiReply(dialogueList.get(0).getDialogueAnswer()));
	} else {
	    if (userModuleList.size() > 0) {// 查找用户模块 没有就返回不知道
		List<Dialogue> dialogueList2 = null;
		for (RobotModule userModule : userModuleList) {	
		    EntityWrapper wrapper = new EntityWrapper();
		    wrapper.eq("module_id", userModule.getModuleId());
		    if (dialogueList2 == null) {
			dialogueList2 = service.selectList(wrapper);
		    } else {
			dialogueList2.addAll(service.selectList(wrapper));
		    }
		}
		if (dialogueList2 == null) {
		    return ResponseResult.ok(StatusCode._200, reply(whatUserSays));
		}
		// TODO 句子相似度查询
		Map<String, MySimHash> hashs = new LinkedHashMap<String, MySimHash>();
		MySimHash Hash = new MySimHash(whatUserSays, 64);// 作为比较的文本
		double simdb = 0;
		int position = -1;
		for (int i = 0; i < dialogueList2.size(); i++) {// 找出最大相似度
		    MySimHash simhash = new MySimHash(dialogueList2.get(i).getDialogueKey(), 64);
		    double sim = simhash.getSemblance(Hash);
		    if (sim > 0.8 && sim > simdb) {
			position = i;
		    }
		}

		if (position < 0) {
		    return ResponseResult.ok(StatusCode._200, reply(whatUserSays));
		} else {
		    return ResponseResult.ok(StatusCode._200,
			    OuLaMiToAIUtils.kiwayAiReply(dialogueList2.get(position).getDialogueAnswer()));
		}
	    } else {
		return ResponseResult.ok(StatusCode._200, reply(whatUserSays));
	    }
	}
    }

    @ApiOperation("测试单个模块")
    @RequestMapping(value = "/getModuleDialogue", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult getModuleDialogue(@RequestParam(value = "whatUserSays") String whatUserSays,
	    @RequestParam(value = "moduleId") String moduleId) {
	EntityWrapper wrapper = new EntityWrapper();
	wrapper.eq("dialogue_key", whatUserSays);
	wrapper.eq("module_id", moduleId);
	List<Dialogue> dialogueList = service.selectList(wrapper);
	if (dialogueList != null && dialogueList.size() > 0) {
	    return ResponseResult.ok(StatusCode._200,
		    OuLaMiToAIUtils.kiwayAiReply(dialogueList.get(0).getDialogueAnswer()));
	}
	List<Dialogue> dialogueList2 = null;
	EntityWrapper wrapper1 = new EntityWrapper();
	wrapper1.eq("module_id", moduleId);
	dialogueList2 = service.selectList(wrapper1);
	if (dialogueList2 == null) {
	    return ResponseResult.ok(StatusCode._200, "不知道你在说什么");
	}
	// TODO 句子相似度查询
	Map<String, MySimHash> hashs = new LinkedHashMap<String, MySimHash>();
	MySimHash Hash = new MySimHash(whatUserSays, 64);// 作为比较的文本
	double simdb = 0;
	int position = -1;
	for (int i = 0; i < dialogueList2.size(); i++) {// 找出最大相似度
	    MySimHash simhash = new MySimHash(dialogueList2.get(i).getDialogueKey(), 64);
	    double sim = simhash.getSemblance(Hash);
	    if (sim > 0.8 && sim > simdb) {
		position = i;
	    }
	}
	if (position < 0) {
	    return ResponseResult.ok(StatusCode._200, "不知道你在说什么");
	} else {
	    return ResponseResult.ok(StatusCode._200,
		    OuLaMiToAIUtils.kiwayAiReply(dialogueList2.get(position).getDialogueAnswer()));
	}

    }

    @ApiOperation("exe批量上传")
    @PostMapping("/import/{moduleId}")
    public ResponseResult addUser(@PathVariable("moduleId") String moduleId, @RequestParam("file") MultipartFile file) {
	if (moduleService.selectById(moduleId) == null) {
	    return new ResponseResult(StatusCode._500, null, "muodleId错误");
	}
	try {
	    boolean notNull = false;
	    String fileName = file.getOriginalFilename();
	    List<Dialogue> dialogueList = new ArrayList<Dialogue>();
	    if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
		return new ResponseResult(StatusCode._500, null, "上传格式不正确");
	    }
	    boolean isExcel2003 = true;
	    if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
		isExcel2003 = false;
	    }
	    InputStream is = file.getInputStream();
	    Workbook wb = null;
	    if (isExcel2003) {
		wb = new HSSFWorkbook(is);
	    } else {
		wb = new XSSFWorkbook(is);
	    }
	    Sheet sheet = wb.getSheetAt(0);
	    if (sheet != null) {
		notNull = true;
	    }
	    Dialogue dialogue;
	    for (int r = 1; r <= sheet.getLastRowNum(); r++) {
		Row row = sheet.getRow(r);
		if (row == null) {
		    continue;
		}
		dialogue = new Dialogue();
		String dialogueKey = row.getCell(0).getStringCellValue();
		String dialogueAnswer = row.getCell(1).getStringCellValue();
		if (!dialogueKey.isEmpty() && !dialogueAnswer.isEmpty()) {
		    dialogue.setCreateTime(new Timestamp(System.currentTimeMillis()));
		    dialogue.setDialogueAnswer(dialogueAnswer);
		    dialogue.setDialogueKey(dialogueKey);
		    dialogue.setModuleId(moduleId);
		    dialogueList.add(dialogue);
		}
	    }
	    for (Dialogue dialogue2 : dialogueList) {
		EntityWrapper wrapper = new EntityWrapper();
		wrapper.eq("dialogue_key", dialogue2.getDialogueKey());
		wrapper.eq("module_id", dialogue2.getModuleId());
		if (service.selectObj(wrapper) == null) {
		    service.insert(dialogue2);
		} else {
		    service.update(dialogue2, wrapper);
		}
	    }
	    return new ResponseResult(StatusCode._200, null, "上传成功");
	} catch (Exception e) {
	    e.printStackTrace();
	    return new ResponseResult(StatusCode._500, null, "上传失败");
	}
    }

    // ---------------------------------欧拉蜜AI接口----------------------------------

    APIConfiguration config = new APIConfiguration(IContant.OLAMI_APP_KEY, IContant.OLAMI_APP_SECRET,
	    IContant.localizeOption);

    protected JSONObject reply(String whatUserSays) {
	TextRecognizer recoginzer = new TextRecognizer(config);
	try {
	    APIResponse response1 = recoginzer.requestNLI(whatUserSays);
	    NLIResult[] nliResults = response1.getData().getNLIResults();
	    if (nliResults[0].hasDescObject()) {
		return OuLaMiToAIUtils.returnOULaMiToAI(nliResults[0]);
	    } else {
		return OuLaMiToAIUtils.aiUnAnswer();
	    }
	} catch (NoSuchAlgorithmException | IOException e1) {
	    e1.printStackTrace();
	}
	return OuLaMiToAIUtils.aiUnAnswer();
    }

    /// *********************************
    @ApiOperation("获取对话信息")
    @GetMapping("/RobotChat/{robotId}")
    public ResponseResult Chat(@PathVariable("robotId") String robotId, String whatUserSays) {
	if (robotService.selectById(robotId) == null) {
	    return ResponseResult.ok(StatusCode._500, "机器Id错误");
	}
	EntityWrapper userModuleEntity = new EntityWrapper();// 查找机器人的模块
	userModuleEntity.eq("robot_id", robotId).eq("isactive", "1");
	List<RobotModule> userModuleList = userModuleService.selectList(userModuleEntity);

	if (userModuleList.size() < 0) {// 一个自定义模块都没有
	    return ResponseResult.ok(StatusCode._500, OuLaMiToAIUtils.aiUnAnswer());
	}
	// TODO 从module
	List<Dialogue> dialogueList = null;
	for (RobotModule userModule : userModuleList) {// 先全匹配找法
	    EntityWrapper wrapper = new EntityWrapper();
	    wrapper.eq("dialogue_key", whatUserSays);
	    wrapper.eq("module_id", userModule.getModuleId());
	    if (dialogueList == null) {
		dialogueList = service.selectList(wrapper);
	    } else {
		dialogueList.addAll(service.selectList(wrapper));
	    }
	}

	if (dialogueList != null && dialogueList.size() > 0) {
	    return ResponseResult.ok(StatusCode._200,
		    OuLaMiToAIUtils.kiwayAiReply(dialogueList.get(0).getDialogueAnswer()));
	} else {
	    if (userModuleList.size() > 0) {// 查找用户模块 没有就返回不知道
		List<Dialogue> dialogueList2 = null;
		for (RobotModule userModule : userModuleList) {
		    EntityWrapper wrapper = new EntityWrapper();
		    wrapper.eq("module_id", userModule.getModuleId());
		    if (dialogueList2 == null) {
			dialogueList2 = service.selectList(wrapper);
		    } else {
			dialogueList2.addAll(service.selectList(wrapper));
		    }
		}
		if (dialogueList2 == null) {
		    return ResponseResult.ok(StatusCode._200, reply(whatUserSays));
		}
		// TODO 句子相似度查询
		Map<String, MySimHash> hashs = new LinkedHashMap<String, MySimHash>();
		MySimHash Hash = new MySimHash(whatUserSays, 64);// 作为比较的文本
		double simdb = 0;
		int position = -1;
		for (int i = 0; i < dialogueList2.size(); i++) {// 找出最大相似度
		    MySimHash simhash = new MySimHash(dialogueList2.get(i).getDialogueKey(), 64);
		    double sim = simhash.getSemblance(Hash);
		    if (sim > 0.8 && sim > simdb) {
			position = i;
		    }
		}

		if (position < 0) {
		    return ResponseResult.ok(StatusCode._200, reply(whatUserSays));
		} else {
		    return ResponseResult.ok(StatusCode._200,
			    OuLaMiToAIUtils.kiwayAiReply(dialogueList2.get(position).getDialogueAnswer()));
		}
	    } else {
		return ResponseResult.ok(StatusCode._200, reply(whatUserSays));
	    }
	}
    }
}
