package com.crypto.model;

public enum CryptoType {

    DOGE("DOGE_values.csv"),
    BTC("BTC_values.csv"),
    ETH("ETH_values.csv"),
    LTC("LTC_values.csv"),
    XRP("XRP_values.csv");

    private final String fileName;

    CryptoType(String fileName){
        this.fileName = fileName;
    }

    public String fileName(){
        return fileName;
    }
}
