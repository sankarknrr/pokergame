package io.pokerapps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RankByCategories implements Comparator<List<Card>> {

	@Override
	public int compare(List<Card> cardList1, List<Card> cardList2) {

		int cardList1Rank = getRankByCategories(cardList1);
		int cardList2Rank = getRankByCategories(cardList2);

		if (cardList2Rank > cardList1Rank) {
			return 1;
		} else if (cardList2Rank < cardList1Rank) {
			return -1;
		}

		for (int i = 0; i < cardList1.size(); i++) {
			int c = cardList2.get(i).compareTo(cardList1.get(i));
			if (c != 0) {
				return c;
			}
		}

		return Integer.compare(cardList1.size(), cardList2.size());
	}

	private int getRankByCategories(List<Card> cards) {

		boolean cardThreeOfAKindDoesntExists = true;
		boolean cardPairDoesntExists = true;
		boolean cardAceFourOfAKindDoesntExists = true;
		int rank = 0;

		// Ace 4 of a kind
		Map<CardValue, Long> aceFilter = cards.stream().filter(c -> c.getCardValue().equals(CardValue.ACE))
				.collect(Collectors.groupingBy(Card::getCardValue, Collectors.counting()));

		cardAceFourOfAKindDoesntExists = (aceFilter.values().removeIf(v -> v == 4) == true) ? false : true;
		if (!cardAceFourOfAKindDoesntExists)
			rank = 8;

		// Flush
		Set<Suit> flushFilter = cards.stream().collect(
				Collectors.collectingAndThen(Collectors.groupingBy(Card::getSuit, Collectors.counting()), m -> {
					m.values().removeIf(l -> l < 5);
					return m.keySet();
				}));
		if (flushFilter.size() > 0) {
			if (rank < 6)
				rank = 6;
		}

		// 3 of a kind
		Map<CardValue, Long> threesFilter = groupByCardValue(cards);
		cardThreeOfAKindDoesntExists = (threesFilter.values().removeIf(v -> v == 3) == true) ? false : true;
		if (cardAceFourOfAKindDoesntExists && !cardThreeOfAKindDoesntExists)
			if (rank < 4)
				rank = 4;

		// 2 of a kind
		cardPairDoesntExists = (threesFilter.values().removeIf(v -> v == 2) == true) ? false : true;
		if (cardAceFourOfAKindDoesntExists && !cardPairDoesntExists)
			if (rank < 2)
				rank = 2;

		// Full house or two pair
		if (!cardThreeOfAKindDoesntExists && !cardPairDoesntExists) {
			if (rank < 7)
				rank = 7;
		} else {
			// Another 2 of a kind
			if (cards.stream().collect(Collectors
					.collectingAndThen(Collectors.groupingBy(Card::getCardValue, Collectors.counting()), m -> {
						m.values().removeIf(l -> l < 2);
						return m.keySet();
					})).size() > 1) {
				if (rank < 3)
					rank = 3;
			}
		}
		
	return rank;

	}

	// TODO
	private int furtherRankIfTheyAreEqual(List<Card> cards1, List<Card> cards2, int currentRank) {
		int rank = 0;

		Set<CardValue> moreThanOneOfAKindFilter = cards1.stream().collect(
				Collectors.collectingAndThen(Collectors.groupingBy(Card::getCardValue, Collectors.counting()), m -> {
					m.values().removeIf(l -> l < 2);
					return m.keySet();
				}));

		moreThanOneOfAKindFilter.forEach(System.out::println);

		return rank;
	}

	public static Map<CardValue, Long> groupByCardValue(List<Card> cards) {
		return cards.stream().collect(Collectors.groupingBy(Card::getCardValue, Collectors.counting()));
	}

	public static Map<Suit, Long> groupBySuit(List<Card> cards) {
		return cards.stream().collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));
	}

}
