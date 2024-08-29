package org.g9project4.email.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 이메일 메세지 데이터 클래스
 *
 * to : 수신인
 * subject : 제목
 * message : 내용
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage implements Serializable {
        private String to;
        private String subject;
        private String message;
}