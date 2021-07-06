package com.camsparser.dto;

import lombok.Data;

import java.util.List;

@Data
public class FundDTO {
    private String name;
    private List<FolioDTO> folios;
    private String registrar;
    private String id;

}
