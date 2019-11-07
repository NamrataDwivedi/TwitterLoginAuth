package kit.com.twitterlogin.preferences;

import android.content.Context;
import com.google.gson.Gson;

public class SharedPref extends BaseSharedPref {
    private static SharedPref sharedPref;

    private SharedPref(Context context) {
        super(context);
    }

    public static SharedPref getInstance(Context context) {
        if (sharedPref == null) {
            sharedPref = new SharedPref(context.getApplicationContext());
        }
        return sharedPref;
    }

    public void setLoginStatus(boolean logInStatus) {
        put(Key.KEY_IS_USER_LOGGED_IN, logInStatus);
    }

    public boolean getLoginStatus() {
        return getBoolean(Key.KEY_IS_USER_LOGGED_IN, false);
    }


    public void saveOffline(Object value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        put(Key.KEY_GSON_VAl, json);
    }

    public String getOfflineData() {
        return getString(Key.KEY_GSON_VAl, "");
    }

    /**
     * Class for keeping all the keys used for shared preferences in one place.
     */
    public static class Key {
        static final String KEY_IS_USER_LOGGED_IN = "IsUserLoggedIn";
        static final String KEY_GSON_VAl = "jsonValue";

    }
}
