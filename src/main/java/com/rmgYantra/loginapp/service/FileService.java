package com.rmgYantra.loginapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rmgYantra.loginapp.model.FileEntity;
import com.rmgYantra.loginapp.repo.FileRepository;

import java.io.IOException;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public FileEntity saveFile(MultipartFile multipartFile) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(multipartFile.getOriginalFilename());
        fileEntity.setFileData(multipartFile.getBytes());
        return fileRepository.save(fileEntity);
    }
    public FileEntity getFile(Long fileId) {
        return fileRepository.findById(fileId).orElse(null);
    }
}

