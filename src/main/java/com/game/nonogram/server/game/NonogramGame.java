package com.game.nonogram.server.game;

import com.game.nonogram.game.generator.NonogramGenerator;
import com.game.nonogram.game.metadata.NonogramGameData;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class NonogramGame {

    private NonogramGenerator nonogramGenerator;
    private NonogramGameData gameData;

    private GameStatus gameStatus=GameStatus.UNDONE;

    private long gameStartTime;

    private int hintCount;

    public long getGameStartTime() {
        return gameStartTime;
    }

    public NonogramGame() {
        gameStartTime=System.nanoTime();
    }

    public void createGame(int size){
        this.gameData=new NonogramGameData(size);
        nonogramGenerator=new NonogramGenerator(gameData.getSize());
        nonogramGenerator.generatePuzzle();
        gameData.setGrid(nonogramGenerator.getGrid());
        gameData.setRows(nonogramGenerator.getRows());
        gameData.setColumns(nonogramGenerator.getColumns());
        initField();
        hintCount=1;
    }

    public void fillCell(int indexRow, int indexColumn){

        if(gameData.getFilledField()[indexRow-1][indexColumn-1]=='_')
            gameData.getFilledField()[indexRow-1][indexColumn-1]='x';
        else
            gameData.getFilledField()[indexRow-1][indexColumn-1]='_';

        checkIfSolved();
        if(gameData.isSolved() && gameStatus == GameStatus.UNDONE)
            gameStatus=GameStatus.DONE;

    }

    private void checkIfSolved() {

        gameData.setSolved(true);

        for(int i=0;i<gameData.getSize();i++){
            for(int j=0;j<gameData.getSize();j++){
                if(gameData.getFilledField()[i][j]=='x' && !gameData.getGrid()[i][j]){
                    gameData.setSolved(false);
                    return;
                }
                if(gameData.getFilledField()[i][j]=='_' && gameData.getGrid()[i][j]){
                    gameData.setSolved(false);
                    return;
                }

            }
        }

    }

    public void resetField(){
        initField();
        hintCount=1;
        gameStartTime=System.nanoTime();
        gameStatus=GameStatus.UNDONE;
    }

    public void solve(){

        char[][] hintField = new char[gameData.getSize()][gameData.getSize()];

        for (int i = 0; i < gameData.getSize(); i++) {
            for (int j = 0; j < gameData.getSize(); j++) {
                if(gameData.getGrid()[i][j])
                    hintField[i][j]='x';
                else
                    hintField[i][j]='_';
            }
        }

        gameData.setFilledField(hintField);
        gameData.setSolved(true);
        gameStatus=GameStatus.SOLVE_USE;


    }

    public void hint(){

        if (hintCount == nonogramGenerator.getStepByStepSolution().size()) {
            solve();
        } else {

            char[][] hintField = new char[gameData.getSize()][gameData.getSize()];

            for (int i = 0; i < gameData.getSize(); i++)
                for (int j = 0; j < gameData.getSize(); j++)
                    hintField[i][j] = nonogramGenerator.getStepByStepSolution().get(hintCount).getField()[i][j];

            gameData.setFilledField(hintField);
            hintCount++;
        }
        gameStatus=GameStatus.HINT_USE;
    }

    private void initField() {
        char[][] field = new char[gameData.getSize()][gameData.getSize()];
        for (int i = 0; i < gameData.getSize(); i++)
            Arrays.fill(field[i], '_');
        gameData.setFilledField(field);
    }

    public NonogramGameData getGameData() {
        return gameData;
    }


    public boolean isSolved() {
        return gameData.isSolved();
    }

    public char[][] getFilledField() {
        return this.gameData.getFilledField();
    }

    public boolean[][] getGrid() {
        return this.gameData.getGrid();
    }

    public int[][] getRows() {
        return this.gameData.getRows();
    }

    public int[][] getColumns() {
        return this.gameData.getColumns();
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public NonogramGenerator getNonogramGenerator() {
        return nonogramGenerator;
    }

    @Override
    public String toString() {
        return "NonogramGame{" +
                "nonogramGenerator=" + nonogramGenerator +
                ", gameData=" + gameData +
                ", gameStatus=" + gameStatus +
                ", hintCount=" + hintCount +
                '}';
    }
}
