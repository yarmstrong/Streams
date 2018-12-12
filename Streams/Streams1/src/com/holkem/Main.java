package com.holkem;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// import static java.util.stream.Collectors.toList;

public class Main {

	public static void main(String[] args) {
		Trader raoul = new Trader("Raoul", "Cambridge");
		Trader mario = new Trader("Mario", "Milan");
		Trader alan = new Trader("Alan", "Cambridge");
		Trader brian = new Trader("Brian", "Cambridge");

		List<Transaction> transactions = Arrays.asList(
				new Transaction(brian, 2011, 300),
				new Transaction(raoul, 2012, 1000),
				new Transaction(raoul, 2011, 400),
				new Transaction(mario, 2012, 710),
				new Transaction(mario, 2012, 700),
				new Transaction(alan, 2012, 950));

		// #1: find all transactions in year 2011 and sort them by value ascending
		transactions.stream()
					.filter(t -> t.getYear() == 2011)
					.sorted(Comparator.comparing(Transaction::getValue))
					.forEach(System.out::println); // .collect(toList())
		System.out.println("#1: find all transactions in year 2011 and sort them by value ascending");
		System.out.println();
		
		// #2: unique cities of traders
		transactions.stream()
					.map(t -> t.getTrader().getCity())
					.distinct()
					.forEach(System.out::println); // distinct can be replaced
		System.out.println("#2: unique cities of traders");
		System.out.println();
		
		// #3: find all traders from cambridge and sort them by name
		transactions.stream()
					.map(Transaction::getTrader)
					.filter(t -> "Cambridge".equals(t.getCity())) // filter by city 1st before distinct to remove Milan entry right away
					.distinct()
					.sorted(Comparator.comparing(Trader::getName))
					.forEach(System.out::println);
		System.out.println("#3: find all traders from cambridge and sort them by name");
		System.out.println();
		
		// #4: unique names of traders asc
		String names = transactions.stream()
					.map(Transaction::getTrader)
					.distinct()// distinct checking is applied only for Trader obj not names bec 2 traders may have same name but in different city
					.map(Trader::getName)
					.sorted(Comparator.reverseOrder())
					// .forEach(System.out::println); WRONG
					// .reduce("", (str1, str2) -> str1 + str2); // inefficient bec new string every concat
					.collect(Collectors.joining(" "));
		System.out.println("#4: return a string of unique names of traders asc");
		System.out.println(names + "\n");
		
		// #5: anyMatch traders in milan
		boolean milanBased = transactions.stream()
					.anyMatch(t -> "Milan".equals(t.getTrader().getCity()));
		System.out.println("#5: anyMatch traders in milan");
		System.out.println("any trader in milan: " + milanBased + "\n");
		
		// #6: print sum amount of transactions in cambridge
		int i = transactions.stream()
					.filter(t -> "Cambridge".equals(t.getTrader().getCity()))
					.map(Transaction::getValue)
					.reduce(0,Integer::sum);
		System.out.println("#6: print sum amount of transactions in cambridge");
		System.out.println("cambridge total:" + i + "\n");
		
		// #7: highest value transaction
		i = transactions.stream()
					.map(Transaction::getValue)
					.reduce(0, Integer::max);
		System.out.println("#7: highest value transaction");
		System.out.println("max transaction:" + i + "\n");
		
		// #8: transaction with smallest value
		System.out.println("#8: transaction with smallest value");
		transactions.stream()
				// .reduce((t1, t2) -> t1.getValue() < t2.getValue() ? t1 : t2);
				.min(Comparator.comparing(Transaction::getValue)) // returns Optional<Transaction>
				.ifPresent(System.out::println);
	}

}
