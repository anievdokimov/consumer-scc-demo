package com.thomascook.consumerdemo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultResponse {
    private String name;
    private Integer age;
    private boolean isSimpson;
}
