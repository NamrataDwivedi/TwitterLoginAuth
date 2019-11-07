package kit.com.twitterlogin.twitter;

import com.twitter.sdk.android.core.models.User;

import java.util.List;

import kit.com.twitterlogin.modals.TwitterResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetUsersShowAPICustomService {
    @GET("/1.1/users/show.json")
    Call<User> show(@Query("user_id") long userId);


    @GET("/1.1/statuses/home_timeline.json")
    Call<List<TwitterResponseModel>> getStatuses(@Query("user_id") long userId, @Query("count") long val);

    @GET("/1.1/statuses/home_timeline.json")
    Call<List<TwitterResponseModel>> getStatuses(@Query("user_id") long userId, @Query("count") long val,
                                                 @Query("since_id") Double value);

    @GET("/1.1/statuses/home_timeline.json")
    Call<List<TwitterResponseModel>> getStatuses();

    /*
     * In retrofit v1 you need to write like this
     *
     * @GET("/1.1/users/show.json")
     * void show(@Query("user_id") long userId, Callback<User> callBack);
     *
     * */
}
