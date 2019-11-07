package kit.com.twitterlogin.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import kit.com.twitterlogin.R;
import kit.com.twitterlogin.BaseActivity;
import kit.com.twitterlogin.MyApplication;
import kit.com.twitterlogin.adapter.FeedsAdapter;
import kit.com.twitterlogin.modals.TwitterResponseModel;
import kit.com.twitterlogin.mvvm.ProjectViewModel;
import kit.com.twitterlogin.preferences.SharedPref;
import kit.com.twitterlogin.util.AndroidNetworkUtility;
import kit.com.twitterlogin.util.MessageEvent;

public class HomeActivity extends BaseActivity {

    private long userId;
    private TwitterSession session;
    private TwitterAuthToken authToken;
    private RecyclerView feedsList;
    private FeedsAdapter mAdapter;
    private List<TwitterResponseModel> listData;
    private ProgressBar progressBar;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private ProjectViewModel mProjectViewModel;
    private static boolean isInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        clickListeners();
    }

    private void loadOfflineData() {
        String offlineData = SharedPref.getInstance(HomeActivity.this).getOfflineData();

        if (!TextUtils.isEmpty(offlineData)) {
            try {
                JSONArray jsonArray = new JSONArray(offlineData);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(offlineData));
                reader.setLenient(true);
                List<TwitterResponseModel> responseModels = gson.fromJson(reader, new TypeToken<List<TwitterResponseModel>>() {
                }.getType());
                listData.addAll(responseModels);
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean checkConnectivity() {
        if (!AndroidNetworkUtility.getInstance().isConnected(HomeActivity.this)) {
            showNoInternetAlert();
            return false;
        }

        return true;
    }

    @Override
    protected void init() {
        super.init();
        builder = new AlertDialog.Builder(HomeActivity.this);
        userId = getIntent().getLongExtra("id", 0);
        feedsList = findViewById(R.id.my_recycler_view);
        progressBar = findViewById(R.id.progressbar);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HomeActivity.this);
        feedsList.setLayoutManager(mLayoutManager);
        feedsList.setItemAnimator(new DefaultItemAnimator());
        listData = new ArrayList<>();
        mAdapter = new FeedsAdapter(HomeActivity.this, listData);
        feedsList.setAdapter(mAdapter);

        if (MyApplication.getTwitterSession() != null) {
            mProjectViewModel = new ProjectViewModel(userId);
            progressBar.setVisibility(View.VISIBLE);
            mProjectViewModel.getFeeds().observe(this, new Observer<List<TwitterResponseModel>>() {
                @Override
                public void onChanged(List<TwitterResponseModel> twitterResponseModels) {
                    if (twitterResponseModels != null) {
                        if (twitterResponseModels.size() > 0) {
                            Log.i("TAG", "fetched: " + twitterResponseModels.size());
                            listData.addAll(twitterResponseModels);
                            SharedPref.getInstance(HomeActivity.this).saveOffline(twitterResponseModels);
                            mAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);

                        } else {
                            showError(getResources().getString(R.string.error_no_feeds));
                        }
                    } else {
                        showError(getResources().getString(R.string.error_api));
                    }
                }
            });
        } else {
            loadOfflineData();
        }

    }

    @Override
    protected void clickListeners() {
        super.clickListeners();

        feedsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                boolean endHasBeenReached = lastVisible + 5 >= totalItemCount;
                if (totalItemCount > 0 && endHasBeenReached) {
                    if (MyApplication.getTwitterSession() != null && checkConnectivity()) {
                        if (!isInProgress) {
                            progressBar.setVisibility(View.VISIBLE);
                            isInProgress = true;
                            fetchMoreData(userId, listData.get(listData.size() - 1).getId());
                        }
                    } else {
                        if (AndroidNetworkUtility.getInstance().isConnected(HomeActivity.this)) {
                            openTwitterLoginPage();
                        } else {
                            showNoInternetAlert();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Subscribe
    public void onEvent(MessageEvent event) {
        TwitterResponseModel twitterResponseModel = event.getMessage();

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.feeddata);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        SimpleDraweeView imgProfile = dialog.findViewById(R.id.img_profile);
        String url = twitterResponseModel.getUser().getProfileImageUrlHttps();
        imgProfile.setImageURI(url);

        TextView user_name = dialog.findViewById(R.id.user_name);
        user_name.setText("@" + twitterResponseModel.getUser().getScreenName());

        TextView display_name = dialog.findViewById(R.id.display_name);
        display_name.setText(twitterResponseModel.getUser().getName());

        TextView user_description = dialog.findViewById(R.id.user_description);
        user_description.setText(twitterResponseModel.getUser().getDescription());

        TextView user_location = dialog.findViewById(R.id.user_location);
        user_location.setText(twitterResponseModel.getUser().getLocation());

        Button button = dialog.findViewById(R.id.btn_yes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showNoInternetAlert() {
        builder.setTitle(getString(R.string.alertmessage_no_internet_title))
                .setMessage(getString(R.string.alertmessage_no_internet_msg))
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                            }
                        });
        if (dialog == null) {
            dialog = builder.create();
        }

        if (getWindow().getDecorView().getRootView().isShown()) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    public void showError(String message) {
        builder.setTitle(getString(R.string.alert_message_too_many_req_title))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.positive_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
        if (dialog == null) {
            dialog = builder.create();
        }

        if ((getWindow().getDecorView().getRootView().isShown())) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    private void fetchMoreData(long userId, Double id) {
        final ProjectViewModel projectViewModel = new ProjectViewModel(userId, id);

        if (MyApplication.getTwitterSession() != null) {
            projectViewModel.getFeeds().observe(this, new Observer<List<TwitterResponseModel>>() {
                @Override
                public void onChanged(List<TwitterResponseModel> twitterResponseModels) {
                    isInProgress = false;
                    if (twitterResponseModels != null) {
                        progressBar.setVisibility(View.GONE);
                        listData.addAll(twitterResponseModels);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        showError(getResources().getString(R.string.error_api));
                    }
                }
            });
        } else {
            openTwitterLoginPage();
        }
    }

    private void openTwitterLoginPage() {
        SharedPref.getInstance(getApplicationContext()).setLoginStatus(false);
        Intent intent = new Intent(HomeActivity.this, TwitterLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
