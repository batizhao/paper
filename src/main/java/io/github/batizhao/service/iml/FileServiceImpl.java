package io.github.batizhao.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import io.github.batizhao.exception.StorageException;
import io.github.batizhao.config.FileUploadProperties;
import io.github.batizhao.domain.File;
import io.github.batizhao.mapper.FileMapper;
import io.github.batizhao.service.FileService;
import io.github.batizhao.util.FileNameAndPathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

/**
 * @author batizhao
 * @date 2020/9/23
 */
@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    private final Path rootLocation;

    @Autowired
    public FileServiceImpl(FileUploadProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    @SneakyThrows
    public File upload(MultipartFile file) {
        log.info("rootLocation: {}", rootLocation);

        if (file == null) {
            throw new StorageException("Failed to store null file.");
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file " + filename);
        }
        if (filename.contains("..")) {
            // This is a security check
            throw new StorageException("Cannot store file with relative path outside current directory " + filename);
        }

        String hexFileName = FileNameAndPathUtils.fileNameEncode(filename);
        try (InputStream inputStream = file.getInputStream()) {
            Path target = this.rootLocation.resolve(FileNameAndPathUtils.pathEncode(hexFileName));
            Files.createDirectories(target.getParent());
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
        }

        //只返回文件名给前端，不包括路径
        return new File().setFileName(hexFileName).setName(filename)
                .setSize(file.getSize()).setUrl(this.rootLocation.toString())
                .setCreateTime(LocalDateTime.now());
    }

    @Override
    @Transactional
    public File uploadAndSave(MultipartFile file) {
        File f = upload(file);
        save(f);
        return f;
    }

    @Override
    @SneakyThrows
    public Resource loadAsResource(String filename) {
        Path file = rootLocation.resolve(FileNameAndPathUtils.pathEncode(filename));
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new StorageException("Could not read file: " + filename);
        }
    }

}
