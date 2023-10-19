package com.game.nonogram.game.utils;

public class CharToIntConverter {

    public static int[][] convertCharToIntArray(char[][] charArray) {
        int rows = charArray.length;
        int cols = charArray[0].length;

        int[][] booleanArray = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (charArray[i][j] == 'x') {
                    booleanArray[i][j] = 1;
                } else if (charArray[i][j] == '_') {
                    booleanArray[i][j] = 0;
                } else {
                    throw new IllegalArgumentException("Invalid character in charArray: " + charArray[i][j]);
                }
            }
        }

        return booleanArray;
    }

}
