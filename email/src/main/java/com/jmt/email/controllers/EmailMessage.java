package com.jmt.email.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 이메일 메세지 데이터 클래스
 *
 * to : 수신인
 * subject : 제목
 * message : 내용
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {
    private String to;
    private String subject;
    private String message;
}
