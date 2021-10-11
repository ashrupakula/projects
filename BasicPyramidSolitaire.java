package cs3500.pyramidsolitaire.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BasicPyramidSolitaire implements PyramidSolitaireModel<Card>
{
    // private field to hold the pyramid
    private List< List<Card> > cardPyramid;
    private List<Card> stockCards;
    private List<Card> drawCards;
    private List<Card> deck;
    private int numRows;
    private int numDraw;
    private int totalPyramidCards;
    private int gameState = 0;  // Game not started

    public BasicPyramidSolitaire()
    {
        gameState = 0;
        numRows = 0;
        numDraw = 0;
        totalPyramidCards = 0;
        deck = getDeck();
    }

    /**
     * Return a valid and complete deck of cards for a game of Pyramid Solitaire.
     * There is no restriction imposed on the ordering of these cards in the deck.
     * The validity of the deck is determined by the rules of the specific game in
     * the classes implementing this interface.
     *
     * @return the deck of cards as a list
     */
    @Override
    public List<Card> getDeck() {
        if(gameState == 0) {
            deck = new ArrayList<>();

            // Changes the logic in assignment 4
            char[] suites = {'♣', '♦', '♥', '♠'};
            String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

            for (int i = 0; i < suites.length; i++) {
                for (int j = 1; j <= 13; j++) {
                    Card c = new Card(j, values[j-1], suites[i]);;
                    deck.add(c);
                }
            }
        }
        return deck;
    }

    /**
     * <p>Deal a new game of Pyramid Solitaire.
     * The cards to be used and their order are specified by the the given deck,
     * unless the {@code shuffle} parameter indicates the order should be ignored.</p>
     *
     * <p>This method first verifies that the deck is valid. It deals cards in rows
     * (left-to-right, top-to-bottom) into the characteristic pyramid shape
     * with the specified number of rows, followed by the specified number of
     * draw cards. When {@code shuffle} is {@code false}, the 0th card in {@code deck}
     * is used as the first card dealt.</p>
     *
     * <p>This method should have no other side effects, and should work for any valid arguments.</p>
     *
     * @param deck    the deck to be dealt
     * @param shuffle if {@code false}, use the order as given by {@code deck},
     *                otherwise use a randomly shuffled order
     * @param numRows number of rows in the pyramid
     * @param numDraw number of draw cards available at a time
     * @throws IllegalArgumentException if the deck is null or invalid,
     *                                  the number of pyramid rows is non-positive,
     *                                  the number of draw cards available at a time is negative,
     *                                  or a full pyramid and draw pile cannot be dealt with the number of given cards in deck
     */
    @Override
    public void startGame(List<Card> deck, boolean shuffle, int numRows, int numDraw) throws IllegalArgumentException {
        // Length Check
        if(deck == null)
            throw new IllegalArgumentException("Deck not initialized!");
        if(deck.size() != 52)
            throw new IllegalArgumentException("Deck size incorrect!");
        if(numRows <= 0)
            throw new IllegalArgumentException("Number of rows must be more than zero!");
        if(numDraw < 0)
            throw new IllegalArgumentException("Number of draw cards must be zero or more!");

        // Changed the duplicate detection logic in Assignment 4
        boolean illegal = false;
        // Check if deck has duplicate cards

        char[] suites = {'♣', '♦', '♥', '♠'};
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for (int i = 0; i < suites.length; i++) {
            for (int j = 1; j <= 13; j++) {
                String s = values[j-1]+suites[i];

                int count = 0;
                for(int k = 0; k < deck.size(); k++)
                {
                    String s2 = deck.get(k).toString();
                    if(s.equals(s2))
                        count++;
                }
                if(count != 1) {
                    illegal = true;
                    break;
                }
            }
            if(illegal)
                break;
        }

        if(illegal)
            throw new IllegalArgumentException("Deck is illegal");

        totalPyramidCards = numRows * (numRows + 1) / 2;

        // Changed in assignment 4
        if(deck.size() < totalPyramidCards + numDraw)
            throw new IllegalArgumentException("Number of rows and draw cards is too large for the given deck size!");

        // Make new copy of deck
        this.deck = new ArrayList<>();
        for(Card c : deck) {
            Card newCard = new Card(c.getCardValue(), c.getCardNumber(), c.getCardSuite());
            this.deck.add(newCard);
        }

        // Assign instance variable
        this.numRows = numRows;
        this.numDraw = numDraw;

        // Shuffle deck if needed
        if(shuffle)
            Collections.shuffle(this.deck);

        cardPyramid = new ArrayList< List<Card> >();

        int k = 0;
        for(int i = 0; i < numRows; i++)
        {
            cardPyramid.add(new ArrayList<Card>());
            for(int j = 0; j <= i; j++)
            {
                Card c = new Card(this.deck.get(k).getCardValue(), this.deck.get(k).getCardNumber(), this.deck.get(k).getCardSuite());
                cardPyramid.get(i).add(c);
                k++;
            }
        }

        stockCards = new ArrayList<>();
        drawCards = new ArrayList<>();

        // Add remaining cards to stockCards
        for(int i = k; i < this.deck.size(); i++)
        {
            Card c = new Card(this.deck.get(i).getCardValue(), this.deck.get(i).getCardNumber(), this.deck.get(i).getCardSuite());
            stockCards.add(c);
        }

        // Add cards only if there is anything in the stock for assignment 3
        if(stockCards.size() > 0) {
            // Add cards to exposed drawCards
            // Added condition in for loop to stop when stock is finished
            for (int i = 0; i < numDraw && stockCards.size() > 0; i++) {
                Card c = new Card(stockCards.get(0).getCardValue(), stockCards.get(0).getCardNumber(), stockCards.get(0).getCardSuite());
                drawCards.add(c);
                stockCards.remove(0);
            }
        }

        // Game has started
        gameState = 1;
    }

    // Checks if the card to be removed from the pyramid is valid or not
    private boolean isCardToRemoveValid(int row, int card)
    {
        // Added OR condition for assignment 3
        if(row >= getNumRows() || row < 0)
            return false;

        // Added OR condition for assignment 3
        if(card >= cardPyramid.get(row).size() || card < 0)
            return false;

        if(cardPyramid.get(row).get(card).isRemoved())
            return false;

        // Added uncovered check for assignment 4
        if(!isCardUncovered(row, card))
            return false;

        return true;
    }

    // Added method for checking if the card is uncovered or not
    private boolean isCardUncovered(int row, int card)
    {
        // Card is uncovered when it is in the last row
        if(row == numRows-1)
            return true;

        // Card is uncovered when the 2 cards below it are removed
        if(cardPyramid.get(row+1).get(card).isRemoved() && cardPyramid.get(row+1).get(card+1).isRemoved())
            return true;

        return false;
    }

    /**
     * Remove two exposed cards on the pyramid, using the two specified card positions.
     *
     * @param row1  row of first card position, numbered from 0 from the top of the pyramid
     * @param card1 card of first card position, numbered from 0 from left
     * @param row2  row of second card position
     * @param card2 card of second card position
     * @throws IllegalArgumentException if the attempted remove is invalid
     * @throws IllegalStateException    if the game has not yet been started
     */
    @Override
    public void remove(int row1, int card1, int row2, int card2) throws IllegalArgumentException, IllegalStateException {
        if(gameState == 0)
            throw new IllegalStateException("Game not yet started");

        if(!isCardToRemoveValid(row1, card1))
            throw new IllegalArgumentException("Requested first card is not valid");

        if(!isCardToRemoveValid(row2, card2))
            throw new IllegalArgumentException("Requested second card is not valid");

        if(cardPyramid.get(row1).get(card1).getCardValue() + cardPyramid.get(row2).get(card2).getCardValue() != 13)
            throw new IllegalArgumentException("Requested cards are not of value 13");

        cardPyramid.get(row1).get(card1).setRemoved(true);
        cardPyramid.get(row2).get(card2).setRemoved(true);
    }

    /**
     * Remove a single card on the pyramid, using the specified card position.
     *
     * @param row  row of the desired card position, numbered from 0 from the top of the pyramid
     * @param card card of the desired card position, numbered from 0 from left
     * @throws IllegalArgumentException if the attempted remove is invalid
     * @throws IllegalStateException    if the game has not yet been started
     */
    @Override
    public void remove(int row, int card) throws IllegalArgumentException, IllegalStateException {
        if(gameState == 0)
            throw new IllegalStateException("Game not yet started");

        if(!isCardToRemoveValid(row, card))
            throw new IllegalArgumentException("Requested card is not valid");

        if(cardPyramid.get(row).get(card).getCardValue() != 13)
            throw new IllegalArgumentException("Requested card is not of value 13");

        cardPyramid.get(row).get(card).setRemoved(true);
    }

    /**
     * Remove two cards, one from the draw pile and one from the pyramid.
     *
     * @param drawIndex the card from the draw pile, numbered from 0 from left
     * @param row       row of the desired card position, numbered from 0 from the top of the pyramid
     * @param card      card of the desired card position, numbered from 0 from left
     * @throws IllegalArgumentException if the attempted remove is invalid
     * @throws IllegalStateException    if the game has not yet been started
     */
    @Override
    public void removeUsingDraw(int drawIndex, int row, int card) throws IllegalArgumentException, IllegalStateException {
        if(gameState == 0)
            throw new IllegalStateException("Game not yet started");

        if(!isCardToRemoveValid(row, card))
            throw new IllegalArgumentException("Requested card is not valid");

        // Added If Condition for assignment 3
        if(drawIndex >= drawCards.size() || drawIndex < 0)
            throw new IllegalArgumentException("Requested draw card is invalid");

        if(cardPyramid.get(row).get(card).getCardValue() + drawCards.get(drawIndex).getCardValue() != 13)
            throw new IllegalArgumentException("Requested cards are not of value 13");

        cardPyramid.get(row).get(card).setRemoved(true);
        discardDraw(drawIndex);
    }

    /**
     * Discards an individual card from the draw pile.
     *
     * @param drawIndex the card from the draw pile to be discarded
     * @throws IllegalArgumentException if the index is invalid or no card is present there.
     * @throws IllegalStateException    if the game has not yet been started
     */
    @Override
    public void discardDraw(int drawIndex) throws IllegalArgumentException, IllegalStateException {
        if(gameState == 0)
            throw new IllegalStateException("Game not yet started");

        // Added OR Condition for assignment 3
        if(drawIndex >= drawCards.size() || drawIndex < 0)
            throw new IllegalArgumentException("Requested draw card is invalid");

        drawCards.get(drawIndex).setRemoved(true);
        drawCards.remove(drawIndex);

        // Add a card from stockpile?
        if(stockCards.size() > 0)
        {
            Card c = new Card(stockCards.get(0).getCardValue(), stockCards.get(0).getCardNumber(), stockCards.get(0).getCardSuite());
            drawCards.add(c);
            stockCards.remove(0);
        }
    }

    /**
     * Returns the number of rows originally in the pyramid, or -1 if the game hasn't been started.
     *
     * @return the height of the pyramid, or -1
     */
    @Override
    public int getNumRows() {
        if(gameState != 0)
            return cardPyramid.size();
        else
            return -1;
    }

    /**
     * Returns the maximum number of visible cards in the draw pile,
     * or -1 if the game hasn't been started.
     *
     * @return the number of visible cards in the draw pile, or -1
     */
    @Override
    public int getNumDraw() {
        if(gameState != 0)
            return numDraw;
        else
            return -1;
    }

    /**
     * Returns the width of the requested row, measured from
     * the leftmost card to the rightmost card (inclusive) as the game is initially dealt.
     *
     * @param row the desired row (0-indexed)
     * @return the number of spaces needed to deal out that row
     * @throws IllegalArgumentException if the row is invalid
     * @throws IllegalStateException    if the game has not yet been started
     */
    @Override
    public int getRowWidth(int row) throws IllegalArgumentException, IllegalStateException {
        if(gameState != 0) {
            if(row < 0 || row >= numRows)
                throw new IllegalArgumentException("Row index invalid");
            else
                return cardPyramid.get(row).size();
        }
        else
            throw new IllegalStateException("Game not yet started");
    }

    /**
     * Signal if the game is over or not. A game is said to be over if there are no possible removes
     * or discards.
     *
     * @return true if game is over, false otherwise
     * @throws IllegalStateException if the game hasn't been started yet
     */
    @Override
    public boolean isGameOver() throws IllegalStateException {
        // Check if game is not started yet
        if(gameState == 0)
            return true;

        // Check if pyramid is all empty
        int removedCards = 0;
        for(int i = 0; i < numRows; i++)
        {
            for(int j = 0; j < i+1; j++)
            {
                if(cardPyramid.get(i).get(j).isRemoved())
                    removedCards++;
            }
        }

        if(removedCards == totalPyramidCards)
        {
            gameState = 2;  // Game is over and user is victorious
            return true;
        }


        boolean noMoreDiscards = false, noMoreRemoves = true;

        if(drawCards.size() == 0)
            noMoreDiscards = true;

        List<Card> exposedCards = new ArrayList<>();

        for(int i = 0; i < numRows; i++)
        {
            for(int j = 0; j < i+1; j++)
            {
                if(!cardPyramid.get(i).get(j).isRemoved())
                {
                    if (i == numRows - 1) {
                        Card c = new Card(cardPyramid.get(i).get(j).getCardValue(), cardPyramid.get(i).get(j).getCardNumber(), cardPyramid.get(i).get(j).getCardSuite());
                        exposedCards.add(c);
                    } else if (cardPyramid.get(i + 1).get(j).isRemoved() && cardPyramid.get(i + 1).get(j + 1).isRemoved()) {
                        Card c = new Card(cardPyramid.get(i).get(j).getCardValue(), cardPyramid.get(i).get(j).getCardNumber(), cardPyramid.get(i).get(j).getCardSuite());
                        exposedCards.add(c);
                    }
                }
            }
        }

        // Check if moves are possible with pyramid exposed cards alone
        for(int i = 0; i < exposedCards.size(); i++)
        {
            for(int j = i+1; j < exposedCards.size(); j++)
            {
                if(exposedCards.get(i).getCardValue() + exposedCards.get(j).getCardValue() == 13)
                {
                    noMoreRemoves = false;
                    break;
                }
            }

            if(!noMoreRemoves)
                break;
        }

        // Check if moves are possible with draw cards and pyramid exposed cards
        for(int i = 0; i < drawCards.size(); i++)
        {
            for(int j = i+1; j < exposedCards.size(); j++)
            {
                if(drawCards.get(i).getCardValue() + exposedCards.get(j).getCardValue() == 13)
                {
                    noMoreRemoves = false;
                    break;
                }
            }

            if(!noMoreRemoves)
                break;
        }

        if(noMoreDiscards && noMoreRemoves)
        {
            gameState = 3;  // Game is over and pyramid is left
            return true;
        }
        else
            return false;
    }

    /**
     * Return the current score, which is the sum of the values of the cards
     * remaining in the pyramid.
     *
     * @return the score
     * @throws IllegalStateException if the game hasn't been started yet
     */
    @Override
    public int getScore() throws IllegalStateException {
        if(gameState != 0) {
            int sum = 0;
            for(int i = 0; i < numRows; i++)
            {
                for(int j = 0; j <= i; j++)
                {
                    if(!cardPyramid.get(i).get(j).isRemoved())
                        sum += cardPyramid.get(i).get(j).getCardValue();
                }
            }
            return sum;
        }
        else
            throw new IllegalStateException("Game not yet started");
    }

    /**
     * Returns the card at the specified coordinates.
     *
     * @param row  row of the desired card (0-indexed from the top)
     * @param card column of the desired card (0-indexed from the left)
     * @return the card at the given position, or <code>null</code> if no card is there
     * @throws IllegalArgumentException if the coordinates are invalid
     * @throws IllegalStateException    if the game hasn't been started yet
     */
    @Override
    public Card getCardAt(int row, int card) throws IllegalArgumentException, IllegalStateException {
        if(gameState != 0) {
            if(row < 0 || row >= numRows)
                throw new IllegalArgumentException("Row index invalid");
            else {
                if(card < 0 || card >= cardPyramid.get(row).size())
                    throw new IllegalArgumentException("Card index invalid");
                else
                {
                    if(cardPyramid.get(row).get(card).isRemoved())
                        return null;
                    else
                        return cardPyramid.get(row).get(card);
                }
            }
        }
        else
            throw new IllegalStateException("Game not yet started");
    }

    /**
     * Returns the currently available draw cards.
     * There should be at most {@link PyramidSolitaireModel#getNumDraw} cards (the number
     * specified when the game started) -- there may be fewer, if cards have been removed.
     *
     * @return the ordered list of available draw cards
     * @throws IllegalStateException if the game hasn't been started yet
     */
    @Override
    public List<Card> getDrawCards() throws IllegalStateException {
        if(gameState != 0) {
            return drawCards;
        }
        else
            throw new IllegalStateException("Game not yet started");
    }

    public String toString()
    {
        boolean b = isGameOver();

        if(gameState == 0)
            return "";

        if(gameState == 2)
            return "You win!";

        if(gameState == 3)
        {
            int scoreInt = getScore();
            String scoreStr = String.valueOf(scoreInt);
            if(scoreStr.length() == 1)
                scoreStr = "0"+scoreStr;
            return "Game over. Score: "+scoreStr;
        }

        StringBuilder s = new StringBuilder();
        for(int i = 0; i < numRows; i++)
        {
            for(int j = 1; j <= (numRows-i-1); j++)
            {
                s.append("  ");
            }
            for(int j = 0; j <= i; j++)
            {
                if(!cardPyramid.get(i).get(j).isRemoved())
                    s.append(cardPyramid.get(i).get(j));
                else if(cardPyramid.get(i).get(j).isRemoved() && cardPyramid.get(i).get(j).getCardValue() != 10 && j < i)
                    s.append(". ");
                else if(cardPyramid.get(i).get(j).isRemoved() && cardPyramid.get(i).get(j).getCardValue() == 10 && j < i)
                    s.append(".  ");
                else if(cardPyramid.get(i).get(j).isRemoved() && j == i)
                    s.append(".");



                if(cardPyramid.get(i).get(j).getCardValue() != 10 && j < i)
                    s.append("  ");
                else if(j < i)
                    s.append(" ");
            }
            s.append("\n");
        }
        s.append("Draw: ");
        for(int i = 0; i < drawCards.size(); i++)
        {
            if(i < drawCards.size()-1)
                s.append(drawCards.get(i)).append(", ");
            else
                s.append(drawCards.get(i));
        }

        return s.toString();
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
        return Objects.hash(cardPyramid, stockCards, drawCards, deck, numRows, numDraw, gameState);
    }
}
