package com.game.nonogram.game.generator;


import com.game.nonogram.game.solver.NonogramSolver;
import com.game.nonogram.game.solver.NonogramUniqueSolver;
import com.game.nonogram.game.solver.data.Path;
import com.game.nonogram.game.utils.Convertor;
import com.game.nonogram.game.utils.TextColor;

import java.util.*;

public class NonogramGenerator {

    public static final int SIZE_MAX = 15;
    public static final int SIZE_MIN = 3;


    private int size;
    private boolean[][] grid;
    private int[][] rows;
    private int[][] columns;

    private int x;
    private boolean impossibleSolution;
    private int iter;

    private Stack<Path> stepByStepSolution;


    public NonogramGenerator(int size) {
        this.size = size;
        this.rows = new int[size][];
        this.columns = new int[size][];
        this.grid = new boolean[size][size];
        x = size*2;
        impossibleSolution=false;
        iter=1;
    }



    public void generatePuzzle() {

        x+=(size*iter);

        //fill grid by randoms values
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                grid[i][j] = new Random().nextBoolean();


        if(impossibleSolution) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (!grid[i][j] && x != 0) {
                        grid[i][j] = new Random().nextBoolean();
                        x--;
                    }
                }
            }
        }

        //create clues for rows and columns
        boolean[][] tempColumns = getColumns(grid);
        for (int i = 0; i < size; i++) {
            rows[i] = setClues(grid[i]);
            columns[i] = setClues(tempColumns[i]);
        }

        NonogramUniqueSolver nonogramUniqueSolver = new NonogramUniqueSolver(size,rows,columns);
        if(nonogramUniqueSolver.check()){
            impossibleSolution=true;
            iter++;
            System.out.println(TextColor.TEXT_RED.get()+"Multiply Solution..."+TextColor.TEXT_PURPLE.get()+ "\nNew generate..."+TextColor.TEXT_RESET.get());
            generatePuzzle();
        }


