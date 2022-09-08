package com.example.rhythme_backend.dto.requestDto;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageRequest {
    private int page = 1;
    private int size = 6;

    private Sort.Direction direction = Sort.Direction.DESC;

    public org.springframework.data.domain.PageRequest of(int page,int size,Sort.Direction direction){
        this.page = page <= 0 ? 1 : page;

        int DEFAULT_SIZE = 10;
        int MAX_SIZE = 20;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;

        this.direction = direction;

        return org.springframework.data.domain.PageRequest.of(page - 1,size, direction);
    }

    public org.springframework.data.domain.PageRequest of(int page,int size){
        this.page = page <= 0 ? 1 : page;

        int DEFAULT_SIZE = 10;
        int MAX_SIZE = 20;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;

        return org.springframework.data.domain.PageRequest.of(page - 1,size);
    }

}
