package com.cloud.servicemanage.entity;

import lombok.Data;

import java.util.List;

@Data
public class Question extends BaseEntity {
    private String id;
    private String questionDescription;
    private String sequen;
    private String questionType;
    private List<MyQuestion> myQuestion;
}
