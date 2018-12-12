package com.holkem;

import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamIterateFibonacci {
    public static void main(String[] args) {
        // final List<Integer> fib = Arrays.asList(0, 1);
        // 0, 1, => 1 (sum of previous 2)
        // to get n(2) = n(2-1) + n(2-2) => same logic to use for code below
        // fib.add(fib.get(n - 1) + fib.get(n - 2));

        /* needs init arr with the 2 initial fibonacci numbers since we need them to be able
           to compute for the sum of previous 2 numbers in fibonacci. start at [0,1]
           succeeding will be [1, 1], [1, 2], [arr[1], arr[0]+arr[1]]. so basically the array
           u generate every stream iteration is a copy of the 2numbers to be used to compute
           the next fibonacci number */
        String fibNumbers = Stream.iterate(new int[]{0, 1},
                t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .map(t -> "[" + t[0] + ", " + t[1] + "]")
                .collect(Collectors.joining(", "));
        System.out.println(fibNumbers);


        /* same as above but only printing the normal fibonacci sequence (no array)
           since arr[0] is a copy of arr[1] which is where the summation of last 2
           fibonacci numbers is saved, then we only need to extract arr[0] */
        fibNumbers = Stream.iterate(new int[]{0, 1},
                t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .map(t -> String.valueOf(t[0]))
                .collect(Collectors.joining(", "));
        System.out.println(fibNumbers);


        // get fib sequence below 100, makes use of the Predicate in Stream.iterate
        fibNumbers = Stream.iterate(new int[]{0,1},
                t -> t[0] < 200,
                t -> new int[]{t[1],t[0]+t[1]})
              .map(t -> String.valueOf(t[0]))
              .collect(Collectors.joining(", "));
        System.out.println(fibNumbers);


        /* use Stream.takeWhile in the operation if not using a Predicate in Stream.iterate
           or even Stream.limit */
        fibNumbers = Stream.iterate(new int[]{0,1},
                t -> new int[]{t[1],t[0]+t[1]})
                .takeWhile(t -> t[0] < 200)
                .map(t -> String.valueOf(t[0]))
                .collect(Collectors.joining(", "));
        System.out.println(fibNumbers);


        /* Stream.generate() vs Stream.iterate() :
           - iterate uses a UnaryOperator (receives T applies operation and returns the same type it received)
           - generate uses Supplier (receives no param, but supplies / return something)
           - generate doesn't depend on previous data while iterate does
           */
        Stream.generate(() -> (int) (Math.random()*100))
              .limit(5)
              .forEach(t -> System.out.print(t + " "));
        System.out.println();


        /* do the fibonacci sequence array again using Stream.generate : the supplier will be saving its own array that can be used to generate the next sequence. to be able for Supplier to save its own array, array needs to be instance variable, meaning Supplier will be an anonymous class instance instead of lambda. so we can say Supplier instance here has "mutable" state. */
        /* takeaway from here is that in using Stream, must use immutable approach to leverage the parallelism of Streams */
        fibNumbers = IntStream.generate(new IntSupplier() {
            private int x = 0;
            private int y = 1;

            @Override
            public int getAsInt() {
                // updates instance variable for next supply.get
                int curr = x; // 1
                x = y;        // 2
                y = curr + y; // 1+2 = 3
                return curr;
            }
        })
        .limit(10)
        .mapToObj(String::valueOf) // from IntStream to Stream<String> bec the return of map is String
        .collect(Collectors.joining(", "));
        System.out.println(fibNumbers);
    }
}
