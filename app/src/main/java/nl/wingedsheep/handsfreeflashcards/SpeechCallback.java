package nl.wingedsheep.handsfreeflashcards;

/**
 * Created by vincentbons on 12/04/17.
 */

public interface SpeechCallback {
    void onStart();
    void onDone();
    void onError();
}