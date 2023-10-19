package com.game.nonogram.game.solver.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermutationUtil {

    public PermutationUtil() {
//        int[] nums = {1,1,0,0};
//        List<List<Integer>> result = permute(nums);
////        for(List<Integer> res: result)
////            System.out.println(res);
//        System.out.println(result.size());
    }

    public  List<List<Integer>> permute(int[] simpleData) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(simpleData); // sort input array to group duplicate values together
        boolean[] used = new boolean[simpleData.length];
        for(int i=0;i<simpleData.length;i++){
            used[i]=false;
        }

        permuteHelper(new ArrayList<>(), simpleData, used, result);
        return result;
    }

    private  void permuteHelper(List<Integer> perm, int[] simpleData, boolean[] used, List<List<Integer>> result) {
        if (perm.size() == simpleData.length) {
            result.add(new ArrayList<>(perm));
        } else {
            int prev=-1;
            for (int i = 0; i < simpleData.length; i++) {
                if (!used[i] && simpleData[i]!=prev ) {
                    used[i] = true;
                    perm.add(simpleData[i]);
                    permuteHelper(perm, simpleData, used, result);
                    perm.remove(perm.size() - 1);
                    used[i] = false;
                    prev=simpleData[i];
                }
            }
        }
    }



}
