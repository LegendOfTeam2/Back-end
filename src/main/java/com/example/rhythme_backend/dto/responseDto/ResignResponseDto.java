package com.example.rhythme_backend.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResignResponseDto {

    private String email;
    private Long id;

}
