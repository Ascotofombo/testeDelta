package com.delta.backend.service;

import com.delta.backend.utils.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    public File getFile(String key) throws IOException {

        File file = null;
        String folder = "C:/home";
        file = FileUploadUtil.getInstance().getFile(key, folder);

        if (!file.exists()) {
            throw new IOException();
        }

        return file;
    }
}
