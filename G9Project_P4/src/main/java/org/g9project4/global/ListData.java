package org.g9project4.global;

import lombok.Data;

import java.util.List;

@Data
public class ListData<T> {
    private List<T> items; // 목록 데이터
    private Pagination pagination;
}
