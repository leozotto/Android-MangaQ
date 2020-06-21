package com.example.mangaq.activity.util;

import android.app.Activity;
import android.content.Intent;

public class IntentManager {
    public static void goTo (Activity activity, Class targetActivityClass) {
        Intent intent = new Intent(activity, targetActivityClass);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void goTo (Activity activity, Class targetActivityClass, Boolean queue) {
        Intent intent = new Intent(activity, targetActivityClass);
        activity.startActivity(intent);
    }
}
