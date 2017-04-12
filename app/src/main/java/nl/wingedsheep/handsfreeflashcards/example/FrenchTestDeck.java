package nl.wingedsheep.handsfreeflashcards.example;

import java.util.Locale;

import nl.wingedsheep.handsfreeflashcards.model.Card;
import nl.wingedsheep.handsfreeflashcards.model.Deck;
import nl.wingedsheep.handsfreeflashcards.model.LearningDirection;

/**
 * Created by vincentbons on 12/04/17.
 */

public class FrenchTestDeck extends Deck {

    Locale french = Locale.FRENCH;
    Locale dutch = Locale.forLanguageTag("nl_NL");

    public FrenchTestDeck() {
        super();
        this.getCards().add(new Card("vechten", "lutter", dutch, french, LearningDirection.BOTH));
        this.getCards().add(new Card("vuilniszakkenjas", "doudoune", dutch, french, LearningDirection.BOTH));
        this.getCards().add(new Card("rits", "fermeture éclair", dutch, french, LearningDirection.BOTH));
        this.getCards().add(new Card("vastzitten", "se coincer", dutch, french, LearningDirection.BOTH));
        this.getCards().add(new Card("zich gedragen", "se comporter", dutch, french, LearningDirection.BOTH));
        this.getCards().add(new Card("beleefdheid", "politesse", dutch, french, LearningDirection.BOTH));
        this.getCards().add(new Card("je haar doen", "se coiffer", dutch, french, LearningDirection.BOTH));
    }
}
