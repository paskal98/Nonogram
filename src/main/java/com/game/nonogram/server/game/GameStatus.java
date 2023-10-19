package com.game.nonogram.server.game;

public enum GameStatus {

    DONE("DONE"),
    UNDONE("NOT DONE"),
    HINT_USE("USE HINTS"),
    SOLVE_USE("USE SOLVING");


    private final String status;

    GameStatus(String status) {
        this.status = status;
    }

    public String get() {
        return status;
    }

}
