package cs3500.pyramidsolitaire.controller;

import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.view.PyramidSolitaireTextualView;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class PyramidSolitaireTextualController implements PyramidSolitaireController
{
    private Readable rd;
    private Appendable ap;

    public PyramidSolitaireTextualController(Readable rd, Appendable ap) throws IllegalArgumentException
    {
        if(rd == null)
            throw new IllegalArgumentException("Readable is null");

        if(ap == null)
            throw new IllegalArgumentException("Appendable is null");

        this.rd = rd;
        this.ap = ap;
    }

    /**
     * The primary method for beginning and playing a game.
     *
     * @param model   The game of solitaire to be played
     * @param deck    The deck of cards to be used
     * @param shuffle Whether to shuffle the deck or not
     * @param numRows How many rows should be in the pyramid
     * @param numDraw How many draw cards should be visible
     * @throws IllegalArgumentException if the model or deck is null
     * @throws IllegalStateException    if the game cannot be started,
     *                                  or if the controller cannot interact with the player.
     */
    @Override
    public <K> void playGame(PyramidSolitaireModel<K> model, List<K> deck, boolean shuffle, int numRows, int numDraw)
            throws IllegalArgumentException, IllegalStateException
    {
        if(model == null)
            throw new IllegalArgumentException("Model is null");

        if(deck == null)
            throw new IllegalArgumentException("Deck is null");

        // TODO: How to implement the IllegalStateException checks?

        // TODO: How to check if Readable fails?

        try {
            model.startGame(deck, shuffle, numRows, numDraw);
        }
        catch(IllegalArgumentException e)
        {
            // ap.append("Invalid move. Play again. *"+e.getMessage()+"*");
            throw new IllegalStateException(e.getMessage());
        }

        PyramidSolitaireTextualView view = new PyramidSolitaireTextualView(model, ap);
        Scanner sc = new Scanner(rd);

        // TODO: Is the try-catch correct?
        try {
            /*
            view.render();

            ap.append("\n");

            int score = model.getScore();
            ap.append("Score: "+score+"\n");

             */

            String token = "";



            while(sc.hasNext()) {
                view.render();
                ap.append("\n");

                // TODO: Change the append for try-catch of getScore

                try {
                    int score = model.getScore();
                    ap.append("Score: " + score + "\n");
                }
                catch(IllegalStateException e)
                {
                    ap.append("Invalid move. Play again. *"+e.getMessage()+"*\n");
                }

                // What if this move is invalid too??
                String move = sc.next();

                // ap.append(move+"\n");

                if (move.equals("rm1")) {

                    token = readNextValidInput(sc);
                    if(token.equals("") || token.equals("q") || token.equals("Q"))
                        break;
                    int row = Integer.parseInt(token);
                    //int row = sc.nextInt();

                    token = readNextValidInput(sc);
                    if(token.equals("") || token.equals("q") || token.equals("Q"))
                        break;
                    int card = Integer.parseInt(token);

                    try {
                        model.remove(row - 1, card - 1);
                    }
                    catch(IllegalArgumentException|IllegalStateException e)
                    {
                        ap.append("Invalid move. Play again. *"+e.getMessage()+"*\n");
                    }
                } else if (move.equals("rm2")) {
                    /*int row1 = sc.nextInt();
                    int card1 = sc.nextInt();
                    int row2 = sc.nextInt();
                    int card2 = sc.nextInt();*/

                    token = readNextValidInput(sc);
                    if(token.equals("") || token.equals("q") || token.equals("Q"))
                        break;
                    int row1 = Integer.parseInt(token);

                    token = readNextValidInput(sc);
                    if(token.equals("") || token.equals("q") || token.equals("Q"))
                        break;
                    int card1 = Integer.parseInt(token);

                    token = readNextValidInput(sc);
                    if(token.equals("") || token.equals("q") || token.equals("Q"))
                        break;
                    int row2 = Integer.parseInt(token);

                    token = readNextValidInput(sc);
                    if(token.equals("") || token.equals("q") || token.equals("Q"))
                        break;
                    int card2 = Integer.parseInt(token);

                    try {
                        model.remove(row1 - 1, card1 - 1, row2 - 1, card2 - 1);
                    }
                    catch(IllegalArgumentException|IllegalStateException e)
                    {
                        ap.append("Invalid move. Play again. *"+e.getMessage()+"*\n");
                    }
                } else if (move.equals("rmwd")) {
                    /*int drawIndex = sc.nextInt();
                    int row = sc.nextInt();
                    int card = sc.nextInt();*/

                    token = readNextValidInput(sc);
                    if(token.equals("") || token.equals("q") || token.equals("Q"))
                        break;
                    int drawIndex = Integer.parseInt(token);

                    token = readNextValidInput(sc);
                    if(token.equals("") || token.equals("q") || token.equals("Q"))
                        break;
                    int row = Integer.parseInt(token);

                    token = readNextValidInput(sc);
                    if(token.equals("") || token.equals("q") || token.equals("Q"))
                        break;
                    int card = Integer.parseInt(token);

                    try {
                        model.removeUsingDraw(drawIndex - 1, row - 1, card - 1);
                    }
                    catch(IllegalArgumentException|IllegalStateException e)
                    {
                        ap.append("Invalid move. Play again. *"+e.getMessage()+"*\n");
                    }
                } else if (move.equals("dd")) {
                    //int drawIndex = sc.nextInt();

                    token = readNextValidInput(sc);
                    if(token.equals("") || token.equals("q") || token.equals("Q"))
                        break;
                    int drawIndex = Integer.parseInt(token);

                    try {
                        model.discardDraw(drawIndex - 1);
                    }
                    catch(IllegalArgumentException|IllegalStateException e)
                    {
                        ap.append("Invalid move. Play again. *"+e.getMessage()+"*\n");
                    }
                } else if (move.equals("q") || move.equals("Q")) {
                    ap.append("Game quit!\n");
                    //ap.append("State of game when quit:\n");
                    view.render();
                    ap.append("\n");
                    try {
                        int score = model.getScore();
                        ap.append("Score: " + score + "\n");
                    }
                    catch(IllegalStateException e)
                    {
                        ap.append("Invalid move. Play again. *"+e.getMessage()+"*\n");
                    }
                    break;
                } else {
                    // TODO: What to do here? Just ignore and continue?
                }

                //System.out.println("1 move done");
                //ap.append("1 move done\n");
                //view.render();
                //ap.append("\n");
            }

            // Should this be connected with the if-else structure below?
            if(token.equals("q") || token.equals("Q"))
            {
                ap.append("Game quit!\n");
                //ap.append("State of game when quit:\n");
                view.render();
                ap.append("\n");
                try {
                    int score = model.getScore();
                    ap.append("Score: " + score + "\n");
                }
                catch(IllegalStateException e)
                {
                    ap.append("Invalid move. Play again. *"+e.getMessage()+"*\n");
                }
            }

            // TODO: How to detect game over? Call toString()
            //if(model.isGameOver())
            if(model.toString().equals("You win!"))
            {
                //view.render();
                //ap.append("\n");
                ap.append("You win!\n");
            }
            else
            {
                //view.render();
                //ap.append("\n");
                try {
                    int score = model.getScore();
                    ap.append("Game over. Score: " + score+"\n");
                }
                catch(IllegalStateException e)
                {
                    ap.append("Invalid move. Play again. *"+e.getMessage()+"*\n");
                }
            }
        }
        catch (IOException e)
        {
            // System.err.println(e.getMessage());
            // e.printStackTrace();
            throw new IllegalStateException("Cannot write to Appendable");
        }
    }

    private String readNextValidInput(Scanner sc)
    {
        while(sc.hasNext())
        {
            String s = sc.next();
            if(s.equals("q") || s.equals("Q"))
                return s;

            // TODO: How to check numbers
            int n = 0;
            try
            {
                n = Integer.parseInt(s);
                return s;
            }
            catch(Exception e)
            {
            }
        }
        return "";
    }

    private String readNextValidMove(Scanner sc)
    {
        while(sc.hasNext())
        {
            String s = sc.next();
            if(s.equals("q"))
                return s;

        }
        return "";
    }
}
