package io.pokerapps;

import java.util.ArrayList;
import java.util.List;

public class Util {

	public static List<char[]> possibleCombinationsUsingBits() {
		int n = 5;
		int r = 3;

		int nCr = totalCombinations(n, r);

		char[] initialString = new char[n];
		List<char[]> combinationStrings = new ArrayList<char[]>();

		int roundNo = 0;
		initialString = formInitialString(n, r, ++roundNo);
		combinationStrings.add(initialString);

		char[] newCombination = initialString.clone();
		int i = n - r - 1;
		while (i < n) {
			newCombination = newCombination.clone();
			swap(newCombination, i + roundNo - 1, i + 1 + roundNo - 1);
			combinationStrings.add(newCombination);

			if (newCombination[n - 1] == '0') {
				if (combinationStrings.size() >= nCr)
					break;
				newCombination = formInitialString(n, r, ++roundNo);
				combinationStrings.add(newCombination);
				if (newCombination[n - 1] == '0')
					break;
				i = n - r - 2;
			}
			i++;
		}

		combinationStrings.forEach(ii -> System.out.println(String.valueOf(ii)));

		return combinationStrings;
	}

	private static int totalCombinations(int n, int r) {

		int nFact = 1;
		int rFact = 1;
		int nrFact = 1;

		for (int i = 1; i <= n; i++) {
			nFact = nFact * i;
		}

		for (int i = 1; i <= r; i++) {
			rFact = rFact * i;
		}

		for (int i = 1; i <= n - r; i++) {
			nrFact = nrFact * i;
		}

		return (nFact / (rFact * nrFact));
	}

	private static char[] formInitialString(int n, int r, int roundNo) {

		char[] initialString = new char[n];

		for (int i = 0; i < roundNo - 1; i++) {
			initialString[i] = '1';
		}

		for (int i = roundNo - 1; i < n - r + roundNo - 1; i++) {
			initialString[i] = '0';
		}

		for (int i = n - r + roundNo - 1; i < n; i++) {
			initialString[i] = '1';
		}

		return initialString;
	}

	private static void swap(char[] arr, int i, int j) {
		char temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

}
