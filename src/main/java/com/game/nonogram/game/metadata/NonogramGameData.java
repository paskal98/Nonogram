package com.game.nonogram.game.metadata;



import com.game.nonogram.game.utils.TextColor;

import java.util.Arrays;

public class NonogramGameData {

    private char[][] filledField;
    private boolean[][] grid;
    private int[][] rows;
    private int[][] columns;
    private int size;
    private String gameTime;

    private boolean isSolved;

    public NonogramGameData() {
    }

    public NonogramGameData(int size) {
        this.size = size;
    }

    public NonogramGameData(char[][] filledField, boolean[][] grid, int[][] rows, int[][] columns, int size, String gameTime, boolean isSolved) {
        this.filledField = filledField;
        this.grid = grid;
        this.rows = rows;
        this.columns = columns;
        this.size = size;
        this.gameTime = gameTime;
        this.isSolved = isSolved;
    }

    public void showField() {

        // print column indexes
        System.out.print(TextColor.TEXT_WHITE.get() + "  ");
        for (int l = 0; l < size; l++)
            System.out.print((l + 1) + " ");
        System.out.println();

        // print field rows and clues
        for (int g = 0; g < size; g++) {

            // index of row
            System.out.print(TextColor.TEXT_WHITE.get() + (g + 1) + " ");

            // field row and clue
            for (int l = 0; l < size; l++)
                System.out.print(TextColor.TEXT_CYAN.get() + filledField[g][l] + " ");

            for (int l = 0; l < rows[g].length; l++)
                System.out.print(TextColor.TEXT_GREEN.get() + rows[g][l] + " ");
            System.out.println();

        }

        // print column clues
        for (int g = 0; g < getLineMaxLength(columns); g++) {
            System.out.print(TextColor.TEXT_GREEN.get() + "  ");

            //  clue
            for (int i = 0; i < size; i++)
                if (g < columns[i].length)
                    System.out.print(columns[i][g] + " ");
                else
                    System.out.print("  ");

            System.out.println();
        }


        System.out.println();

    }

    private int getLineMaxLength(int line[][]) {
        int maxLength = 0;
        for (int i = 0; i < line.length; i++)
            if (line[i].length > maxLength)
                maxLength = line[i].length;
        return maxLength;
    }


    public char[][] getFilledField() {
        return filledField;
    }

    public void setFilledField(char[][] filledField) {
        this.filledField = filledField;
    }


    public boolean[][] getGrid() {
        return grid;
    }

    public void setGrid(boolean[][] grid) {
        this.grid = grid;
    }

    public int[][] getRows() {
        return rows;
    }

    public void setRows(int[][] rows) {
        this.rows = rows;
    }

    public int[][] getColumns() {
        return columns;
    }

    public void setColumns(int[][] columns) {
        this.columns = columns;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getGameTime() {
        return gameTime;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    @Override
    public String toString() {
        return "NonogramGameData{" +
                "filed=" + Arrays.toString(filledField) +
                ", grid=" + Arrays.toString(grid) +
                ", rows=" + Arrays.toString(rows) +
                ", columns=" + Arrays.toString(columns) +
                ", size=" + size +
                ", gameTime='" + gameTime + '\'' +
                ", isSolved=" + isSolved +
                '}';
    }
}
