package com.songlanyun.jymall.common.upload.controller;

import com.songlanyun.jymall.common.upload.utils.FileUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.oss.cloud.OSSFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


/**
 * 图片上传
 *
 * @author zhouquan
 * @email zhouquan4877@163.com
 * @date 2019-03-29 15:58:27
 */
@CrossOrigin
@Controller
@RequestMapping("file")
@Component
public class FileController {

	/*@Value("${localPath}")
	public String localPath;
	@Value("${virtualPath}")
	public String virtualPath;*/
	@RequestMapping("upload")
	@ResponseBody
	public R savePicture(@RequestParam MultipartFile file) {
		// TODO Auto-generated method stub
		String url=null;
		try {
			String f = file.getOriginalFilename();
			String suffix = StringUtils.substringAfter(f, ".");
			String filename = FileUtils.getFileName(suffix, null);

			/********************OSS上传*************************/
			url = OSSFactory.build().upload(file.getBytes(), "yxtimages/" + filename);
			/********************OSS上传*************************/

			/*********本地上传(Tomcat配置映射C:/upload/file)*********/
			/*String path= localPath+filename;
			System.out.println("path="+path);
			File files = new File(localPath);
			if (!files.exists()) {
				files.mkdirs();
			}
			File newFile = new File(path);
			file.transferTo(newFile);
			url =localPath+filename;*/
			/*********本地上传(Tomcat配置映射C:/upload/file)*********/

		} catch (Exception e) {
			e.printStackTrace();
			R.error("上传异常");
		}
		return R.ok().put("data",url);
	}


	public R savePictures(@RequestParam MultipartFile file) {

		String url=null;
		try {
			String f = file.getOriginalFilename();
			String suffix = StringUtils.substringAfter(f, ".");
			String filename = FileUtils.getFileName(suffix, null);

			/********************OSS上传*************************/
			url = OSSFactory.build().upload(file.getBytes(), "xlggoods/" + filename);
			/********************OSS上传*************************/

			/*********本地上传(Tomcat配置映射C:/upload/file)*********/
			/*String path= localPath+filename;
			System.out.println("path="+path);
			File files = new File(localPath);
			if (!files.exists()) {
				files.mkdirs();
			}
			File newFile = new File(path);
			file.transferTo(newFile);
			url =localPath+filename;*/
			/*********本地上传(Tomcat配置映射C:/upload/file)*********/

		} catch (Exception e) {
			e.printStackTrace();
			R.error("上传异常");
		}
		return R.ok().put("data",url);
	}
	public R uploadPic(@RequestParam MultipartFile file) {

		String url=null;
		try {
			String f = file.getOriginalFilename();
			String suffix = StringUtils.substringAfter(f, ".");
			String filename = FileUtils.getFileName(suffix, null);

			/********************OSS上传*************************/
			url = OSSFactory.build().upload(file.getBytes(), "yxt/" + filename);
			/********************OSS上传*************************/

			/*********本地上传(Tomcat配置映射C:/upload/file)*********/
			/*String path= localPath+filename;
			System.out.println("path="+path);
			File files = new File(localPath);
			if (!files.exists()) {
				files.mkdirs();
			}
			File newFile = new File(path);
			file.transferTo(newFile);
			url =localPath+filename;*/
			/*********本地上传(Tomcat配置映射C:/upload/file)*********/

		} catch (Exception e) {
			e.printStackTrace();
			R.error("上传异常");
		}
		return R.ok().put("data",url);
	}

	public R upLoadApk(@RequestParam MultipartFile file) {

		String url=null;
		try {
			String f = file.getOriginalFilename();
			String suffix = StringUtils.substringAfter(f, ".");
			String filename = FileUtils.getFileName(suffix, null);

			/********************OSS上传*************************/
			url = OSSFactory.build().upload(file.getBytes(), "xlgapk/" + filename);
			/********************OSS上传*************************/

		} catch (Exception e) {
			e.printStackTrace();
			R.error("上传异常");
		}
		return R.ok().put("data",url);
	}
	public R upLoadIdcard(@RequestParam MultipartFile file) {

		String url=null;
		try {
			String f = file.getOriginalFilename();
			String suffix = StringUtils.substringAfter(f, ".");
			String filename = FileUtils.getFileName(suffix, null);

			/********************OSS上传*************************/
			url = OSSFactory.build().upload(file.getBytes(), "xlgcard/" + filename);
			/********************OSS上传*************************/

		} catch (Exception e) {
			e.printStackTrace();
			R.error("上传异常");
		}
		return R.ok().put("data",url);
	}
	public R upLoadExpressLogo(@RequestParam MultipartFile file) {

		String url=null;
		try {
			String f = file.getOriginalFilename();
			String suffix = StringUtils.substringAfter(f, ".");
			String filename = FileUtils.getFileName(suffix, null);

			/********************OSS上传*************************/
			url = OSSFactory.build().upload(file.getBytes(), "xlgExpressLogo/" + filename);
			/********************OSS上传*************************/

		} catch (Exception e) {
			e.printStackTrace();
			R.error("上传异常");
		}
		return R.ok().put("data",url);
	}
}
