package com.shashwatshrey.notificationservice.smsrequest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//Sms_Requests table in MySQL
//To resolve- status
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="SMS_REQUESTS")
@Builder
public class Sms_Requests implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private long id;
    @Column(name="PHONE_NUMBER")
    private String phone_number;
    @Column(name="MESSAGE")
    private String message;
    @Column(name="FAILURE_COMMENTS")
    private String failure_comments;
    @Column(name="FAILURE_CODE")
    private int failure_code;
    @Column(name="CREATED_AT")
    private Date created_at;
    @Column(name="UPDATED_AT")
    private Date updated_at;
    @Column(name = "STATUS")
    private String status;

}
