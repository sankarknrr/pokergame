package io.pokerapps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class PokerGame {

	private static List<Suit> suits = Arrays.asList(Suit.values());
	private static int suitSize = suits.size();

	private static List<CardValue> cardValues = Arrays.asList(CardValue.values());
	private static int cardValueSize = cardValues.size();

	private static final int noOfPlayers = 2;
	private static final int noOfCardPoker = 5;
	private static int[] dealtCardsIndex = new int[(noOfPlayers * 2) + noOfCardPoker];

	public static void main(String[] args) {

		List<Card> deck = createDeck();
		List<Player> players = createPlayers(deck);
	}

	public static List<Card> createDeck() {

		Card[] shuffledDeck = new Card[52];

		for (int i = 0; i < suitSize; i++) {
			for (int j = 0; j < cardValueSize; j++) {
				Card newCard = new Card(suits.get(i), cardValues.get(j));
				int newPosition = new Random().nextInt(52);

				while (shuffledDeck[newPosition] != null) {
					newPosition = new Random().nextInt(52);
				}

				shuffledDeck[newPosition] = newCard;
			}
		}
		return Arrays.asList(shuffledDeck);
	}

	public static List<Player> createPlayers(List<Card> deck) {

		List<Player> players = new ArrayList<Player>(noOfPlayers);
		Arrays.fill(dealtCardsIndex, -1);

		for (int i = 0; i < noOfPlayers; i++) {
			players.add(createPlayer(deck, 2, i * 2, false));
		}

		players.add(createDealer(deck));
		return players;
	}

	public static Player createDealer(List<Card> deck) {

		return createPlayer(deck, noOfCardPoker, noOfPlayers * 2, true);
	}

	public static Player createPlayer(List<Card> deck, int noOfCards, int k, boolean isDealer) {

		List<Card> cards = new ArrayList<Card>(noOfCards);

		for (int j = 0; j < noOfCards; j++) {
			int takeACardFromDeckIndex = new Random().nextInt(52);
			while (ifExists(dealtCardsIndex, takeACardFromDeckIndex)) {
				takeACardFromDeckIndex = new Random().nextInt(52);
			}
			dealtCardsIndex[k++] = takeACardFromDeckIndex;
			cards.add(deck.get(takeACardFromDeckIndex));
		}

		return new Player(cards, isDealer);
	}

	public static boolean ifExists(int[] dealtCardsIndex, int i) {

		for (int j = 0; j < dealtCardsIndex.length; j++) {
			if (dealtCardsIndex[j] != -1 && dealtCardsIndex[j] == i) {
				return true;
			}
		}

		return false;
	}

	public static List<Card> playPoker(List<Player> players) {

		List<char[]> combinations = Util.possibleCombinationsUsingBits();

		List<List<Card>> player1CardCombinations = new ArrayList<List<Card>>();
		List<List<Card>> player2CardCombinations = new ArrayList<List<Card>>();

		List<Card> dealerCards = null;
		List<Card> player1Cards = null;
		List<Card> player2Cards = null;
		List<Card> tempCard1 = new ArrayList<Card>();
		List<Card> tempCard2 = new ArrayList<Card>();

		int i = 0;
		for (Player p : players) {
			if (p.isDealer() == false) {
				if (i == 0) {
					player1Cards = p.getCards();
					i++;
				} else {
					player2Cards = p.getCards();
				}
			} else {
				dealerCards = p.getCards();
			}
		}

		for (char[] c : combinations) {
			tempCard1.clear();
			tempCard2.clear();
			tempCard1.addAll(player1Cards);
			tempCard2.addAll(player2Cards);
			for (int j = 0; j < c.length; j++) {
				if (c[j] == '1') {
					tempCard1.add(dealerCards.get(j));
					tempCard2.add(dealerCards.get(j));
				}
			}
			player1CardCombinations.add(new ArrayList<Card>(tempCard1));
			player2CardCombinations.add(new ArrayList<Card>(tempCard2));
		}

		rankPlayerCardsByCategories(player1CardCombinations, 1);
		rankPlayerCardsByCategories(player2CardCombinations, 2);

		return findWinner(player1CardCombinations.stream().findFirst().get(),
				player2CardCombinations.stream().findFirst().get());

	}

	public static void rankPlayerCardsByCategories(List<List<Card>> playerCardCombinations, int i) {

		System.out.println("RANKING PLAYER...." + i);
		playerCardCombinations.forEach(c -> {
			Collections.sort(c);
		});

		Collections.sort(playerCardCombinations, new RankByCategories());

		System.out.println("Player " + i + " card combinations..after sorting by rank categories");
		playerCardCombinations.forEach(c -> System.out.println(c));
	}

	public static List<Card> findWinner(List<Card> player1BestCombination, List<Card> player2BestCombination) {

		List<List<Card>> winningCombinations = new ArrayList<List<Card>>();

		winningCombinations.add(player1BestCombination);
		winningCombinations.add(player2BestCombination);

		rankPlayerCardsByCategories(winningCombinations, 3);
		System.out.println("\n\n\nWinning Combination = " + winningCombinations.stream().findFirst().get());

		return winningCombinations.stream().findFirst().get();
	}
}
