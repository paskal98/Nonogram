package com.game.nonogram.game.solver;

import java.util.Arrays;

public class NonogramUniqueSolver {

    private  int gridSize ;
    private  int[][] rowClues ;
    private  int[][] columnClues ;

    public NonogramUniqueSolver(int gridSize, int[][] rowClues, int[][] columnClues) {
        this.gridSize = gridSize;
        this.rowClues = rowClues;
        this.columnClues = columnClues;
    }

    public boolean check(){
        int[][] grid = new int[gridSize][gridSize];
        boolean[] rowComplete = new boolean[gridSize];
        int solutions = solve(0, 0, grid, rowComplete, 0);
        if(solutions>1) return true;
        return false;
    }

    private  int solve(int row, int col, int[][] grid, boolean[] rowComplete, int solutionCount) {
        if (solutionCount >= 2) return solutionCount;

        if (row == gridSize) {
            return solutionCount + 1;
        }

        int nextCol = (col + 1) % gridSize;
        int nextRow = (nextCol == 0) ? row + 1 : row;

        grid[row][col] = 0;
        if (checkPosition(row, col, grid)) {
            int count1 = solve(nextRow, nextCol, grid, rowComplete, solutionCount);
            if (count1 >= 2) return count1;
            solutionCount += count1;
        }

        grid[row][col] = 1;
        if (checkPosition(row, col, grid)) {
            int count2 = solve(nextRow, nextCol, grid, rowComplete, solutionCount);
            if (count2 >= 2) return count2;
            solutionCount += count2;
        }

        return solutionCount;
    }

    private  boolean checkPosition(int row, int col, int[][] grid) {
        int[] currentRow = grid[row];
        int[] currentColumn = new int[gridSize];
        for (int r = 0; r < gridSize; r++) {
            currentColumn[r] = grid[r][col];
        }
        return isValidPartialLine(rowClues[row], currentRow) && isValidPartialLine(columnClues[col], currentColumn);
    }

    private  boolean isValidPartialLine(int[] clues, int[] line) {
        int clueIdx = 0;
        int consecutive = 0;
        boolean lastCellFilled = false;
        for (int i = 0; i < line.length; i++) {
            int cell = line[i];
            if (cell == 1) {
                consecutive++;
                lastCellFilled = true;
            } else {
                if (lastCellFilled) {
                    if (clueIdx >= clues.length || clues[clueIdx] != consecutive) {
                        return false;
                    }
                    clueIdx++;
                    consecutive = 0;
                    lastCellFilled = false;
                }
            }
        }
        if (lastCellFilled) {
            if (clueIdx >= clues.length || clues[clueIdx] != consecutive) {
                return false;
            }
            clueIdx++;
        }
        if (clueIdx < clues.length && clues[clueIdx] <= consecutive) {
            return false;
        }
        return clueIdx <= clues.length;
    }


}



