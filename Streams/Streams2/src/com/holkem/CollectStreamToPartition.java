package com.holkem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;


public class CollectStreamToPartition<s> {

    public static void main(String[] args) {

        /* compared to GROUPINGBY into groups, GROUPINGBY into partition is grouped using Boolean,
           so that the classifier it uses is not a Function<T,R>, but a Predicate<T>, ie: either pass or fail */

        List<Dish> menu = Menu.getMenu();


        // PARTITIONED if vegetarian or not
        Map<Boolean, List<Dish>> vegetarianOrNot = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian)); // groupingBy also works but with partitioningBy ur sure there's only 2 groups
        System.out.println(vegetarianOrNot);


        // list of all vegetarian dishes only
        List<Dish> vegetarianDishes = vegetarianOrNot.get(true);
        // can get this without partitioningBy menus.stream.filter(vege).collect(toList);
        System.out.println(vegetarianDishes);


        // PARTITIONINGBY : for prime nums

        /* IntPredicate is the int-consuming version of Predicate which returns boolean */

        System.out.println(partitionPrimes(20, isPrime));
        System.out.println(partitionPrimes(20, nonPrime));


        System.out.println(new ArrayList<Integer>().stream().anyMatch(i -> true));
        System.out.println(new ArrayList<Integer>().stream().noneMatch(i -> true));
        System.out.println(new ArrayList<Integer>().stream().allMatch(i -> true));

        boolean b = new ArrayList<Integer>().stream()
                .noneMatch(i -> {
                    System.out.println("test");
                    return i != 0;
                });
        System.out.println(b);

    }

    private static final Predicate<Integer> isPrime = candidate -> { // expected to receive Integer not int
        int sqrt = (int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2, sqrt) // check for possible divisor with % 0 for value max its sqrt only
                .noneMatch(i -> candidate % i == 0);
    };

    private static final Predicate<Integer> nonPrime = candidate -> { // expected to receive Integer not int
        int sqrt = (int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2, sqrt) // check for possible divisor with % 0 for value max its sqrt only
                .anyMatch(i -> candidate % i == 0);
    };

    private static Map<Boolean, List<Integer>> partitionPrimes(int n, Predicate<Integer> predicate) {
        return IntStream.rangeClosed(2, n).boxed() // need to convert supplied int from IntStream into Integer for collecting into a Collection (no primitives)
                .collect(partitioningBy(predicate));
    }
}
