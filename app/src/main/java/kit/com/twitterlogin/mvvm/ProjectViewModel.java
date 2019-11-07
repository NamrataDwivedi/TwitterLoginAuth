package kit.com.twitterlogin.mvvm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import kit.com.twitterlogin.modals.TwitterResponseModel;

public class ProjectViewModel extends ViewModel {

    private MutableLiveData<List<TwitterResponseModel>> mFeedList;

    public ProjectViewModel(long userId) {
        this.mFeedList = ProjectRepository.getInstance().loadInitialFeed(userId);
    }

    public ProjectViewModel(long userId, Double id) {
        this.mFeedList = ProjectRepository.getInstance().loadRemainingFeeds(userId, id);
    }

    public MutableLiveData<List<TwitterResponseModel>> getFeeds() {
        return mFeedList;
    }

}
