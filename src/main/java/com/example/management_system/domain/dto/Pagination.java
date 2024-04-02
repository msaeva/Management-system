package com.example.management_system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Pagination<T> {
    private List<T> data;
    private long totalRecords;
}
