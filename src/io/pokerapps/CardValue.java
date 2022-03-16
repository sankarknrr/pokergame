package io.pokerapps;

public enum CardValue {
	
	TWO(2), THREE(3), FOUR(4),
	FIVE(5), SIX(6), SEVEN(7),
	EIGHT(8), NINE(9), TEN(10),
	JACK(11), QUEEN(12), KING(13), ACE(14);
	
	CardValue(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getCardNumber() {
		return cardNumber;
	}

	private int cardNumber;
}

 