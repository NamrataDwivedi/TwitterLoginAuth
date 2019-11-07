package kit.com.twitterlogin.util;

import kit.com.twitterlogin.modals.TwitterResponseModel;

public class MessageEvent {
    TwitterResponseModel mTwitterResponseModel;

    public MessageEvent(TwitterResponseModel position) {
        mTwitterResponseModel = position;
    }

    public TwitterResponseModel getMessage() {
        return mTwitterResponseModel;
    }
}
