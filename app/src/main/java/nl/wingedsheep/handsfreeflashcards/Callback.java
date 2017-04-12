package nl.wingedsheep.handsfreeflashcards;

/**
 * Created by vincentbons on 12/04/17.
 */

public interface Callback<T> {
    void onResponse(T result);
    void onFailure(Throwable t);
}