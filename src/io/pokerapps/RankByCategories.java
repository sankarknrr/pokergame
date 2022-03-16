package io.pokerapps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
			rank = 4;

		// 3 of a kind
		Map<CardValue, Long> threesFilter = cards.stream()
				.collect(Collectors.groupingBy(Card::getCardValue, Collectors.counting()));

		cardThreeOfAKindDoesntExists = (threesFilter.values().removeIf(v -> v == 3) == true) ? false : true;
		if (cardAceFourOfAKindDoesntExists && !cardThreeOfAKindDoesntExists)
			rank = 3;

		// 2 of a kind
		cardPairDoesntExists = (threesFilter.values().removeIf(v -> v == 2) == true) ? false : true;
		if (cardAceFourOfAKindDoesntExists && !cardPairDoesntExists)
			rank = 2;
		
		// Full house
		if (!cardThreeOfAKindDoesntExists && !cardPairDoesntExists) {
			rank = 5;
		}

		return rank;
	}

}
