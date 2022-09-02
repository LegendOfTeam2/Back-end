package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.service.S3Service;
import com.example.rhythme_backend.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UploadController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestPart("imgUrl") List<MultipartFile> multipartFiles){
        s3Service.upload(multipartFiles);
        return new ResponseEntity<>(Message.success("업로드 성공"), HttpStatus.OK);
    }
}
