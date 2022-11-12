package com.kwarcinski.exchanges.bitbay.entity;

import lombok.Data;

import java.util.List;

@Data
public class History {

    private String status;

    private List<List<DataTime>> items = null;


}