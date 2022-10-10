package com.cn.jmw.data.provider.base.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author jmw
 * @Description TODO
 * @date 2022年10月09日 17:15
 * @Version 1.0
 */
public class FileUtils {

    public static String concatPath(String... paths) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            if (StringUtils.isBlank(path)) {
                continue;
            }
            path = StringUtils.appendIfMissing(path, "/");
            if (i != 0) {
                path = StringUtils.removeStart(path, "/");
            }
            if (i == paths.length - 1) {
                path = StringUtils.removeEnd(path, "/");
            }
            stringBuilder.append(path);
        }
        return StringUtils.removeEnd(stringBuilder.toString(), "/");
    }
}
