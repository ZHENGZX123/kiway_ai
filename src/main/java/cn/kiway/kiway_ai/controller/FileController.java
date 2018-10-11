package cn.kiway.kiway_ai.controller;

import java.io.File;
import java.io.IOException;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.kiway.kiway_ai.entity.ResponseResult;
import cn.kiway.kiway_ai.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Api(tags = "上传文件")
@RestController
@RequestMapping("file")
public class FileController {
    private String filePath = "static";

    @Value("${kiwayMarketplace.uploadFilePath}")
    private String uploadFilePath = "";
    @Value("${kiwayMarketplace.baseUrl}")
    private String baseUrl = "";

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * 文件上传相关代码
     * @param file
     * @return
     */
    @ApiOperation("上传文件")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseResult upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
	if (file.isEmpty()) {
	    return new ResponseResult<>(200, "文件不能为空");
	}
	// 文件上传后的路径
	String projectPath = new File(uploadFilePath).getAbsolutePath();
	String uploadPath = FileUtils.getFilePath(file);
	File dest = new File(projectPath + uploadPath);
	// 检测是否存在目录
	if (!dest.getParentFile().exists()) {
	    dest.getParentFile().mkdirs();
	}
	try {
	    file.transferTo(dest);
	    JSONObject data = new JSONObject();
	    data.put("url", baseUrl + uploadPath);
	    data.put("size", file.getSize());
	    data.put("suffix", file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
	    return new ResponseResult<>(200, data);
	} catch (IllegalStateException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return new ResponseResult(500, "上传失败");
    }
}
