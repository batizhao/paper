package io.github.batizhao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author batizhao
 * @date 2020/9/23
 */
public interface FileService extends IService<File> {

    /**
     * 上传
     * @param file
     * @return
     */
    File upload(MultipartFile file);

    /**
     * 上传并保存
     * @param file
     * @return
     */
    File uploadAndSave(MultipartFile file);

    /**
     * 加载资源
     * @param filename 文件名
     * @return
     */
    Resource loadAsResource(String filename);
}
