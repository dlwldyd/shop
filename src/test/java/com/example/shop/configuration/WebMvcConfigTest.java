package com.example.shop.configuration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebMvcConfigTest {

    @Autowired
    private WebMvcConfig webMvcConfig;

    @Test
    void uploadPath() {
//        String path = webMvcConfig.getUploadPath();
//        Assertions.assertThat(path).isEqualTo("file:///C:/shop");
    }
}