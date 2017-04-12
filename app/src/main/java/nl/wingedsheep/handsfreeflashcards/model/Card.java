package nl.wingedsheep.handsfreeflashcards.model;

import java.util.Locale;

/**
 * Created by vincentbons on 12/04/17.
 */

public class Card {
    private String frontText;
    private String backText;
    private Locale frontLocale;
    private Locale backLocale;
    private LearningDirection learningDirection;

    public Card(String frontText, String backText, Locale frontLocale, Locale backLocale, LearningDirection learningDirection) {
        this.frontText = frontText;
        this.backText = backText;
        this.frontLocale = frontLocale;
        this.backLocale = backLocale;
        this.learningDirection = learningDirection;
    }

    public String getFrontText() {
        return frontText;
    }

    public String getBackText() {
        return backText;
    }

    public Locale getFrontLocale() {
        return frontLocale;
    }

    public Locale getBackLocale() {
        return backLocale;
    }

    public LearningDirection getLearningDirection() {
        return learningDirection;
    }
}
