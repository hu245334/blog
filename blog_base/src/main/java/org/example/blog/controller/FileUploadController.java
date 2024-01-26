package org.example.blog.controller;

import org.example.blog.pojo.Result;
import org.example.blog.utils.AliOSSUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String fileName = null;
        if (originalFilename != null) {
            fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String img_url = AliOSSUtil.upload(fileName, file.getInputStream());
        return Result.success(img_url);
    }
}
