package com.game.nonogram.game.utils;

public enum TextColor {

    TEXT_RED("\u001B[31m"),
    TEXT_BLACK("\u001B[30m"),
    TEXT_GREEN("\u001B[32m"),
    TEXT_BLUE("\u001B[34m"),
    TEXT_RESET("\u001B[0m"),
    TEXT_PURPLE("\u001B[35m"),
    TEXT_CYAN("\u001B[36m"),
    TEXT_YELLOW("\u001B[33m"),
    TEXT_WHITE("\u001B[37m"),

    YELLOW_BACKGROUND("\u001B[43m"),
    BLUE_BACKGROUND("\u001B[44m"),
    BLACK_BACKGROUND("\u001B[40m"),
    PURPLE_BACKGROUND("\u001B[45m"),
    CYAN_BACKGROUND("\u001B[46m"),
    GREEN_BACKGROUND("\u001B[42m"),
    WHITE_BACKGROUND("\u001B[47m"),
    RED_BACKGROUND("\u001B[41m");


    private final String colorEncode;

    TextColor(String colorEncode) {
        this.colorEncode = colorEncode;
    }

    public String get() {
        return colorEncode;
    }
}
