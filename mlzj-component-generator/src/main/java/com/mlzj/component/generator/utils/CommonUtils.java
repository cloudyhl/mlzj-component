package com.mlzj.component.generator.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author yhl
 * @date 2023/4/25
 */
public class CommonUtils {

    private CommonUtils() {

    }

    public static void addFilesToZip(ZipOutputStream zipOut, File file) throws IOException {
        if (file.isDirectory()) {

            for (File subFile : file.listFiles()) {
                addFilesToZip(zipOut, subFile);
            }
        } else {
            String existFileName = getExistFileName(file);
            zipOut.putNextEntry(new ZipEntry(existFileName));
            Files.copy(file.toPath(), zipOut);
            zipOut.closeEntry();
        }
    }

    public static String getExistFileName(File file) {
        Configuration config = GenUtils.getConfig();
        String packageName = config.getString("package");
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }
        if (file.getAbsolutePath().indexOf("domain") > 0) {
            return packagePath + "domain" + File.separator + file.getName();
        }
        if (file.getAbsolutePath().indexOf("utils") > 0) {
            return packagePath + "utils" + File.separator + file.getName();
        }
        return packagePath;

    }


}
