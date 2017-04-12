package nl.wingedsheep.handsfreeflashcards;

import android.app.Application;
import android.content.Context;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class FlashCardsApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }

    public static Context getAppContext() {
        return FlashCardsApplication.context;
    }

}
