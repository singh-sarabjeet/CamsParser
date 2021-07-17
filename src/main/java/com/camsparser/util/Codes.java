package com.camsparser.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Codes {

    CP_APP_200("CP_APP_200", "App is running");

    private String code;
    private String message;
}
