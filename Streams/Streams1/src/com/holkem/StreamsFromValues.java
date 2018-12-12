package com.holkem;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamsFromValues {
    public static void main(String[] args) {
        // #1: manipulating words using streams instead of array looping
        Stream<String> s = Stream.of("word1", "word2", "word3", "word4");
        String upWords = s.map(String::toUpperCase).collect(Collectors.joining(" "));
        System.out.println(upWords);

        // #2: Stream.ofNullable()
        String homeValue = System.getProperty("home");

        Stream<String> stream = homeValue == null ? Stream.empty() : Stream.of(homeValue);
        stream.forEach(System.out::println);

        Stream<String> homeValueStream = Stream.ofNullable(homeValue);
        homeValueStream.forEach(System.out::println);

        // #3: Arrays.stream() : strean generated will be based on the type of array used ie: int[] will give IntStream
        double[] doubleArr = {1L, 2L, 3L, 4L, 5L};
        System.out.println(Arrays.stream(doubleArr, 1, 4).sum()); // index 1 to 3 only

        // #4: Stream from files using NIO Files.lines(), see FlatMapStreamFromFiles.java

        // #5: generate stream based on function, this is an infinite stream with values produced / generated on demand, use limit to not to calculate values forever
        Stream<Double> iterableDoubleStream = Stream.iterate(0.0, n -> n + 2.0);
        Stream<Long> iterableLongStream = Stream.iterate(0L, n -> n + 2L);
        IntStream iterateInt = Stream.iterate(0, n -> n + 2).mapToInt(t -> t); // Stream<Integer> to IntStream

        iterableDoubleStream.limit(10).forEach(System.out::println);

    }
}
