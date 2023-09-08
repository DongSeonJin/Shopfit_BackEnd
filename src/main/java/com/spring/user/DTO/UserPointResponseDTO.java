package com.spring.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class UserPointResponseDTO {

    private Long userId;
    private int point;

}
