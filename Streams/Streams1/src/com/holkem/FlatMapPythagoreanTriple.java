package com.holkem;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FlatMapPythagoreanTriple {
    public static void main(String[] args) {
        // given a find b such that they make c perfect
        Stream<int[]> triplets = IntStream.rangeClosed(1, 100) // IntStream to use 1-100 stream
                .boxed() // need to convert from IntStream to Stream<Integer> bec flatMap will be returning a Stream<int[]> and will be in conflict with IntStream. besides, the target is int[] and IntStream are stream of ints not streams name only
                .flatMap(a -> // for each a, there would be a stream of triples. if use plain map(), result would be stream (main stream) of streams (each stream coming from each value of a). with flatMap(), all streams from each a would be combined together so that the result would be only a single stream
                        IntStream.rangeClosed(a, 100) // IntStream to use 1-100 stream
                            .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0) // while in int, can do the filtering
                                .boxed() // need to convert from IntStream to Stream<Integer> so can use the Stream<Integer>.map() that is able to receive a return of anything besides int which is the required return type by IntStream.map()
                                .map(b -> new int[]{a, b, (int) Math.sqrt(a*a + b * b)}) // from Stream<Integer>, mapping returned int[] so the type is now Stream<int[]>. compute value of c given a and b, then return 3 int into an array
                );
        triplets.limit(5)
                .forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));

        Stream<int[]> stream = IntStream.rangeClosed(1, 100)
                .filter(b -> Math.sqrt(1 + b * b) % 1 == 0)
                .boxed() // need to convert from IntStream to Stream<Integer> so can use the Stream<Integer>.map() that is able to receive a return of anything besides int which is the required return type by IntStream.map()
                .map(b -> new int[]{1, b, (int) Math.sqrt(1 + b * b)}); // from Stream<Integer>, mapping returned int[] so the type is now Stream<int[]>. compute value of c given a and b, then return 3 int into an array
    }
}
