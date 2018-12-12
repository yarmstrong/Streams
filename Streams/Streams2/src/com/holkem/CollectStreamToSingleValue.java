package com.holkem;

import java.util.*;

import static java.util.stream.Collectors.*;

public class CollectStreamToSingleValue {

    public static void main(String[] args) {

        List<Dish> menu = Menu.getMenu();

        // COUNT: stream.count equivalent for stream.collect(counting)
        long countStream = menu.stream().count();
        Long countCollect = menu.stream().collect(counting());
        System.out.println(countStream);
        System.out.println(countCollect);


        // MIN/MAX: stream.max equivalent for stream.collect(maxBy)
        Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> maxStream = menu.stream().max(dishCaloriesComparator);
        Optional<Dish> maxCollect = menu.stream().collect(maxBy(dishCaloriesComparator));
        maxStream.ifPresent(System.out::println);
        maxCollect.ifPresent(System.out::println);


        // SUM: long/double/intstream.sum equivalent for stream.collect(summingInt/Long/Double)
        int sumIntStream = menu.stream().mapToInt(Dish::getCalories).sum();
        Integer sumCollect = menu.stream().collect(summingInt(Dish::getCalories));
        System.out.println(sumIntStream);
        System.out.println(sumCollect);


        // AVERAGE: long/double/intstream.average equivalent for stream.collect(averagingInt)
        OptionalDouble averageIntStream = menu.stream().mapToInt(Dish::getCalories).average();
        Double averageCollect = menu.stream().collect(averagingInt(Dish::getCalories));
        averageIntStream.ifPresent(d -> System.out.printf("%.02f%n", d));
        System.out.printf("%.02f%n", averageCollect);


        // GET 5 (COUNT,SUM,MIN,AVERAGE,MAX) IN 1 GO USING stream.collect(summarizingInt) => ~Int bec int is the data being summarized, Function<T,Integer>.
        IntSummaryStatistics calorieSummaryStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));
        System.out.println(calorieSummaryStatistics);


        // CONCAT STREAM INTO A SINGLE STRING
        // no need for map Dish obj to String if Dish.toString already returns name
        String concatString = menu.stream().map(Dish::getName).collect(joining(", "));
        System.out.println(concatString);


        // NOTE: ALL OF THESE ARE SPECIAL CASES OF THE COLLECTORS.REDUCING() FACTORY METHOD
        // WHICH MEANS U CAN DEFINE UR OWN REDUCTION PROCESS DIFFERENT FROM THE 6 SPECIAL CASES


        // REDUCING VERSION OF SUMMINGINT
        Integer sumCal = menu.stream().collect(reducing(0, Dish::getCalories, Integer::sum));
        System.out.println(sumCal);
    }
}
