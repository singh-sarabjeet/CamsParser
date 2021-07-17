package com.camsparser.dto;

import lombok.Data;

@Data
public class MetaDTO {
    private String code;
    private String message;
    private String responseId;
    private String requestId;
    private String displayMessage;

    public MetaDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
