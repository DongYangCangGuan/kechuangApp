package com.cloud.servicemanage.entity;

import lombok.Data;

@Data
public class MyQuestion extends BaseEntity {
    private String id;
    private String questionId;
    private String answer;
    private String content;
}
