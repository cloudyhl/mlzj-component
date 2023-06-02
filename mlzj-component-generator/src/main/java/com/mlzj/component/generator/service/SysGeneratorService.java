
package com.mlzj.component.generator.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.mlzj.component.generator.dao.GeneratorDao;
import com.mlzj.component.generator.utils.CommonUtils;
import com.mlzj.component.generator.utils.GenUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;
import com.mlzj.component.generator.utils.PageUtils;
import com.mlzj.component.generator.utils.Query;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 代码生成器
 *
 * @author yhl
 * @since 2022/8/4
 */
@Service
public class SysGeneratorService {
	@Autowired
	private GeneratorDao generatorDao;

	public PageUtils queryList(Query query) {
		Page<?> page = PageHelper.startPage(query.getPage(), query.getLimit());
		List<Map<String, Object>> list = generatorDao.queryList(query);

		return new PageUtils(list, (int)page.getTotal(), query.getLimit(), query.getPage());
	}

	public Map<String, String> queryTable(String tableName) {
		return generatorDao.queryTable(tableName);
	}

	public List<Map<String, String>> queryColumns(String tableName) {
		return generatorDao.queryColumns(tableName);
	}

	public byte[] generatorCode(String[] tableNames) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		for(String tableName : tableNames){
			//查询表信息
			Map<String, String> table = queryTable(tableName);
			//查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			//生成代码
			GenUtils.generatorCode(table, columns, zip);
		}
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		// 将项目中的文件添加到Zip文件中
		CommonUtils.addFilesToZip(zip, new File(rootPath.replace("/target/classes", "/src/main/java/com/mlzj/component/generator/domain")));
		CommonUtils.addFilesToZip(zip, new File(rootPath.replace("/target/classes", "/src/main/java/com/mlzj/component/generator/utils/ObjectTools.java")));

		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}
}
