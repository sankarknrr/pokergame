package io.pokerapps;

public class Card implements Comparable<Card>{
	private Suit suit;
	private CardValue cardValue;

	public Card(Suit suit, CardValue cardValue) {
		this.suit = suit;
		this.cardValue = cardValue;
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	public CardValue getCardValue() {
		return cardValue;
	}

	public void setCardValue(CardValue cardValue) {
		this.cardValue = cardValue;
	}

	@Override
	public String toString() {
		return "Card[" + cardValue + " " + suit + "]";
	}
	
	@Override
	public boolean equals(Object o) {
	    if (!(o instanceof Card))
	        return false;
	    
	    Card otherCard = (Card) o;
	    return otherCard.suit.equals(suit) && otherCard.cardValue.equals(cardValue);
	}

	@Override
	public int compareTo(Card otherCard) {		
		return Integer.compare(getCardValue().getCardNumber(), otherCard.getCardValue().getCardNumber());		
	}
	
	
}
