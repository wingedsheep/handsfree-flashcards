package nl.wingedsheep.handsfreeflashcards.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import nl.wingedsheep.handsfreeflashcards.Callback;
import nl.wingedsheep.handsfreeflashcards.SpeechCallback;

/**
 * Created by vincentbons on 11/04/17.
 */

public class TextToSpeechInstance {

    private Locale locale;
    private Context context;
    private TextToSpeech textToSpeech = null;
    private boolean initialized = false;
    private ArrayList<SpeechWithCallbacks> beforeInitialized = new ArrayList<>();
    private String googleTtsPackage = "com.google.android.tts", picoPackage = "com.svox.pico";
    // TODO map from utteranceID to callback. Entry gets removed after done or error.
    private SpeechCallback currentSpeechCallback;

    private class SpeechWithCallbacks {
        private String text;
        private SpeechCallback speechCallback;

        SpeechWithCallbacks(String text, SpeechCallback speechCallback) {
            this.text = text;
            this.speechCallback = speechCallback;
        }

        public String getText() {
            return text;
        }

        public SpeechCallback getSpeechCallback() {
            return speechCallback;
        }
    }

    public TextToSpeechInstance(final Context context, final Locale locale) {
        this.context = context;
        this.locale = locale;
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setEngineByPackageName(googleTtsPackage);
                    textToSpeech.setLanguage(locale);
                    UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {
                        @Override
                        public void onStart(String s) {
                            if (currentSpeechCallback != null) {
                                currentSpeechCallback.onStart();
                            }
                        }

                        @Override
                        public void onDone(String s) {
                            if (currentSpeechCallback != null) {
                                currentSpeechCallback.onDone();
                            }
                        }

                        @Override
                        public void onError(String s) {
                            if (currentSpeechCallback != null) {
                                currentSpeechCallback.onError();
                            }
                        }
                    };
                    textToSpeech.setOnUtteranceProgressListener(utteranceProgressListener);
                    initialized = true;
                    for (SpeechWithCallbacks speech : beforeInitialized) {
                        speak(speech.getText(), speech.getSpeechCallback());
                    }
                }
            }
        });
    }

    public TextToSpeechInstance(final Context context, final Locale locale, final Callback<Void> callback) {
        this.context = context;
        this.locale = locale;
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setEngineByPackageName(googleTtsPackage);
                    textToSpeech.setLanguage(locale);
                    UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {
                        @Override
                        public void onStart(String s) {
                            if (currentSpeechCallback != null) {
                                currentSpeechCallback.onStart();
                            }
                        }

                        @Override
                        public void onDone(String s) {
                            if (currentSpeechCallback != null) {
                                currentSpeechCallback.onDone();
                            }
                        }

                        @Override
                        public void onError(String s) {
                            if (currentSpeechCallback != null) {
                                currentSpeechCallback.onError();
                            }
                        }
                    };
                    initialized = true;
                    if (callback != null) {
                        callback.onResponse(null);
                    }
                    for (SpeechWithCallbacks speech : beforeInitialized) {
                        speak(speech.getText(), speech.getSpeechCallback());
                    }
                } else {
                    callback.onFailure(new Exception("Failed to initialize"));
                }
            }
        });
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void speak(String text, SpeechCallback speechCallback) {
        currentSpeechCallback = speechCallback;
        if (!initialized) {
            beforeInitialized.add(new SpeechWithCallbacks(text, speechCallback));
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(text);
        } else {
            ttsUnder20(text);
        }
    }

    public void disconnect() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId);
    }
}
