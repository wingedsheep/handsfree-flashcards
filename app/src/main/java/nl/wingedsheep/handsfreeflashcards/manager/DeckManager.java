package nl.wingedsheep.handsfreeflashcards.manager;

import java.util.Random;

import nl.wingedsheep.handsfreeflashcards.model.Card;
import nl.wingedsheep.handsfreeflashcards.model.Deck;

/**
 * Created by vincentbons on 12/04/17.
 */

public class DeckManager {

    private static Random random = new Random();

    public static Card drawRandomCard(Deck deck) {
        int rand = random.nextInt(deck.getCards().size());
        return deck.getCards().get(rand);
    }
}
