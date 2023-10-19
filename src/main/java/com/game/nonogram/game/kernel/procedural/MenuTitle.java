package com.game.nonogram.game.kernel.procedural;

public enum MenuTitle {

    GAME_TITLE("NONOGRAM"),
    EXIT_MSG("Thank you for game! Bye!"),
    RECORDS_TITLE("RECORDS"),
    SIZE_TITLE("CHANGE SIZE"),
    UPLOAD_TITLE("UPLOAD MAP"),
    GAME_ONSTART_TITLE("GAME MENU");


    private final String title;

    MenuTitle(String title) {
        this.title = title;
    }

    public String get() {
        return title;
    }

}
