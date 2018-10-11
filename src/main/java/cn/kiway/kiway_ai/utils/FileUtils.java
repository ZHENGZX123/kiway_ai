package cn.kiway.kiway_ai.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    @Value("${kiwayMarketplace.uploadFilePath}")
    private String uploadFilePath = "";

    private static String imagePath = "/static/image/";
    private static String apkPath = "/static/apk/";
    private static String zipPath = "/static/zip/";
    private static String otherPath = "/static/other/";

    public static String getFilePath(MultipartFile file) {
	String fileName = file.getOriginalFilename();
	// 获取文件的后缀名
	String suffixName = fileName.substring(fileName.lastIndexOf("."));
	fileName = UUID.randomUUID() + suffixName;
	String uploadPath = "";
	if (suffixName.equals(".png") || suffixName.equals(".jpg") || suffixName.equals(".jpeg")) {
	    uploadPath = imagePath;
	} else if (suffixName.equals(".zip") || suffixName.equals(".rar")) {
	    uploadPath = zipPath;
	} else if (suffixName.equals(".apk")) {
	    uploadPath = apkPath;
	} else {
	    uploadPath = otherPath;
	}
	Date d = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String dateNowStr =sdf.format(d) + "/";
	String path = uploadPath + dateNowStr + fileName;
	return path.replace("-", "");
    }

}
