package com.holkem;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatMapHelloWorld {
    public static void main(String[] args) {
        String hello = "Hello";
        String[] split = hello.split("");
        System.out.println(Arrays.asList(split)); // [H, e, l, l, o]

        List<String> words = Arrays.asList("Hello", "World");

        List<String[]> map1 = words.stream()
                .map(word -> word.split(""))
                .distinct()
                .collect(Collectors.toList());
        map1.forEach(arr -> System.out.println(Arrays.asList(arr)));
        /* List<String> => stream      => Stream<String>
                        => map split[] => Stream<String[]>
                        => collect     => List<String[]>
        List<String[]> : [H, e, l, l, o], [W, o, r, l, d] */

        List<Stream<String>> map2 = words.stream()
                .map(word -> word.split(""))
                .map(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
        map2.forEach(stream -> {
            String str = stream.collect(Collectors.joining());
            System.out.println(str);
        });
        /* List<String>
                => stream            => Stream<String>           => Hello
                => map split[]       => Stream<String[]>         => [H, e, l, l, o]
                => map Arrays.stream => Stream< Stream<String> > => H e l l o, W o r l d (removed them from array into stream which u can combine together via collect)
                       bec Arrays.stream(T[]) returns Stream<T>
                           Arrays.asList(T[]) return List<T>
                => collect           => List<Stream<String>>     => List: stream1 stream2 for collect
        When list looped and stream collected, they just returned to String so nonsense */

        String map3 = words.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.joining());
        System.out.println(map3);
        /* List<String>
                => stream                => Stream<String>       => Hello
                => map split[]           => Stream<String[]>     => [H, e, l, l, o]
                => flatMap Arrays.stream => Stream<String>       => H e l l o W o r l d
                     Arrays.stream returns Stream<T>, so u have stream1, stream2
                     but flatMap  combines these 2 stream and return it as T (contents of the mapped stream)
                => collect               => List<String> => List: stream1 stream2 for collect
        */
    }
}
