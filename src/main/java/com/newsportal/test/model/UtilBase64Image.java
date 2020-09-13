package com.newsportal.test.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

public class UtilBase64Image {
    public static String encoder(MultipartFile multipartFile) {
        try {
            System.out.println(multipartFile.getContentType());
            String base64Image = "data:"+multipartFile.getContentType()+";base64," + Base64.getEncoder().encodeToString(multipartFile.getBytes());
            System.out.println(base64Image);
            return base64Image;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
