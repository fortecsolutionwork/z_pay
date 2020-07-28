package com.application.zpay.models;

/**
 * Created by Gaganjot Singh on 21/07/2020.
 */
public class GasModel {

    String shortCode;
    String boardName;

    public GasModel(String shortCode, String boardName) {
        this.shortCode = shortCode;
        this.boardName = boardName;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
}
