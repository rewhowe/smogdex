package com.example.smogdex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.util.Pair;

public class FuzzySearcher {

	private static final String TAG = FuzzySearcher.class.getSimpleName();

	private static class FirstElemComparator implements Comparator<Pair<Integer, Object>> {
		@Override
		public int compare(Pair<Integer, Object> lhs, Pair<Integer, Object> rhs) {
			return lhs.first - rhs.first;
		}
	}

	private static final FirstElemComparator PAIR_COMPARATOR = new FirstElemComparator();

	/**
	 * Quickly fuzzy searches an array of Objects using a given query. Results are left in-order.
	 * @param query - The case-insensitive query to search with. ".*" is placed between each character to allow for fuzzing.
	 * @param items - The array of Objects to search through - will not be mutated.
	 * @param result - Search results will be APPENDED to this List. The type MUST match the type of 'items' (not checked).
	 */
	public static void quickSearch(final String query, final Object items[], List result) {
		Pattern p = Pattern.compile(query.replace("", ".*"), Pattern.CASE_INSENSITIVE);
		for (Object item : items) {
			if (p.matcher(item.toString()).find()) {
				result.add(item);
			}
		}
	}

	/**
	 * Fuzzy searches an array of Objects using a given query and sorts the result. This is not much slower quickSearch().
	 * @param query - The case-insensitive query to search with. ".*" is placed between each character to allow for fuzzing.
	 * @param items - The array of Objects to search through - will not be mutated.
	 * @param sortedResult - Search results will be APPENDED to this List. The type MUST match the type of 'items' (not checked).
	 * @param locale - Used to convert Strings to lower case for string-matching.
	 */
	public static void sortedSearch(String query, List items, List sortedResult) {
		List<Pair<Integer, Object>> result = new ArrayList<Pair<Integer, Object>>();

		// subList(1, length+1) is used because splitting on "" returns leading and trailing ""
		List<String> splitQuery = Arrays.asList(query.toLowerCase(Locale.ENGLISH).split("")).subList(1, query.length() + 1);

		for (Object item : items) {
			String s = item.toString().toLowerCase(Locale.ENGLISH);
			Integer score = -1;
			int i = 0;
			int j;

			for (String q : splitQuery) {
				j = s.indexOf(q, i);
				if (j == -1) {
					score = null;
					break;
				}
				score += (j - i);
				i = j + 1;
			}

			if (score != null) {
				result.add(new Pair<Integer, Object>(score, item));
			}
		}

		Collections.sort(result, PAIR_COMPARATOR);
		for (Pair<Integer, Object> pair : result) {
			sortedResult.add(pair.second);
		}
	}

}
