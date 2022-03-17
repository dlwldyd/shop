package com.example.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class FileService {

    public void uploadFile(String imgFullPathName, MultipartFile itemImgFile) throws IOException {

        itemImgFile.transferTo(new File(imgFullPathName));
    }

    public void deleteFile(String filePath) {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("{} 파일을 삭제하였습니다.", filePath);
        } else {
            log.info("{} 파일이 존재하지 않습니다.", filePath);
        }
    }
}
