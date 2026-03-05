package com.apartment.dto;

import com.apartment.model.Staff;
import lombok.Data;

import java.util.Date;

@Data
public class AnnoucementDTO {
    private String title;
    private String content;

    private Date createdAt;

    private int priority;
}
