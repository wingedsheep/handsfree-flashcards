package nl.wingedsheep.handsfreeflashcards.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import nl.wingedsheep.handsfreeflashcards.Callback;

/**
 * Created by vincentbons on 11/04/17.
 */

public class TextToSpeechInstance {

    private Locale locale;
    private Context context;
    private TextToSpeech textToSpeech = null;
    private boolean initialized = false;
    private ArrayList<String> beforeInitialized = new ArrayList<>();
    private String googleTtsPackage = "com.google.android.tts", picoPackage = "com.svox.pico";

    public TextToSpeechInstance(final Context context, final Locale locale) {
        this.context = context;
        this.locale = locale;
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setEngineByPackageName(googleTtsPackage);
                    textToSpeech.setLanguage(locale);
                    initialized = true;
                    for (String text : beforeInitialized) {
                        speak(text);
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
                    initialized = true;
                    if (callback != null) {
                        callback.onResponse(null);
                    }
                    for (String text : beforeInitialized) {
                        speak(text);
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

    public void speak(String text) {
        if (!initialized) {
            beforeInitialized.add(text);
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
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
}
