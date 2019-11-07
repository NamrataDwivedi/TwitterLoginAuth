package kit.com.twitterlogin.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AndroidNetworkUtility {

    private static AndroidNetworkUtility INSTANCE;

    private AndroidNetworkUtility() {

    }

    public static AndroidNetworkUtility getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new AndroidNetworkUtility();
        }

        return INSTANCE;
    }

    public boolean isConnected(Context ctx) {
        boolean flag = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            flag = true;
        }
        return flag;
    }
}
