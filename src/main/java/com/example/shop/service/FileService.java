package com.example.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class FileService {

    /**
     * 파일을 업로드함
     * @param imgFullPathName 업로드할 파일 이름(절대경로)
     * @param itemImgFile 업로드할 파일
     */
    public void uploadFile(String imgFullPathName, MultipartFile itemImgFile) throws IOException {

        itemImgFile.transferTo(new File(imgFullPathName));
    }

    /**
     * 파일을 삭제함
     * @param imgFullPathName 삭제할 파일 이름(절대경로)
     */
    public void deleteFile(String imgFullPathName) {
        File deleteFile = new File(imgFullPathName);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("{} 파일을 삭제하였습니다.", imgFullPathName);
        } else {
            log.info("{} 파일이 존재하지 않습니다.", imgFullPathName);
        }
    }
}
