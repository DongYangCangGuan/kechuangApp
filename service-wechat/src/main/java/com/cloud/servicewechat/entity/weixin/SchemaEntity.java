package com.cloud.servicewechat.entity.weixin;

import lombok.Data;

@Data
public class SchemaEntity {
    // 卷宗id
    private String id;

    //档案id
    private String documentId;
    private String businessCode;
    private String schema;
    private String file;
    private String imagefile;


}
