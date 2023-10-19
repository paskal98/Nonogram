package com.game.nonogram.game.solver;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldMatcher {

    private int[][] grid;
    private List<String> rowPatterns;
    private List<String> colPatterns;

    public FieldMatcher(int[][] grid, List<String> rowPatterns, List<String> colPatterns) {
        this.grid = grid;
        this.rowPatterns = rowPatterns;
        this.colPatterns = colPatterns;
    }

    public boolean isPatternValid() {
        for (int i = 0; i < grid.length; i++) {
            if (!checkPattern(grid[i], rowPatterns.get(i))) {
                return false;
            }
        }

        for (int i = 0; i < grid[0].length; i++) {
            int[] col = new int[grid.length];
            for (int j = 0; j < grid.length; j++) {
                col[j] = grid[j][i];
            }
            if (!checkPattern(col, colPatterns.get(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean checkPattern(int[] line, String patternStr) {
        String actualPattern = getPattern(line);
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(actualPattern);
        return matcher.matches();
    }

    private String getPattern(int[] line) {
        StringBuilder pattern = new StringBuilder();
        for (int i = 0; i < line.length; i++) {
            pattern.append(line[i] == 1 ? "x" : "_");
        }
        return pattern.toString();
    }

    public static void main(String[] args) {
        int[][] grid = {
                {1, 0, 1},
                {1, 1, 1},
                {1, 0, 1}
        };
        List<String> colPatterns = Arrays.asList("_*x{3}_*", "_*x{1}_*", "_*x{3}_*");
        List<String> rowPatterns = Arrays.asList("_*x{1}_+x{1}_*", "_*x{3}_*", "_*x{1}_+x{1}_*");

        FieldMatcher nonogram = new FieldMatcher(grid, rowPatterns, colPatterns);
        System.out.println("Is the pattern valid? " + nonogram.isPatternValid());
    }

}
