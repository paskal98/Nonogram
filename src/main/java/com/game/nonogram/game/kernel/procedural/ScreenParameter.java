package com.game.nonogram.game.kernel.procedural;

public enum ScreenParameter {

    WIDTH(80);

    private final int parameter;

    ScreenParameter(int parameter) {
        this.parameter = parameter;
    }

    public int get() {
        return parameter;
    }

}
