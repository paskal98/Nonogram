package com.game.nonogram.server.thymeleafrecognizer;

public class TableData {

    private String nickName;
    private String solvingTime;
    private String score;
    private String mapSize;
    private String status;

    public TableData(String nickName, String solvingTime, String score, String mapSize, String status) {
        this.nickName = nickName;
        this.solvingTime = solvingTime;
        this.score = score;
        this.mapSize = mapSize;
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSolvingTime() {
        return solvingTime;
    }

    public void setSolvingTime(String solvingTime) {
        this.solvingTime = solvingTime;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMapSize() {
        return mapSize;
    }

    public void setMapSize(String mapSize) {
        this.mapSize = mapSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
