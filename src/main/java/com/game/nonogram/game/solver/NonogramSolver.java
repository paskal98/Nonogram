package com.game.nonogram.game.solver;


import com.game.nonogram.game.generator.NonogramGenerator;
import com.game.nonogram.game.solver.data.LineData;
import com.game.nonogram.game.solver.data.Path;
import com.game.nonogram.game.solver.util.DataTransformUtil;
import com.game.nonogram.game.solver.util.PatternUtil;
import com.game.nonogram.game.utils.TextColor;

import java.util.*;

import static com.game.nonogram.game.utils.CharToBooleanConverter.convertCharToBooleanArray;
import static com.game.nonogram.game.utils.CharToIntConverter.convertCharToIntArray;


public class NonogramSolver {

    private boolean[][] grid;
    private int[][] rowsClues;
    private int[][] columnsClues;

    private int size;
    private char[][] field;

    boolean[][] pathField;

    private List<LineData> linesData;

    private int permutations = 0;

    private Stack<Path> path = new Stack<>();

    private int solved[][];
    private long startTime = System.nanoTime();


    public NonogramSolver(boolean[][] grid, int[][] rowsClues, int[][] columnsClues, int size) {
        this.grid = grid;
        this.rowsClues = rowsClues;
        this.columnsClues = columnsClues;
        this.size = size;
        this.field = initField(size);
        this.pathField = new boolean[size][size];
        this.solved=new int[size][size];
    }

    public void solve() {

        // #0
        // Prepare necessary data for all lines
        linesData = new ArrayList<>();
        for (int i = 0; i < size; i++)
            linesData.add(new LineData("row", rowsClues[i], size, i));
        for (int i = 0; i < size; i++)
            linesData.add(new LineData("column", columnsClues[i], size, i));
        //end


        // #1
        //start position
        //Found the best start position (Find line where count of combinations is lowest)
        LineData start = linesData.get(0);
        long lowest = start.getAllPossibleCombinationCount();
        for (LineData line : linesData) {
            if (line.getAllPossibleCombinationCount() < lowest) {
                start = line;
                lowest = start.getAllPossibleCombinationCount();
            }
        }


        // #1.1 add to stack LineData
        // LineData represent data of each row and column
        // All necessary data for permutation contains in LineData
        path.push(new Path(start, 0, saveField()));


        // #2 Start permutations
        // Solving...
        try {

            startTime = System.nanoTime();
            while (true) {

                int res = permute();
                if (res == 100)
                    break;
                if (res == -101 || res == -102) {
                    permutations = -1;
                    break;
                }
                if (res == 1) {
                    if (isSolved())
                        break;
                    permutations = -1;
                    break;
                }
            }

        } catch (StackOverflowError | EmptyStackException excp) {
            System.out.println(TextColor.TEXT_RED.get() + excp + TextColor.TEXT_RESET.get());
            permutations = -1;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            permutations = -1;
        }

    }

    public boolean solver(){



        // #0
        // Prepare necessary data for all lines
        linesData = new ArrayList<>();
        for (int i = 0; i < size; i++)
            linesData.add(new LineData("row", rowsClues[i], size, i));
        for (int i = 0; i < size; i++)
            linesData.add(new LineData("column", columnsClues[i], size, i));
        //end

        // #1
        //start position
        //Found the best start position (Find line where count of combinations is lowest)
        LineData start = linesData.get(0);
        long lowest = start.getAllPossibleCombinationCount();
        for (LineData line : linesData) {
            if (line.getAllPossibleCombinationCount() < lowest) {
                start = line;
                lowest = start.getAllPossibleCombinationCount();
            }
        }


        // #1.1 add to stack LineData
        // LineData represent data of each row and column
        // All necessary data for permutation contains in LineData
        path.push(new Path(start, 0, saveField()));

        // #2 Start permutations
        // Solving...
        try {

            startTime = System.nanoTime();
            while (true) {

                int res = permute();
                if (res == 100)
                    break;
                if (res == -101 || res == -102) {
                    permutations = -1;
                    break;
                }
                if (res == 1) {
                    if (isSolved())
                        break;
                    permutations = -1;
                    break;
                }
            }

        } catch (StackOverflowError | EmptyStackException excp) {
            System.out.println(TextColor.TEXT_RED.get() + excp + TextColor.TEXT_RESET.get());
            permutations = -1;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            permutations = -1;
        }

        List<String> colPatterns = new ArrayList<>();
        List<String> rowPatterns = new ArrayList<>();

        for (int i=0;i<linesData.size()/2;i++){
            rowPatterns.add(String.valueOf(linesData.get(i).getLinePattern()));
        }

        for (int i=linesData.size()/2;i<linesData.size();i++){
            colPatterns.add(String.valueOf(linesData.get(i).getLinePattern()));
        }

        this.solved=convertCharToIntArray(field);

        //normal
        FieldMatcher nonogram = new FieldMatcher(this.solved, rowPatterns, colPatterns);



        return nonogram.isPatternValid();

    }

