package kit.com.twitterlogin.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import kit.com.twitterlogin.R;
import kit.com.twitterlogin.BaseActivity;
import kit.com.twitterlogin.MyApplication;
import kit.com.twitterlogin.preferences.SharedPref;
import kit.com.twitterlogin.util.AndroidNetworkUtility;

public class TwitterLoginActivity extends BaseActivity {
    private TextView txtTitle;
    private TwitterLoginButton twitterLoginButton;
    private ImageView imgBounceBall, img_bounce_ball1, img_bounce_ball2;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_login);
        init();
        clickListeners();
        checkConnectivity();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (MyApplication.getTwitterSession() == null) {
            if (SharedPref.getInstance(TwitterLoginActivity.this).getLoginStatus()) {

                if (checkConnectivity()) {
                    twitterLoginButton.performClick();
                } else {
                    loginMethod(null, 0);
                }
            }

        }

    }

    private boolean checkConnectivity() {
        if (!AndroidNetworkUtility.getInstance().isConnected(TwitterLoginActivity.this)) {
            showNoInternetAlert();
            return false;
        }

        return true;
    }

    @Override
    protected void init() {
        super.init();
        builder = new AlertDialog.Builder(TwitterLoginActivity.this);
        txtTitle = findViewById(R.id.txt_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/script_regular.otf");
        imgBounceBall = findViewById(R.id.img_bounce_ball);
        img_bounce_ball1 = findViewById(R.id.img_bounce_ball1);
        img_bounce_ball2 = findViewById(R.id.img_bounce_bal2);
        twitterLoginButton = findViewById(R.id.default_twitter_login_button);
        txtTitle.setTypeface(tf);

        imgBounceBall.startAnimation(setAnimation(400, imgBounceBall));
        img_bounce_ball1.startAnimation(setAnimation(400, img_bounce_ball1));
        img_bounce_ball2.startAnimation(setAnimation(400, img_bounce_ball2));
    }

    private TranslateAnimation setAnimation(long startOffset, final ImageView imageView) {
        final TranslateAnimation transAnim = new TranslateAnimation(0, 0, 0,
                80);
        transAnim.setStartOffset(startOffset);
        transAnim.setDuration(3000);
        transAnim.setFillAfter(true);
        transAnim.setInterpolator(new BounceInterpolator());
        transAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.clearAnimation();
                imageView.startAnimation(transAnim);

            }
        });

        return transAnim;
    }

    @Override
    protected void clickListeners() {
        super.clickListeners();
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                SharedPref.getInstance(getApplicationContext()).setLoginStatus(true);
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                MyApplication.setTwitterSession(session);
                TwitterAuthToken authToken = session.getAuthToken();

                long userID = result.data.getId();

                loginMethod(session, userID);

            }

            @Override
            public void failure(TwitterException exception) {
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
        }
    }

    public void loginMethod(TwitterSession twitterSession, long userID) {
        Intent intent = new Intent(TwitterLoginActivity.this, HomeActivity.class);

        String userName;
        if (twitterSession != null) {
            userName = twitterSession.getUserName();
            intent.putExtra("username", userName);
        }
        intent.putExtra("id", userID);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
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

        if (!dialog.isShowing()) {
            dialog.show();
        }

    }

}
