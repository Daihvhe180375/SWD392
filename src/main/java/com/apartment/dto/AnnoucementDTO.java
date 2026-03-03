package com.apartment.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AnnoucementDTO {
    private String title;
    private String content;

    private Date createdAt;

    
}
