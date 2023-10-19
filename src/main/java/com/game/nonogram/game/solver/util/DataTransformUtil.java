package com.game.nonogram.game.solver.util;

public class DataTransformUtil {


    public static String booleanToString(boolean[] defaultCombination){

        StringBuilder result = new StringBuilder("");

        for(int i=0;i<defaultCombination.length;i++){
            if(defaultCombination[i])
                result.append( 'x');
            else
                result.append( '_');
        }

        return String.valueOf(result);
    }

    public static boolean[] simpleDataToBoolean(int[] simpleData, int[] clue, int size){

        boolean [] rawCombination = new boolean[size];


        int clueIndex=0;
        int rawCounter=0;

        for(int i=0;i<simpleData.length;i++){

            if(simpleData[i]==0) rawCounter++;
            else if(simpleData[i]==1){
                for(int j=0;j<clue[clueIndex];j++){
                    rawCombination[rawCounter]=true;
                    rawCounter++;
                }
                rawCounter++;
                clueIndex++;
            }

        }


        return rawCombination;

    }

    public static int[] clueToSimpleData(int clue[],int size){

        // get unused spaces count
        int unusedSpaces= CombinationUtil.getUnusedSpacesCount(clue,size);

        // calc size for simple data
        int simpleDataSize = clue.length+unusedSpaces;

        //init array
        int [] simpleData = new int[simpleDataSize];

        //transform
        for (int i=0,downCount=clue.length;i<simpleDataSize;i++,downCount--){
            if(downCount>0)
                simpleData[i]=1;
            else
                simpleData[i]=0;
        }

        return simpleData;

    }

    public static char[] buildLineFromClue(int[] clues, int lineSize){

        char[] stringBuilder = new char[lineSize];

        int rawCounter=0;

        for(int i=0;i<clues.length;i++){
            for(int j=0;j<clues[i];j++)
                stringBuilder[rawCounter++]='x';
            if((rawCounter+1)<lineSize)
                stringBuilder[rawCounter++]='_';
        }

        for (int i=rawCounter;i<lineSize;i++)
            stringBuilder[rawCounter++]='_';


        return stringBuilder;

    }

}