//        printFiledInt();
//        System.out.println();
//        printFiledIntInsideOut();

        //solving
        System.out.println(TextColor.TEXT_PURPLE.get()+"Solving..."+TextColor.TEXT_RESET.get());
        long startTime = System.nanoTime();

        NonogramSolver nonogramSolver =null;
        try{
            nonogramSolver = new NonogramSolver(grid, rows, columns, size);
            nonogramSolver.solve();
        } catch (Exception e){
            System.out.println(TextColor.TEXT_RED.get() +e+TextColor.TEXT_RESET.get());
            System.out.println(TextColor.TEXT_RED.get() +e.getLocalizedMessage()+TextColor.TEXT_RESET.get());
        }

        int permutations = nonogramSolver.getPermutations();

        //make field solvable
        if(permutations==-1) {
            impossibleSolution=true;
            iter++;
            System.out.println(TextColor.TEXT_RED.get()+"Impossible Solution..."+TextColor.TEXT_PURPLE.get()+ "\nNew generate..."+TextColor.TEXT_RESET.get());
            generatePuzzle();
        }

            setStepByStepSolution(nonogramSolver.getPath());
            System.out.println(TextColor.TEXT_PURPLE.get() + "SOLVED!\nAll necessary data prepared\n" + TextColor.TEXT_RESET.get());

            printExecutionData(startTime, permutations, nonogramSolver.getPath().size());


    }


    public void prepareUploadMap(boolean[][] uploadGrid){

        //refill grid
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                grid[i][j] = uploadGrid[i][j];

        //create clues for rows and columns
        boolean[][] tempColumns = getColumns(uploadGrid);
        for (int i = 0; i < size; i++) {
            rows[i] = setClues(uploadGrid[i]);
            columns[i] = setClues(tempColumns[i]);
        }

        System.out.println(TextColor.TEXT_PURPLE.get()+"Solving..."+TextColor.TEXT_RESET.get());

        NonogramSolver nonogramSolver =null;
        try{
            nonogramSolver = new NonogramSolver(grid, rows, columns, size);
            nonogramSolver.solve();
        } catch (Exception e){
            System.out.println(TextColor.TEXT_RED.get() +e+TextColor.TEXT_RESET.get());
            System.out.println(TextColor.TEXT_RED.get() +e.getLocalizedMessage()+TextColor.TEXT_RESET.get());
        }

        int permutations = nonogramSolver.getPermutations();

        if(permutations==-1) {
            impossibleSolution=true;
            iter++;
            System.out.println(TextColor.TEXT_RED.get()+"Impossible Solution..."+TextColor.TEXT_PURPLE.get()+ "\nNew generate..."+TextColor.TEXT_RESET.get());
            generatePuzzle();
        }
        setStepByStepSolution(nonogramSolver.getPath());
        System.out.println(TextColor.TEXT_PURPLE.get()+"SOLVED!\nAll necessary data prepared\n"+TextColor.TEXT_RESET.get());


    }

    public static int[] setClues(List<Boolean> gridLine) {

        boolean[] gridLineConverted = new boolean[gridLine.size()];
        for (int i = 0; i < gridLine.size(); i++)
            gridLineConverted[i] = gridLine.get(i);
        return setClues(gridLineConverted);

    }

    // set clues by one line from row or column
    public static int[] setClues(boolean[] gridLine) {
        List<Integer> clues = new ArrayList<>();
        boolean lastCell = false;
        int counter = 0;

        // take one cell from row or column line
        // with iteration create correct clues for puzzle
        for (boolean cell : gridLine) {
            if (cell) {
                if (lastCell) {
                    counter++;
                } else {
                    counter = 1;
                    lastCell = true;
                }
            } else if (counter > 0) {
                clues.add(counter);
                counter = 0;
                lastCell = false;
            }
        }

        if (counter > 0) clues.add(counter);
        else if (clues.isEmpty()) clues.add(0);


        return clues.stream().mapToInt(Integer::intValue).toArray();
    }

    //get columns from grid
    private boolean[][] getColumns(boolean[][] grid) {
        boolean[][] columns = new boolean[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                columns[i][j] = grid[j][i];

        return columns;
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public int[][] getRows() {
        return rows;
    }

    public int[][] getColumns() {
        return columns;
    }

    public Stack<Path> getStepByStepSolution() {
        return stepByStepSolution;
    }

    private void setStepByStepSolution(Stack<Path> stepByStepSolution) {
        this.stepByStepSolution = stepByStepSolution;
    }


    // procedural methods
    // print grid,row,column data
    private void printFiled() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
               if(grid[i][j])
                   System.out.printf("%s", 'x');
               else
                   System.out.printf("%s", '_');
            }
            System.out.println();
        }
    }

    private void printFiledInt() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(grid[i][j])
                    System.out.printf("%s ", 'x');
                else
                    System.out.printf("%s ", '_');
            }
            System.out.println();
        }
    }

    private void printFiledIntInsideOut() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(grid[i][j])
                    System.out.printf("%s ", '_');
                else
                    System.out.printf("%s ", 'x');
            }
            System.out.println();
        }
    }

    private void printRows() {
        for (int i = 0; i < size; i++)
            System.out.println(Arrays.toString(rows[i]));
    }

    private void printColumns() {
        for (int i = 0; i < size; i++)
            System.out.println(Arrays.toString(columns[i]));
    }

    private void printExecutionData(long startTime, int steps,int pathSize){

        long elapsedTime = System.nanoTime() - startTime;
        System.out.println(TextColor.TEXT_GREEN.get() + "\nIt takes: " + TextColor.TEXT_BLUE.get() + steps + " PERMUTATIONS" + TextColor.TEXT_RESET.get());
        System.out.println(TextColor.TEXT_GREEN.get() + "Stack size (solved steps): " + TextColor.TEXT_BLUE.get() + pathSize + " STEPS" + TextColor.TEXT_RESET.get());
        System.out.println(TextColor.TEXT_GREEN.get() +"Exec. time: " + TextColor.TEXT_BLUE.get() +elapsedTime / 1000000+"ms");
        System.out.println(TextColor.TEXT_GREEN.get() +"Mem. total: "+ TextColor.TEXT_BLUE.get() + Convertor.humanReadableByteCountSI(Runtime.getRuntime().totalMemory()) +TextColor.TEXT_RESET.get());
        System.out.println(TextColor.TEXT_GREEN.get() +"Mem. free:  "+ TextColor.TEXT_BLUE.get() +Convertor.humanReadableByteCountSI(Runtime.getRuntime().freeMemory()) +TextColor.TEXT_RESET.get());
        System.out.println(TextColor.TEXT_GREEN.get() +"Mem. used:  "+ TextColor.TEXT_BLUE.get() +Convertor.humanReadableByteCountSI(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) +"\n"+TextColor.TEXT_RESET.get());


    }

}
