package com.camsparser.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionDTO {

    private Date date;
    private String transaction;
    private Float amount;
    private Float units;
    private Float price;
    private Float unitBalance;
}
