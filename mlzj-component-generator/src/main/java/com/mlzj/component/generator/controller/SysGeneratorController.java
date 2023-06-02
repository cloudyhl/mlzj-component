
package com.mlzj.component.generator.controller;


import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import com.mlzj.component.generator.service.SysGeneratorService;
import com.mlzj.component.generator.utils.CommonUtils;
import com.mlzj.component.generator.utils.PageUtils;
import com.mlzj.component.generator.utils.Query;
import com.mlzj.component.generator.utils.R;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 代码生成器
 * 
 * @author yhl
 * @since 2022/8/4
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
	@Autowired
	private SysGeneratorService sysGeneratorService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils pageUtil = sysGeneratorService.queryList(new Query(params));
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 生成代码
	 */
	@RequestMapping("/code")
	public void code(String tables, HttpServletResponse response) throws IOException{

		try {
			byte[] data = sysGeneratorService.generatorCode(tables.split(","));

			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename=mlzj_code.zip");
			response.addHeader("Content-Length", "" + data.length);
			response.setContentType("application/octet-stream; charset=UTF-8");

			IOUtils.write(data, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
