package com.example.mangaq.activity.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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

    public static void goTo(Activity activity, Class targetActivityClass, Bundle bundle) {
        Intent intent = new Intent(activity, targetActivityClass);
        intent.putExtra("extra", bundle);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void goTo(Activity activity, Class targetActivityClass, Bundle bundle, Boolean queue) {
        Intent intent = new Intent(activity, targetActivityClass);
        intent.putExtra("bundleExtra", bundle);
        activity.startActivity(intent);
    }

    public static void finish(Activity activity) {
        activity.finish();
    }
}