    private int permute() {
        permutations++;

        long elapsedTime = System.nanoTime() - startTime;

        Path currentNode = path.peek();


        // stop solving if it takes 300.000 permutations
        if (permutations % 300000 == 0)
            return -101;

        // stop solving if it takes more than 20 sec.
        if (elapsedTime / 1000000 > 10000)
            return -102;

        // check if solved, and stop solving
        if (isSolved())
            return 100;

        // update array of all current possible solutions
        updateCombinations(currentNode);

        // check if used all possible combination
        if (currentNode.getLineData().getCurrentPossibleCount() < currentNode.getIteration() + 1) {

            path.pop();
            currentNode = path.peek();

            int lastNodeIteration = currentNode.getIteration();
            lastNodeIteration++;
            currentNode.setIteration(lastNodeIteration);

            // return previous state of filed
            retrieveField(currentNode.getField());

            return 0;
        }

        //get possible solution
        boolean[] possibleSolution = insertSolution(
                currentNode.getLineData(),
                currentNode.getIteration(),
                currentNode.getLineData().getClue());

        //Save previous state of line
        char[] savedLine = savePreviousStateLine(currentNode.getLineData().getLineType());

        //fill line in field
        fillField(possibleSolution, currentNode.getLineData().getLineType());

        // check if line inputted correct in filed
        if (isSolutionCorrect()) {
            LineData lineData = nextStep();
            if (lineData != null) {
                path.push(new Path(lineData, 0, saveField()));
                return 0;
            }
            return 1;
        } else {
            //return previous state of inputted line
            returnPreviousStateLine(savedLine, currentNode.getLineData().getLineType());
            currentNode.setIteration(currentNode.getIteration() + 1);
            return 0;
        }

    }


    ////////// permute helpers


    //By filled line, create pattern.
    //Pattern help to find all possible solutions
    //Update array of all current possible solutions
    private void updateCombinations(Path currentNode) {

        char[] temp = new char[size];
        if (Objects.equals(currentNode.getLineData().getLineType(), "row"))
            temp = field[currentNode.getLineData().getLineIndex()];
        else if (Objects.equals(currentNode.getLineData().getLineType(), "column"))
            temp = getColumn(field, currentNode.getLineData().getLineIndex());

        PatternUtil.setCurrentPossibleCombinations(
                currentNode.getLineData(),
                PatternUtil.getPattern(temp));

    }

    private boolean isSolved() {



        List<String> colPatterns = new ArrayList<>();
        List<String> rowPatterns = new ArrayList<>();

        for (int i=0;i<linesData.size()/2;i++){
            rowPatterns.add(String.valueOf(linesData.get(i).getLinePattern()));
        }

        for (int i=linesData.size()/2;i<linesData.size();i++){
            colPatterns.add(String.valueOf(linesData.get(i).getLinePattern()));
        }

        this.solved=convertCharToIntArray(field);

        FieldMatcher nonogram = new FieldMatcher(this.solved, rowPatterns, colPatterns);

        return nonogram.isPatternValid();
    }

    private char[] savePreviousStateLine(String lineType) {

        char[] tempLine = new char[size];

        if (Objects.equals(lineType, "row")) {
            for (int i = 0; i < size; i++)
                tempLine[i] = field[path.peek().getLineData().getLineIndex()][i];
        } else if (Objects.equals(lineType, "column")) {
            for (int i = 0; i < size; i++)
                tempLine[i] = field[i][path.peek().getLineData().getLineIndex()];
        }

        return tempLine;
    }

    private void fillField(boolean[] possibleSolution, String lineType) {

        if (Objects.equals(lineType, "column")) {
            char[] filedTemp = fillLine(getColumn(
                            field,
                            path.peek().getLineData().getLineIndex()),
                    DataTransformUtil.booleanToString(possibleSolution).toCharArray());

            for (int i = 0; i < size; i++) {
                field[i][path.peek().getLineData().getLineIndex()] = filedTemp[i];
            }
        } else if (Objects.equals(lineType, "row")) {
            field[path.peek().getLineData().getLineIndex()] = fillLine(
                    field[path.peek().getLineData().getLineIndex()],
                    DataTransformUtil.booleanToString(possibleSolution).toCharArray());
        }

    }

