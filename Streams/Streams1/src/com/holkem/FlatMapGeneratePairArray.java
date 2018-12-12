package com.holkem;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class FlatMapGeneratePairArray {
    public static void main(String[] args) {
        List<Integer> nums1 = Arrays.asList(1, 2, 3);
        List<Integer> nums2 = Arrays.asList(3,4);

        System.out.println(nums1); // [1, 2, 3] printing a List<Integer>, passed int transformed to Integer

        int[] arr = new int[] {1,2,3};
        System.out.println(Arrays.asList(arr)); // [[I@2f7c7260] fails when passing int[] to List

        Integer[] arr2 = new Integer[] {1,2,3};
        System.out.println(Arrays.asList(arr2)); // [1, 2, 3] when passing Integer[] to List
        System.out.println();

        // generate pairing
        List<int[]> pairing = nums1.stream()
             .flatMap(a -> nums2.stream()
                            .map(b -> new int[] {a,b})
                     )
             .collect(Collectors.toList());
        pairing.forEach(pair -> {
            StringJoiner sj = new StringJoiner(",", "[", "]");
            for (int i : pair) {
                sj.add(String.valueOf(i));
            }
            System.out.println(sj.toString());
        });
        System.out.println();

        // generate pairing whose sum is divisible by 3
        pairing = nums1.stream()
                .flatMap(a -> nums2.stream()
                                   .filter(b -> (a+b) % 3 == 0)
                                   .map(b -> new int[] {a,b})
                )
                .collect(Collectors.toList());
        pairing.forEach(pair -> {
            StringJoiner sj = new StringJoiner(",", "[", "]");
            for (int i : pair) {
                sj.add(String.valueOf(i));
            }
            System.out.println(sj.toString());
        });
        System.out.println();
    }
}
