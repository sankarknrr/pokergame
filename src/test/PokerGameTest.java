package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.pokerapps.*;

class PokerGameTest {

	private static List<Card> cards = new ArrayList<Card>();

	private static List<Suit> suits = Arrays.asList(Suit.values());
	private static int suitSize = suits.size();

	private static List<CardValue> cardValues = Arrays.asList(CardValue.values());
	private static int cardValueSize = cardValues.size();

	@BeforeAll
	static void setUp() {
		Card card1 = new Card(Suit.SPADES, CardValue.ACE);
		Card card2 = new Card(Suit.CLUBS, CardValue.EIGHT);
		Card card3 = new Card(Suit.SPADES, CardValue.ACE);
		Card card4 = new Card(Suit.HEARTS, CardValue.NINE);

		cards.add(0, card1);
		cards.add(1, card2);
		cards.add(2, card3);
		cards.add(3, card4);

		System.out.println(cards.get(0));
		System.out.println(cards.get(1));
		System.out.println(cards.get(2));
	}

	// if two cards have same Suit and CardValue, then they are same
	@Test
	void testTwoCardsAreEqual() {
		assertFalse(cards.get(0).equals(cards.get(1)));
		assertFalse(cards.get(1).equals(cards.get(2)));
		assertTrue(cards.get(0).equals(cards.get(2)));
	}

	@Test
	void testCompareTwoCards() {
		List<Card> copyCards = new ArrayList<Card>(cards);

		Collections.sort(copyCards);
		// System.out.println(cards);
		assertEquals(copyCards.get(0).getCardValue(), CardValue.EIGHT);
	}

	@Test
	void testPickACard() {

		Card pickedCard1 = new Card(suits.get(new Random().nextInt(suitSize)),
				cardValues.get(new Random().nextInt(cardValueSize)));

		System.out.println(pickedCard1);
	}

	@Test
	void testCreateDeck() {

		List<Card> shuffledDeck = PokerGame.createDeck();

		assertEquals(13, shuffledDeck.stream().filter(i -> i.getSuit().equals(Suit.HEARTS)).count());
		assertEquals(13, shuffledDeck.stream().filter(i -> i.getSuit().equals(Suit.SPADES)).count());
		assertEquals(13, shuffledDeck.stream().filter(i -> i.getSuit().equals(Suit.CLUBS)).count());
		assertEquals(13, shuffledDeck.stream().filter(i -> i.getSuit().equals(Suit.DIAMONDS)).count());

	}

	@Test
	void testCreatePlayerAndDealer() {

		List<Card> shuffledDeck = PokerGame.createDeck();
		List<Player> players = PokerGame.createPlayers(shuffledDeck);

		assertEquals(3, players.size());
		players.stream().filter(i -> i.isDealer() == true).forEach(i -> {
			assertTrue(i.getCards().size() == 5);
			System.out.println(i);
		});

		players.stream().filter(i -> i.isDealer() == false).forEach(i -> {
			assertTrue(i.getCards().size() == 2);
			System.out.println(i);
		});

	}

	List<Card> testDataPlayer(Card card1, Card card2) {

		List<Card> playerCards = new ArrayList<Card>();
		playerCards.add(card1);
		playerCards.add(card2);

		return playerCards;
	}

