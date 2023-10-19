package com.game.nonogram.game.solver.data;


import com.game.nonogram.game.solver.util.CombinationUtil;
import com.game.nonogram.game.solver.util.DataTransformUtil;
import com.game.nonogram.game.solver.util.PatternUtil;
import com.game.nonogram.game.solver.util.PermutationUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class LineData {

    private int[] clue;
    private String lineType;
    private  long allPossibleCombinationCount;
    private int lineIndex;
    private int lineSize;
    private Pattern linePattern;
    private int sumOfClue;


    private int[][] allPossibleCombinationList=null;
    private int[][] currentPossibleCombinationList=null;
    private long currentPossibleCount;

    private String filePath="";

    public LineData(String lineType, int[] clue, int lineSize) {
        this.clue = clue;
        this.sumOfClue= CombinationUtil.getSumOfClue(clue);
        this.lineType = lineType;
        this.lineSize = lineSize;

        this.allPossibleCombinationCount =  CombinationUtil.getPossibleCombCount(clue,lineSize);
        this.currentPossibleCount = this.allPossibleCombinationCount;

        this.linePattern = PatternUtil.getDefaultPater(clue) ;
    }


    public LineData(String lineType, int[] clue, int lineSize, int lineIndex) {
        this(lineType,clue,lineSize);
        this.lineIndex=lineIndex;
    }

    public int[] getClue() {
        return clue;
    }


    public String getLineType() {
        return lineType;
    }

    public long getAllPossibleCombinationCount() {
        return allPossibleCombinationCount;
    }



    public int getLineIndex() {
        return lineIndex;
    }

    public int getLineSize() {
        return lineSize;
    }


    public Pattern getLinePattern() {
        return linePattern;
    }


    public int[][] getAllPossibleCombinationList() {


        if(Objects.equals(this.filePath, "")){
            PermutationUtil permutationUtil = new PermutationUtil();
            int[] simpleData  = DataTransformUtil.clueToSimpleData(clue,lineSize);

            List<List<Integer>> temp =  permutationUtil.permute(simpleData);
            int[][] possibleCombinationList = new int[temp.size()][simpleData.length];

            int i=0;
            for( List<Integer> line: temp){
                possibleCombinationList[i] = line.stream().mapToInt(Integer::intValue).toArray();
                i++;
            }

            setAllPossibleCombinationList(possibleCombinationList);

        }


        return allPossibleCombinationList;
    }



    public void setAllPossibleCombinationList(int[][] allPossibleCombinationList) {
        this.allPossibleCombinationList = allPossibleCombinationList;
    }


    public int[][] getCurrentPossibleCombinationList() {
        if(currentPossibleCombinationList==null){
            setCurrentPossibleCombinationList(getAllPossibleCombinationList());
        }

        return currentPossibleCombinationList;
    }

    public void setCurrentPossibleCombinationList(int[][] currentPossibleCombinationList) {
        this.currentPossibleCombinationList = currentPossibleCombinationList;
    }

    public long getCurrentPossibleCount() {
        return currentPossibleCount;
    }

    public void setCurrentPossibleCount(long currentPossibleCount) {
        this.currentPossibleCount = currentPossibleCount;
    }


    public int getSumOfClue() {
        return sumOfClue;
    }

    @Override
    public String toString() {
        return "LineDataV2{" +
                "clue=" + Arrays.toString(clue) +
                ", lineType='" + lineType + '\'' +
                ", allPossibleCombinationCount=" + allPossibleCombinationCount +
                ", lineIndex=" + lineIndex +
                ", lineSize=" + lineSize +
                ", linePattern=" + linePattern +
                ", sumOfClue=" + sumOfClue +
                ", currentPossibleCount=" + currentPossibleCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineData that = (LineData) o;
        return allPossibleCombinationCount == that.allPossibleCombinationCount && lineSize == that.lineSize && Arrays.equals(clue, that.clue) && linePattern.equals(that.linePattern);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(allPossibleCombinationCount, lineSize, linePattern);
        result = 31 * result + Arrays.hashCode(clue);
        return result;
    }
}
