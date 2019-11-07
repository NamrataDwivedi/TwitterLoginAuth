package kit.com.twitterlogin.mvvm;

import androidx.lifecycle.MutableLiveData;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import java.util.List;
import kit.com.twitterlogin.MyApplication;
import kit.com.twitterlogin.modals.TwitterResponseModel;
import kit.com.twitterlogin.twitter.MyTwitterApiClient;

class ProjectRepository {

    private int FEED_PER_REQUEST = 30;

    private static ProjectRepository mInstance;

    private ProjectRepository() {

    }

    static ProjectRepository getInstance() {
        if (mInstance == null) {
            mInstance = new ProjectRepository();
        }
        return mInstance;
    }

    MutableLiveData<List<TwitterResponseModel>> loadInitialFeed(long userID) {

        final MutableLiveData<List<TwitterResponseModel>> data = new MutableLiveData<>();

        new MyTwitterApiClient(MyApplication.getTwitterSession()).getCustomService().getStatuses(userID, FEED_PER_REQUEST)
                .enqueue(new Callback<List<TwitterResponseModel>>() {
                    @Override
                    public void success(Result<List<TwitterResponseModel>> result) {
                        List<TwitterResponseModel> responseResult = (List<TwitterResponseModel>) result.response.body();
                        data.setValue(responseResult);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        data.setValue(null);
                    }
                });
        return data;

    }


    MutableLiveData<List<TwitterResponseModel>> loadRemainingFeeds(long userID, Double id) {

        final MutableLiveData<List<TwitterResponseModel>> data = new MutableLiveData<>();

        new MyTwitterApiClient(MyApplication.getTwitterSession()).getCustomService().getStatuses(userID, FEED_PER_REQUEST, id)
                .enqueue(new Callback<List<TwitterResponseModel>>() {
                    @Override
                    public void success(Result<List<TwitterResponseModel>> result) {
                        List<TwitterResponseModel> responseModels = (List<TwitterResponseModel>) result.response.body();
                        data.setValue(responseModels);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        data.setValue(null);
                    }
                });
        return data;

    }

}
