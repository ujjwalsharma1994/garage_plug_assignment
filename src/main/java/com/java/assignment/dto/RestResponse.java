package com.java.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RestResponse {
    private String message;
    private int statusCode;
    private Object data;
}
