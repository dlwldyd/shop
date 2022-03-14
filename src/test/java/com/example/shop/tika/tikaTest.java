package com.example.shop.tika;

import org.apache.tika.Tika;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class tikaTest {

    @Test
    void tika() throws IOException {
        Tika tika = new Tika();
        String fileName = "tikaTestFile";
        String contentType = "jpg";
        String path = "C:/shop/item/tikaTestFile.jpg";
        MockMultipartFile mockMultipartFile = getMockMultipartFile(fileName, contentType, path);

        InputStream inputStream = mockMultipartFile.getInputStream();

        String fileType = tika.detect(inputStream);

        Assertions.assertThat(fileType).isEqualTo("image/jpeg");
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }
}
