package org.g9project4.file.controllers;


import lombok.Data;

@Data
public class RequestThumb {
    private Long seq; //파일 등록번호
    private String url; // 원격 파일 URL
    private Integer width; //너비
    private Integer height; // 높이
}
