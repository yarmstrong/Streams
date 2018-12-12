package com.holkem;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class CollectStreamToGroups {

    public enum CaloricLevel { DIET, NORMAL, FAT }

    public static void main(String[] args) {

        List<Dish> menu = Menu.getMenu();

        /* GROUPINGBY into groups uses classifier Function<T,R> where the returned R is the
           group name in a way, like group of all Fish type */


        // GROUPINGBY : accepts a classifier Function<T,R> input T return R
        Map<Dish.Type, List<Dish>> groupByType = menu.stream()
                .collect(groupingBy(Dish::getType));
        System.out.println(groupByType); // Dish.toString returns only name



        // GROUPINGBY : classifier checks dish based on calorie content
        Map<CaloricLevel, List<Dish>> groupByCalorie = menu.stream()
                .collect(groupingBy(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                }));
        System.out.println(groupByCalorie);



        /* GROUPINGBY : 1st classifier group by type, applying 2nd collector (ie: filter)
           to apply on the grouped elements
           IMPORTANT HERE : apply the filter only when they are grouped, so that FISH type
           will be in the mapping even if all FISH dishes will be filtered out eventually
           - All args used within the collect method are all Collector coming from Collectors factory method */
        Map<Dish.Type, List<Dish>> dishBelow500MappedToDishObject = menu.stream()
                .collect(groupingBy(
                        Dish::getType,
                        filtering(d -> d.getCalories() > 500, toList()) // do a filter of elements in a group and accumulate them in a list
                ));
        System.out.println(dishBelow500MappedToDishObject);



        /* GROUPINGBY : 1st classifier group by type, applying 2nd collector to apply on
           the grouped elements (here: mapping used to map Dish to String so instead of
           List<Dish>, we get List<String>) */
        Map<Dish.Type, List<String>> dishBelow500MappedToDishName = menu.stream()
                .collect(groupingBy(
                        Dish::getType,
                        mapping(Dish::getName, toList()) // do a mapping (called map in stream, but mapping in Collectors) of elements in a group and accumulate them in a list
                ));
        System.out.println(dishBelow500MappedToDishName);



        // groupby Dish.Type, Dish => getName => dishTag.get("pork") => returns List => change to stream, then flatten
        final Map<String, List<String>> dishTags = Menu.getDishTags();
        Map<Dish.Type, Set<String>> dishTypeMappedToDishTags = menu.stream()
                .collect(groupingBy(
                        Dish::getType,
                        flatMapping(
                                // dish -> dishTags.get(dish.getName()).stream(),
                                dish -> dishTags.get(dish.getName()).stream(),
                                toSet()) // removes duplicates
                ));
        System.out.println(dishTypeMappedToDishTags);



        // MULTI-LEVEL GROUPING: map within a map
        Map<Dish.Type,
                Map<CaloricLevel, List<Dish>>> dishesByCalorieByType = menu.stream()
                .collect(groupingBy(
                        Dish::getType,
                        groupingBy(
                                dish -> {
                                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                    else return CaloricLevel.FAT;
                                }) // data is Stream<Dish> but massaged it into another grouping Map
                                // could have used toList() as arg2 for inner groupingBy by it defaults to toList so no need
                ));
        System.out.println(dishesByCalorieByType);
        // {MEAT={NORMAL=[beef], FAT=[pork], DIET=[chicken]},
        //  FISH={NORMAL=[salmon], DIET=[prawn]},
        //  OTHER={NORMAL=[french fries, pizza], DIET=[rice, season fruit]}}



        // COLLECTING DATA IN SUBGROUPS : so instead of a List<T>, computed for a single value (ie: sum)
        Map<Dish.Type, Long> numberOfDishPerType = menu.stream()
                .collect(groupingBy(Dish::getType, counting()));
        System.out.println(numberOfDishPerType);



        /* GET HIGHEST CALORIE DISH PER TYPE, note that maxBy returns Optional<T> not just T
           and use comparingInt instead of comparing */
        Map<Dish.Type, Optional<Dish>> maxCalorieDishPerType = menu.stream()
                .collect(groupingBy(Dish::getType,
                         maxBy(Comparator.comparingInt(Dish::getCalories))));// Stream<Dish> => Dish get max => but return Dish itself
        System.out.println(maxCalorieDishPerType);

        // reference : dish with max calories without grouping
        Optional<Dish> overallMaxCalorieDish = menu.stream().max(Comparator.comparingInt(Dish::getCalories));
        overallMaxCalorieDish.ifPresent(System.out::println);



        /* re-code of above but instead of getting Optional<T> (if Optional.isEmpty() Dish.Type will not show
           at all) so use collectingAndThen(originalCollectorWhoseReturnIsOptional, massageTheOptionalToDish)
           so that for FISH with no data, the result would still show FISH but with this data: FISH=null*/
        Map<Dish.Type, Dish> maxCalorieDishPerType2 = menu.stream()
                .collect(groupingBy(Dish::getType,
                         collectingAndThen(
                                 maxBy(Comparator.comparingInt(Dish::getCalories)),
                                 dish -> dish.orElse(null) // if null
                         )
                ));
        System.out.println(maxCalorieDishPerType2);

        /* NOTE: so basically u can use IntSummaryStatistics (COUNT,SUM,MIN,AVERAGE,MAX) together with groupingBy
           either individually as: counting, summingInt, maxBy(Comparator.comparingInt()), averagingInt,
           minBy(Comparator.comparingInt */


        // GROUPINGBY : CaloricLevel on each DishType
        Map<Dish.Type, Set<CaloricLevel>> test = menu.stream()
                .collect(groupingBy(
                        Dish::getType,
                        mapping(dish -> {
                            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        }, toCollection(HashSet::new)) // toSet() or use toCollection and provide ur supplier of your desired Set collection to use
                ));
        System.out.println(test);
    }
}
