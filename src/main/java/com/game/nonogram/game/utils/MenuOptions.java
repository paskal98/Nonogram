package com.game.nonogram.game.utils;

public enum MenuOptions {

    START_GAME(1),
    SHOW_RECORDS(2),
    EXIT(3),
    BACK(4),
    START(5),
    SIZE(6),
    UPLOAD(7);

    private int classifier;

    MenuOptions(int classifier) {
        this.classifier = classifier;
    }

    public int get(){
        return this.classifier;
    }
}
