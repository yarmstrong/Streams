package com.holkem;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatMapStreamFromFiles {
    public static void main(String[] args) {

        Path file = Paths.get(".", "src/com/holkem", "Main.java");
        if (Files.notExists(file)) {
            System.out.println("File does not exist");
        }

        // DISTINCT WORDS IN THE FILE
        try (Stream<String> lines = Files.lines(file, Charset.defaultCharset())) {

            Stream<String[]> streamOfArraySplitWordsPerLine = lines
                    .map(line -> line.split(" "));
            Stream<Stream<String>> streamOfStreamSplitWordsPerLine = streamOfArraySplitWordsPerLine
                    .map(Arrays::stream);
            Stream<String> streamOfFlattenedStream = streamOfStreamSplitWordsPerLine
                    .flatMap(line -> line);

            long uniqueWords = streamOfFlattenedStream.distinct().count();

            System.out.println("# of unique words in " + file.getFileName() + " is: " + uniqueWords);

        } catch (IOException e) {
            System.out.println("Error opening file: "  + e.getMessage());
            e.printStackTrace();
        }

        // DISTINCT WORDS PER LINE IN THE FILE (to compare use of flatMap vs map only)

        // create a supplier so u get fresh stream to process
        Supplier<Stream<String>> supplier = () -> {
            Stream<String> s = null;
            try {
                s = Files.lines(file, Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        };

        /* try1: Stream<Long> to Stream<String> to String using Collectors.joining() */
        String distinctWordsPerLine = supplier.get().map(line -> line.split(" ")) // Stream<String[]>
                .map(line -> String.valueOf(Arrays.stream(line).distinct().count())) // Stream<Long> to Stream<String> right away
                // .map(String::valueOf) // Stream<String>
                .collect(Collectors.joining(", ")); // only able to do joining if Stream<String> thus the 2nd mapping
        System.out.println("# of distinct words per line in " + file.getFileName() + " is: " + distinctWordsPerLine);

        /* try2: Stream<Long> to LongStream to do math operation on it */
        long sumOfLong = supplier.get().map(line -> line.split(" ")) // Stream<String[]>
                .mapToLong(line -> Arrays.stream(line).distinct().count()) // map() returns Stream<Long>, but mapToLong returns LongStream
                .sum();
        System.out.println(sumOfLong);
    }
}

/*
HOW STREAM.FLATMAP() WORKS COMPARED TO STREAM.MAP()
IMPORTANT : USE FLATMAP WHEN THE RESULT OF LAMBDA EXPRESSION IS A STREAM
    AND U DONT WANT STREAM OF STREAM AS A RESULT BUT 1 STREAM ONLY
    THATS WHEN U FLATTEN IT
Stream<String[]> streamOfSplitWordsArrayPerLine =
    lines.map(line -> line.split(" "));
Stream<Stream<String>> streamOfStreamSplitWordsPerLine =
    lines.map(line -> Arrays.stream(line.split(" ")));
Stream<String> streamOfFlattenedStream =
    lines.flatMap(line -> Arrays.stream(line.split(" ")));
*/