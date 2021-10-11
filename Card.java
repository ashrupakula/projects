package cs3500.pyramidsolitaire.model.hw02;

import java.util.Objects;

public class Card
{
    private int cardValue;
    private String cardNumber;
    private char cardSuite;
    private boolean removed;

    public Card(int cardValue, String cardNumber, char cardSuite)
    {
        this.cardValue = cardValue;
        this.cardNumber = cardNumber;
        this.cardSuite = cardSuite;
        this.removed = false;
    }

    public int getCardValue() {
        return cardValue;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public char getCardSuite() {
        return cardSuite;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public String toString()
    {
        return cardNumber+cardSuite;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        else if(this.hashCode() == o.hashCode())
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardValue, cardNumber, cardSuite);
    }
}