	List<Card> testData(String category) {

		List<Card> cardD = new ArrayList<Card>();

		cardD.add(new Card(Suit.SPADES, CardValue.NINE));

		if (category.equalsIgnoreCase("FourOfAKind")) {
			cardD.add(new Card(Suit.CLUBS, CardValue.ACE));
			cardD.add(new Card(Suit.HEARTS, CardValue.ACE));
			cardD.add(new Card(Suit.SPADES, CardValue.ACE));
			cardD.add(new Card(Suit.DIAMONDS, CardValue.JACK));
		}

		if (category.equalsIgnoreCase("FullHouse")) {
			cardD.add(new Card(Suit.CLUBS, CardValue.ACE));
			cardD.add(new Card(Suit.HEARTS, CardValue.ACE));
			cardD.add(new Card(Suit.SPADES, CardValue.NINE));
			cardD.add(new Card(Suit.DIAMONDS, CardValue.SIX));
		}

		if (category.equalsIgnoreCase("TwoPair")) {
			cardD.add(new Card(Suit.CLUBS, CardValue.ACE));
			cardD.add(new Card(Suit.SPADES, CardValue.SIX));
			cardD.add(new Card(Suit.DIAMONDS, CardValue.FIVE));
			cardD.add(new Card(Suit.DIAMONDS, CardValue.QUEEN));
		}

		if (category.equalsIgnoreCase("OnePair")) {
			cardD.add(new Card(Suit.CLUBS, CardValue.KING));
			cardD.add(new Card(Suit.SPADES, CardValue.FOUR));
			cardD.add(new Card(Suit.DIAMONDS, CardValue.FIVE));
			cardD.add(new Card(Suit.DIAMONDS, CardValue.QUEEN));
		}

		if (category.equalsIgnoreCase("SameSuit")) {
			cardD.add(new Card(Suit.CLUBS, CardValue.KING));
			cardD.add(new Card(Suit.CLUBS, CardValue.FOUR));
			cardD.add(new Card(Suit.CLUBS, CardValue.FIVE));
			cardD.add(new Card(Suit.CLUBS, CardValue.QUEEN));
		}

		if (category.equalsIgnoreCase("Sequence")) {
			cardD.add(new Card(Suit.CLUBS, CardValue.FIVE));
			cardD.add(new Card(Suit.SPADES, CardValue.JACK));
			cardD.add(new Card(Suit.DIAMONDS, CardValue.EIGHT));
			cardD.add(new Card(Suit.DIAMONDS, CardValue.SEVEN));
		}

		return cardD;
	}

	List<Player> testData1(String category) {

		List<Player> players = new ArrayList<Player>();
		players.add(new Player(
				testDataPlayer(new Card(Suit.CLUBS, CardValue.EIGHT), new Card(Suit.DIAMONDS, CardValue.ACE)), false));
		players.add(new Player(
				testDataPlayer(new Card(Suit.CLUBS, CardValue.NINE), new Card(Suit.CLUBS, CardValue.SIX)), false));
		players.add(new Player(testData(category), true));

		return players;
	}

	@Test
	void testPlayPokerFourOfAKind() {

		System.out.println("Playing four of kind hand");
		List<Card> cards = PokerGame.playPoker(testData1("FourOfAKind"));

		System.out.println("\n\n Winning Combination = " + cards);
		Map<CardValue, Long> m = RankByCategories.groupByCardValue(cards);

		assertTrue(m.values().removeIf(l -> l == 4));
	}

	@Test
	void testPlayPokerFullHouse() {

		System.out.println("Playing full house hand");
		List<Card> cards = PokerGame.playPoker(testData1("FullHouse"));

		System.out.println("\n\n Winning Combination = " + cards);
		Map<CardValue, Long> m = RankByCategories.groupByCardValue(cards);
		// one 3 of a kind
		m.values().removeIf(l -> l == 3);
		// one pair
		m.values().removeIf(l -> l == 2);

		assertEquals(0, m.keySet().size());
	}

	@Test
	void testPlayPokerTwoPair() {

		System.out.println("Playing two pair hand");
		List<Card> cards = PokerGame.playPoker(testData1("TwoPair"));

		System.out.println("\n\n Winning Combination = " + cards);
		Map<CardValue, Long> m = RankByCategories.groupByCardValue(cards);
		m.values().removeIf(l -> l < 2);

		assertEquals(2, m.keySet().size());
	}

	@Test
	void testPlayPokerOnePair() {

		System.out.println("Playing one pair hand");
		List<Card> cards = PokerGame.playPoker(testData1("OnePair"));

		System.out.println("\n\n Winning Combination = " + cards);
		Map<CardValue, Long> m = RankByCategories.groupByCardValue(cards);
		m.values().removeIf(l -> l < 2);

		assertEquals(1, m.keySet().size());

	}

	@Test
	void testPlayPokerFlush() {

		System.out.println("Playing flush or same suit hand");
		List<Card> cards = PokerGame.playPoker(testData1("SameSuit"));

		System.out.println("Winning Combination = " + cards);
		assertEquals(5, cards.stream().filter(c -> c.getSuit().equals(Suit.CLUBS)).count());
	}

	@Test
	void testPlayPokerSqeuence() {

		System.out.println("Playing straight hand");
		List<Card> cards = PokerGame.playPoker(testData1("Sequence"));

		System.out.println("Winning Combination = " + cards);
	}

	@Test
	void testnCr() {
		int n = 7;
		int r = 3;

		List<char[]> combinations = Util.nCr(n, r);

		combinations.forEach(System.out::println);
		assertEquals(Util.totalCombinations(n, r), combinations.size());
	}

}
