package com.computer.parts.shop;

import org.springframework.core.io.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication {


    private Resource blobFile;

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

}
