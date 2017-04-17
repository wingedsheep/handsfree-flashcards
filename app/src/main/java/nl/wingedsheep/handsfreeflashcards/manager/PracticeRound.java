package nl.wingedsheep.handsfreeflashcards.manager;

import android.content.Context;

import org.threeten.bp.LocalDateTime;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import nl.wingedsheep.handsfreeflashcards.SpeechCallback;
import nl.wingedsheep.handsfreeflashcards.model.Card;
import nl.wingedsheep.handsfreeflashcards.model.Deck;
import nl.wingedsheep.handsfreeflashcards.model.LearningDirection;

/**
 * Created by vincentbons on 12/04/17.
 */

public class PracticeRound {
    private Locale mainLocale = Locale.ENGLISH;
    private HashMap<Locale, TextToSpeechInstance> speechEngines = new HashMap<>();
    private Deck deck;
    private Context context;
    private boolean stopping = false;
    private LocalDateTime endTime = null;
    private Random random = new Random();
    private ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(1);

    public PracticeRound(Deck deck, Context context) {
        this.deck = deck;
        this.context = context;
        for (Card card : deck.getCards()) {
            Locale locale = card.getFrontLocale();
            if (!speechEngines.containsKey(locale)) {
                speechEngines.put(locale, new TextToSpeechInstance(context, locale));
            }
            Locale backLocale = card.getBackLocale();
            if (!speechEngines.containsKey(backLocale)) {
                speechEngines.put(backLocale, new TextToSpeechInstance(context, backLocale));
            }
        }
        speechEngines.put(mainLocale, new TextToSpeechInstance(context, mainLocale));
    }

    public void addCardTask(int delaySeconds) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Card randomCard = DeckManager.drawRandomCard(deck);
                playCard(randomCard);
            }
        };
        executorService.schedule(task, delaySeconds, TimeUnit.SECONDS);
    }

    private boolean isInitialized() {
        boolean initialized = speechEngines.get(mainLocale).isInitialized();
        if (! initialized) return false;
        else {
            for (TextToSpeechInstance instance : speechEngines.values()) {
                if (!instance.isInitialized()) return false;
            }
        }
        return true;
    }

    public void addAnswerTask(final Card card, final LearningDirection learningDirection, int delaySeconds) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                playAnswer(card, learningDirection);
            }
        };
        executorService.schedule(task, delaySeconds, TimeUnit.SECONDS);
    }

    public void playMinutes(final int minutes) {
        if (!isInitialized()) {
            // TODO use callbacks of the speechInstances for this
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!isInitialized()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    startPlayMinutes(minutes);
                }
            });
            t.start();
        } else {
            startPlayMinutes(minutes);
        }
    }

    private void startPlayMinutes(final int minutes) {
        stopping = false;
        endTime = LocalDateTime.now().plusMinutes(minutes);
        addCardTask(0);
    }

    public LearningDirection playCard(Card card) {
        if (card.getLearningDirection() == LearningDirection.BOTH) {
            if (random.nextDouble() > 0.5) {
                return playCard(card, LearningDirection.BACK_TO_FRONT);
            } else {
                return playCard(card, LearningDirection.FRONT_TO_BACK);
            }
        } else if (card.getLearningDirection() == LearningDirection.BACK_TO_FRONT) {
            return playCard(card, LearningDirection.BACK_TO_FRONT);
        } else {
            return playCard(card, LearningDirection.FRONT_TO_BACK);
        }
    }

    public LearningDirection playCard(final Card card, final LearningDirection learningDirection) {
        speechEngines.get(mainLocale).speak("What is.", new SpeechCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onDone() {
                if (learningDirection == LearningDirection.FRONT_TO_BACK) {
                    speechEngines.get(card.getFrontLocale()).speak(card.getFrontText(), new SpeechCallback() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDone() {
                            if (!stopping && LocalDateTime.now().isBefore(endTime)) {
                                addAnswerTask(card, learningDirection, 10);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
                } else if (learningDirection == LearningDirection.BACK_TO_FRONT) {
                    speechEngines.get(card.getBackLocale()).speak(card.getBackText(), new SpeechCallback() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDone() {
                            if (!stopping && LocalDateTime.now().isBefore(endTime)) {
                                addAnswerTask(card, learningDirection, 10);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
            }

            @Override
            public void onError() {

            }
        });

        return learningDirection;
    }

    public void playAnswer(final Card card, final LearningDirection learningDirection) {
        speechEngines.get(mainLocale).speak("The answer is.", new SpeechCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onDone() {
                if (learningDirection == LearningDirection.BACK_TO_FRONT) {
                    speechEngines.get(card.getFrontLocale()).speak(card.getFrontText(), new SpeechCallback() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDone() {
                            if (!stopping && LocalDateTime.now().isBefore(endTime)) {
                                addCardTask(5);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
                } else {
                    speechEngines.get(card.getBackLocale()).speak(card.getBackText(), new SpeechCallback() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onDone() {
                            if (!stopping && LocalDateTime.now().isBefore(endTime)) {
                                addCardTask(5);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    public void stop() {
        stopping = true;
        for (TextToSpeechInstance speechInstance : speechEngines.values()) {
            speechInstance.disconnect();
        }
    }
}
