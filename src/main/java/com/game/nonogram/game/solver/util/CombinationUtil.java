package com.game.nonogram.game.solver.util;

import org.apache.commons.numbers.combinatorics.BinomialCoefficient;

public class CombinationUtil {

    public static  long getPossibleCombCount(int[] clueLine, int lineLength) {
        // calculate sum of clues
        int sumOfClues= getSumOfClue(clueLine);

        // get unusable spaces count (k)
        int unusedSpaces=getUnusedSpacesCount(sumOfClues,clueLine,lineLength);

        // get filed cells (n)
        int filledCells = lineLength-sumOfClues;

        // C(n k)
        return BinomialCoefficient.value(filledCells+1,unusedSpaces);
    }

    private static int getUnusedSpacesCount(int sumOfClues,int[] clueLine, int lineLength){
        int reservedSpaces = clueLine.length-1;
        return lineLength-sumOfClues-reservedSpaces;
    }

    public static int getUnusedSpacesCount(int[] clueLine, int lineLength){
        int sumOfClues= getSumOfClue(clueLine);
        return getUnusedSpacesCount(sumOfClues,clueLine,lineLength);
    }

    public static  int getSumOfClue(int[] clue){
        int sumOfClues=0;
        for (int j : clue) sumOfClues += j;
        return sumOfClues;
    }

}
