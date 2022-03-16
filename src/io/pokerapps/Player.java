package io.pokerapps;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private List<Card> cards;
	private boolean isDealer = false;

	public Player(List<Card> cards, boolean isDealer) {
		this.cards = cards;
		this.isDealer = isDealer;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
		

	public boolean isDealer() {
		return isDealer;
	}

	public void setDealer(boolean isDealer) {
		this.isDealer = isDealer;
	}

	@Override
	public String toString() {
		return "Player [cards=" + cards + "]";
	}
	
	
}
