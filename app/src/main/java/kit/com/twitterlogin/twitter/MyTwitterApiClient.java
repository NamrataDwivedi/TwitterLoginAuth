package kit.com.twitterlogin.twitter;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

public class MyTwitterApiClient extends TwitterApiClient {

    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    public GetUsersShowAPICustomService getCustomService() {
        return getService(GetUsersShowAPICustomService.class);
    }
}