    private void retrieveField(char[][] previousField) {

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.field[i][j] = previousField[i][j];
    }

    private char[] fillLine(char[] line, char[] solution) {

        for (int i = 0; i < size; i++) {
            if (line[i] == '_') {
                line[i] = solution[i];
            }
        }
        return line;

    }

    private char[][] saveField() {

        char[][] tempField = new char[size][size];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                tempField[i][j] = this.field[i][j];
        return tempField;
    }

    private void returnPreviousStateLine(char[] tempLine, String lineType) {
        if (Objects.equals(lineType, "row")) {
            field[path.peek().getLineData().getLineIndex()] = tempLine;
        } else if (Objects.equals(lineType, "column")) {
            for (int i = 0; i < size; i++)
                field[i][path.peek().getLineData().getLineIndex()] = tempLine[i];
        }
    }


    //// Solvers

    private boolean[] insertSolution(LineData lineData, int iterator, int[] clue) {


        int[] line = null;
        if (lineData.getCurrentPossibleCombinationList() != null)
            line = lineData.getCurrentPossibleCombinationList()[iterator];
        else
            line = lineData.getAllPossibleCombinationList()[iterator];


        return DataTransformUtil.simpleDataToBoolean(line, clue, size);
    }

    private LineData nextStep() {

        LineData tempLineData = null;

        int iteration = 0;

        //row
        long lowest = -1;
        for (int i = 0; i < size; i++, iteration++) {
            char[] temp = field[i];


            PatternUtil.setCurrentPossibleCombinations(
                    linesData.get(iteration),
                    PatternUtil.getPattern(temp));

            if (isFullFilled(temp, rowsClues[i])) {

                if (lowest == -1) {
                    tempLineData = linesData.get(iteration);
                    lowest = tempLineData.getCurrentPossibleCount();
                } else if (linesData.get(iteration).getCurrentPossibleCount() < lowest) {
                    tempLineData = linesData.get(iteration);
                    lowest = tempLineData.getCurrentPossibleCount();
                }
            }

        }


        //column
        for (int i = 0; i < size; i++, iteration++) {
            char[] temp = getColumn(field, i);


            PatternUtil.setCurrentPossibleCombinations(
                    linesData.get(iteration),
                    PatternUtil.getPattern(temp));

            if (isFullFilled(temp, columnsClues[i])) {

                if (lowest == -1) {
                    tempLineData = linesData.get(iteration);
                    lowest = tempLineData.getCurrentPossibleCount();
                } else if (linesData.get(iteration).getCurrentPossibleCount() < lowest) {
                    tempLineData = linesData.get(iteration);
                    lowest = tempLineData.getCurrentPossibleCount();
                }
            }
        }


        return tempLineData;

    }

    private boolean isSolutionCorrect() {

        int listIteration = 0;

        //row
        for (int i = 0; i < size; i++) {

            int countX = 0;
            for (int x = 0; x < size; x++)
                if (field[i][x] == 'x') countX++;

            //check input complete
            if (countX == linesData.get(listIteration).getSumOfClue() && countX != linesData.get(listIteration).getSumOfClue())
                if (!PatternUtil.isLineCorrectByRegex(field[i], linesData.get(listIteration))) return false;
            if (countX > linesData.get(listIteration).getSumOfClue()) return false;

            listIteration++;
        }

        //column
        for (int i = 0; i < size; i++) {

            int countX = 0;
            for (int x = 0; x < size; x++)
                if (field[x][i] == 'x') countX++;

            if (countX == linesData.get(listIteration).getSumOfClue() && countX != linesData.get(listIteration).getSumOfClue())
                if (!PatternUtil.isLineCorrectByRegex(getColumn(field, i), linesData.get(listIteration))) return false;
            if (countX > linesData.get(listIteration).getSumOfClue()) return false;

            listIteration++;

        }


        return true;
    }

    //////////////// others helpers
    private boolean isFullFilled(char[] line, int[] clues) {

        List<Boolean> combinations = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            if (line[i] == 'x') combinations.add(true);
            else combinations.add(false);
        }

        int[] res = NonogramGenerator.setClues(combinations);
        return !Arrays.equals(res, clues);
    }

    private char[][] initField(int size) {
        char[][] field = new char[size][size];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                field[i][j] = '_';

        return field;
    }

    private char[] getColumn(char[][] line, int columnIndex) {

        char[] result = new char[size];

        for (int i = 0; i < size; i++) {
            result[i] = line[i][columnIndex];
        }
        return result;
    }

    public int getPermutations() {
        return permutations;
    }

    public Stack<Path> getPath() {
        return path;
    }

    public int[][] getSolved() {
        return solved;
    }








}
