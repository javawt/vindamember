package com.songlanyun.jymall.common.upload.utils;

import com.songlanyun.jymall.common.utils.OSinfo;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

@Component
public class FileUtils {
	@Value("${localPath}")
	public static String localPath;
	@Value("${virtualPath}")
	public static String virtualPath;


	static {
		if (OSinfo.isLinux()){
			//TODO 对于Linux服务器，如果该路径不存在请提前创建好目录
			localPath = "/usr/jymall/img";
			virtualPath = "http://119.23.247.181:8080/jyshop/path/";
		}else{
			localPath = "C:/upload/file/";
//			virtualPath = "https://localhost:8082/sly/path/";
			virtualPath = "http://192.168.0.160:8080/jyshop/path/";
		}
	}

	/**
	 * 获取图片格式化名称
	 * @param suffix  后缀
	 * @param type  类型~选填
	 * @return
	 */
	public static String getFileName(String suffix, String type){
		StringBuffer filename = new StringBuffer(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));
		filename.append("_");
		if (StringUtils.isNotBlank(type)){
			filename.append(type).append("_");
		}
		filename.append(RandomStringUtils.randomAlphabetic(6)).append(".").append(suffix);
		return filename.toString();
	}
	/**
	 * 删除图片
	 * @param url
	 */
	public static void deleteFile(String url) {
		if (StringUtils.isNotBlank(url)) {
			if (url.length()>0) {
				File file=new File(url);
				file.delete();
			}
		}
	}

	/**
	 * 判断文件大小
	 *
	 * @param len
	 *            文件长度
	 * @param size
	 *            限制大小
	 * @param unit
	 *            限制单位（B,K,M,G）
	 * @return
	 */
	public static boolean checkFileSize(Long len, int size, String unit) {
//        long len = file.length();
		double fileSize = 0;
		if ("B".equals(unit.toUpperCase())) {
			fileSize = (double) len;
		} else if ("K".equals(unit.toUpperCase())) {
			fileSize = (double) len / 1024;
		} else if ("M".equals(unit.toUpperCase())) {
			fileSize = (double) len / 1048576;
		} else if ("G".equals(unit.toUpperCase())) {
			fileSize = (double) len / 1073741824;
		}
		if (fileSize > size) {
			return false;
		}
		return true;
	}

	/**
	 * MultipartFile 转 File
	 * @param file
	 * @throws Exception
	 */
	public static File multipartFileToFile(MultipartFile file ) throws Exception {

		File toFile = null;
		if(file.equals("")||file.getSize()<=0){
			file = null;
		}else {
			InputStream ins = file.getInputStream();
			toFile = new File(file.getOriginalFilename());
			inputStreamToFile(ins, toFile);
			ins.close();
		}
		return toFile;
	}

	public static void inputStreamToFile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取上传文件的绝对路径
	 * @param url 文件的虚拟路径
	 * @return  文件的绝对路径
	 */
	public static String getAbsUrl(String url){
		if(null != url && !"".equals(url)){
			String[] split = url.split(virtualPath,-1);
			String str = split[split.length - 1];
			return  localPath+str;
		}
		return  "";
	}
}
