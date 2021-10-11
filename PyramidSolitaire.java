package cs3500.pyramidsolitaire;

import cs3500.pyramidsolitaire.controller.PyramidSolitaireTextualController;
import cs3500.pyramidsolitaire.model.hw02.Card;
import cs3500.pyramidsolitaire.model.hw02.PyramidSolitaireModel;
import cs3500.pyramidsolitaire.model.hw04.PyramidSolitaireCreator;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class PyramidSolitaire{
    public static void main(String[] args) {
        // No need to handle invalid command line args
        try {
            PyramidSolitaireModel<Card> model = null;

            if (args[0].equals("basic"))
                model = PyramidSolitaireCreator.create(PyramidSolitaireCreator.GameType.BASIC);
            else if (args[0].equals("relaxed"))
                model = PyramidSolitaireCreator.create(PyramidSolitaireCreator.GameType.RELAXED);
            else if (args[0].equals("multipyramid"))
                model = PyramidSolitaireCreator.create(PyramidSolitaireCreator.GameType.MULTIPYRAMID);

            int numRows = 7;
            int numDraw = 3;

            if (args.length == 3) {
                numRows = Integer.parseInt(args[1]);
                numDraw = Integer.parseInt(args[2]);
            }

            List<Card> deck = model.getDeck();

            // Changed the shuffle to true as per TA instructions
            // model.startGame(deck, true, numRows, numDraw);

            // Making controller and calling playGame as per TA instructions
            // Keeping the shuffle to true

            // TODO: What input string to give here? Or should we pass a Scanner to System.in
            Reader in = new StringReader("rm1 7 5 rmwd 3 6 6 q");
            StringBuilder out = new StringBuilder();
            PyramidSolitaireTextualController controller = new PyramidSolitaireTextualController(in, out);
            controller.playGame(model, deck, true, numRows, numDraw);
        }
        catch(IllegalArgumentException | IllegalStateException e)
        {
            // TODO: Change this?
            System.out.println("Some error occurred");
        }
    }
}
