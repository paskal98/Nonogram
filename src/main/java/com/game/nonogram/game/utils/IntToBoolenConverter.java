package com.game.nonogram.game.utils;

public class IntToBoolenConverter {

    public static boolean[][] convertIntToBooleanArray(int[][] intArray) {
        int rows = intArray.length;
        int cols = intArray[0].length;

        boolean[][] booleanArray = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (intArray[i][j] == 1) {
                    booleanArray[i][j] = true;
                } else if (intArray[i][j] == 0) {
                    booleanArray[i][j] = false;
                } else {
                    throw new IllegalArgumentException("Invalid character in charArray: " + intArray[i][j]);
                }
            }
        }

        return booleanArray;
    }

}
