package com.game.nonogram.game.solver.data;

public class Path {

    private LineData lineData;
    private int iteration=0;

    private char[][] field;

    public Path(LineData lineData, int iteration, char[][] field) {
        this.lineData = lineData;
        this.iteration = iteration;
        this.field = field;
    }

    public LineData getLineData() {
        return lineData;
    }

    public void setLineData(LineData lineData) {
        this.lineData = lineData;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public char[][] getField() {
        return field;
    }

    public void setField(char[][] field) {
        this.field = field;
    }
}
