package com.game.nonogram.game.solver.util;



import com.game.nonogram.game.solver.data.LineData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {


    public static int getPossibleCountByFilled(Pattern pattern, int[] clue, int[][] allPossibleCombinationList, int lineSize) {
        int c = 0;
        for (int[] combination : allPossibleCombinationList) {

            boolean[] rawCombination = DataTransformUtil.simpleDataToBoolean(combination, clue, lineSize);
            Matcher m = pattern.matcher(DataTransformUtil.booleanToString(rawCombination));

            boolean matchFound = m.find();
            if (matchFound) c++;

        }


        return c;

    }

    public static void setCurrentPossibleCombinations(LineData lineData , Pattern pattern) {

        List<int[]> tempList = new ArrayList<>();

        int c = 0;
        for (int[] combination : lineData.getAllPossibleCombinationList()) {

            boolean[] rawCombination = DataTransformUtil.simpleDataToBoolean(combination, lineData.getClue(), lineData.getLineSize());
            Matcher m = pattern.matcher(DataTransformUtil.booleanToString(rawCombination));

            boolean matchFound = m.find();
            if (matchFound) {
                tempList.add(combination);
                c++;
            }

        }

        int[][] currentPossibleCombinations = new int[tempList.size()][];
        int i=0;
        for(int[] combination: tempList){
            currentPossibleCombinations[i] = combination;
            i++;
        }

        lineData.setCurrentPossibleCount(c);
        lineData.setCurrentPossibleCombinationList(currentPossibleCombinations);

    }


    public static Pattern getPattern(char[] inputLine) {

        StringBuilder pattern = new StringBuilder("^");

        int CountX = 0;
        int CountO = 0;

        boolean lastIsX = false;
        boolean lastIsO = false;


        for (int i = 0; i < inputLine.length; i++) {

            if (inputLine[i] == 'x') {
                if (lastIsO) {
                    pattern.append("[x|_]{" + CountO + "}");
                    CountO = 0;
                    lastIsO = false;
                }

                CountX++;
                lastIsX = true;

            } else {
                if (lastIsX) {
                    pattern.append("x{" + CountX + "}");
                    CountX = 0;
                    lastIsX = false;
                }

                CountO++;
                lastIsO = true;
            }

            if (lastIsX && (i + 1) == inputLine.length)
                pattern.append("x{" + CountX + "}");
            else if (lastIsO && (i + 1) == inputLine.length)
                pattern.append("[x|_]{" + CountO + "}");


        }

        Pattern pat = Pattern.compile(String.valueOf(pattern));
        return pat;
    }


    public static Pattern getDefaultPater(int[] clue) {

        StringBuilder pattern = new StringBuilder("_*");


        for (int i = 0; i < clue.length; i++) {
            if ((i + 1) < clue.length)
                pattern.append("x{" + clue[i] + "}_+");
            else
                pattern.append("x{" + clue[i] + "}");
        }


        pattern.append("_*");

        Pattern pat = Pattern.compile(String.valueOf(pattern));
        return pat;

    }

    public static boolean isLineCorrectByRegex(char[] line, LineData lineData) {

        String stringInput = "";
        for (int i = 0; i < line.length; i++) {
            stringInput += line[i];
        }

        Pattern pat = Pattern.compile(String.valueOf(lineData.getLinePattern()));
        Matcher matcher = pat.matcher(stringInput);
//        System.out.println(pat);

        int count = 0;
        while (matcher.find()) {
            count++;
            if(count==2) break;
        }

        if(count == 1)
            return true;

        return false;

    }

}
