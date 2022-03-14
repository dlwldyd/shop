package com.example.shop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    public void uploadFile(String imgFullPathName, MultipartFile itemImgFile) throws IOException {

        itemImgFile.transferTo(new File(imgFullPathName));
    }
}
