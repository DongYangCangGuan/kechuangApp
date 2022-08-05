package com.cloud.servicemanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class Questionnaire extends BaseEntity {
    private String questionname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;
    private String releaseFlag;
    private String questionnairetypes;
    private String questionBelong;
    private List<Question> question;
}
