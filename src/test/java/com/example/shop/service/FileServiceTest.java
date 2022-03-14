package com.example.shop.service;

import com.example.shop.MakeMockMultipartFile;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import static com.example.shop.MakeMockMultipartFile.*;

class FileServiceTest {

    @Test
    void fileServiceTest() throws IOException {
        FileService fileService = new FileService();
        String fileName = "tikaTestFile";
        String contentType = "jpg";
        String path = "C:/shop/item/tikaTestFile.jpg";
        String newFileName = UUID.randomUUID().toString();
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, path);

        fileService.uploadFile("C:/shop/item/" + newFileName + ".jpg", mockMultipartFile);
    }
}