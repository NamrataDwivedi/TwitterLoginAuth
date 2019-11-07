package kit.com.twitterlogin;

import android.app.Application;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterSession;

public class MyApplication extends Application {
    private static TwitterSession twitterSession;
    static String TWITTER_CONSUMER_KEY = "XgKaKmnp4J9gmOXhZ7FlfI22g";
    static String TWITTER_CONSUMER_SECRET = "UgBEbm0a6C0f1qyzNbkp77lPqgJYt7LVD5IKkdrWGIEZduZTSH";

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET))
                .debug(true)//enable debug mode
                .build();

        //finally initialize twitter with created configs
        Twitter.initialize(config);

    }


    public static void setTwitterSession(TwitterSession session) {
        twitterSession = session;
    }


    public static TwitterSession getTwitterSession() {
        return twitterSession;
    }
}
