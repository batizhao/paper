package io.github.batizhao.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author batizhao
 * @date 2020/9/27
 */
@UtilityClass
public class FileNameAndPathUtils {

    /**
     * 对文件名进行 md5
     * @param fileName 文件名
     * @return
     */
    public String fileNameEncode(String fileName) {
        String username = SecurityUtils.getUser().getUsername();
        return DigestUtils.md5Hex(username + fileName) + fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 根据 fileNameEncode 生成的文件名，生成 hash 目录结构
     * @param hexFileName hash 以后的文件名
     * @return
     */
    public String pathEncode(String hexFileName) {
        String secondMD5 = DigestUtils.md5Hex(hexFileName);
        String p1 = secondMD5.substring(0, 2);
        String p2 = secondMD5.substring(2, 4);

        StringBuilder sb = new StringBuilder();
        sb.append(p1)
                .append("/").append(p2)
                .append("/").append(hexFileName);
        return sb.toString();
    }

}
