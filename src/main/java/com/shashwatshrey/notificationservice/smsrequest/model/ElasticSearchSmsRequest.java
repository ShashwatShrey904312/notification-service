package com.shashwatshrey.notificationservice.smsrequest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;



@Document(indexName = "smsrequests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticSearchSmsRequest implements Serializable {

    @Id
    @Field(type = FieldType.Long)
    private long id;

    @Field(type=FieldType.Text)
    private String phoneNumber;

    @Field(type=FieldType.Text)
    private String message;

    @Field(type=FieldType.Text)
    private String failure_comments;

    @Field(type = FieldType.Integer)
    private int failure_code;

    @Field(type = FieldType.Date)
    private Date createdAt;

    @Field(type = FieldType.Date)
    private Date updatedAt ;


}
