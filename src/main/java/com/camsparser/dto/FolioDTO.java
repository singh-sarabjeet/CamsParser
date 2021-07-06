package com.camsparser.dto;

import lombok.Data;

import java.util.List;

@Data
public class FolioDTO {
    private Long folioNo;
    private List<TransactionDTO> transactions;
}
