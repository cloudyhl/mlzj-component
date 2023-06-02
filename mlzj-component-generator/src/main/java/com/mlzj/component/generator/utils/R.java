package com.mlzj.component.generator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 返回数据
 *
 * @author yhl
 * @since 2022/8/4
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public R() {
		put("code", 0);
	}
	
	public static R error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static R error(String msg) {
		return error(500, msg);
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public static void main(String[] args) throws FileNotFoundException {
//		String sourceFolder = "/path/to/source/folder";
//		String outputZip = "/path/to/output/zipfile.zip";
//		FileOutputStream fos = new FileOutputStream(outputZip);
//		ZipOutputStream zipOut = new ZipOutputStream(fos);
		String rootPackage = "com.mlzj.component.generator";
		String[] packageNames = rootPackage.split("\\."); // 把根包名按照 . 分割成不同的包名

		// 遍历每个包名，构造 Java 代码文件路径
		StringBuilder javaFilePath = new StringBuilder();
		for (String packageName : packageNames) {
			javaFilePath.append(File.separator).append(packageName);
		}
		FileInputStream inputStream = new FileInputStream(javaFilePath.append(File.separator).append("config").append(File.separator).append("DbConfig.java").toString());
		System.out.println(inputStream);
	}
}
