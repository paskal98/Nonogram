package com.game.nonogram.game.utils;

public class CharToBooleanConverter {

    public static boolean[][] convertCharToBooleanArray(char[][] charArray) {
        int rows = charArray.length;
        int cols = charArray[0].length;

        boolean[][] booleanArray = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (charArray[i][j] == 'x') {
                    booleanArray[i][j] = true;
                } else if (charArray[i][j] == '_') {
                    booleanArray[i][j] = false;
                } else {
                    throw new IllegalArgumentException("Invalid character in charArray: " + charArray[i][j]);
                }
            }
        }

        return booleanArray;
    }

}
