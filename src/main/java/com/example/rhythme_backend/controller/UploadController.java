package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.service.S3Service;
import com.example.rhythme_backend.domain.Message;
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
        return new ResponseEntity<>(Message.success(s3Service.upload(multipartFiles)), HttpStatus.OK);
    }

    @PostMapping("/auth/upload/media")
    public ResponseEntity<?> uploadMedia(@RequestPart("mediaUrl") List<MultipartFile> multipartFiles){
        return new ResponseEntity<>(Message.success(s3Service.uploadMedia(multipartFiles)),HttpStatus.OK);
    }
}
