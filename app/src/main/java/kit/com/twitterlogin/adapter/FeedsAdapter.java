package kit.com.twitterlogin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import kit.com.twitterlogin.R;
import kit.com.twitterlogin.modals.TwitterResponseModel;
import kit.com.twitterlogin.util.MessageEvent;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedsViewHolder> {
    private Context mContext;
    private List<TwitterResponseModel> twitterResponseModelList;

    public FeedsAdapter(Context mContext, List<TwitterResponseModel> twitterResponseModelList) {
        this.mContext = mContext;
        this.twitterResponseModelList = twitterResponseModelList;
    }

    @NonNull
    @Override
    public FeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View itemView = layoutInflater.inflate(R.layout.feeds_list, parent, false);
        return new FeedsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedsViewHolder holder, final int position) {
        final TwitterResponseModel twitterResponseModel = twitterResponseModelList.get(position);

        holder.mUserId.setText("@" + twitterResponseModel.getUser().getScreenName().trim());
        holder.mUsername.setText(twitterResponseModel.getUser().getName().trim());
        holder.mUserTweet.setText(twitterResponseModel.getText().trim());
        String url = twitterResponseModel.getUser().getProfileImageUrlHttps();
        holder.imgProfile.setImageURI(url);


        holder.mUserId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new MessageEvent(twitterResponseModel));
            }
        });
    }

    @Override
    public int getItemCount() {
        return twitterResponseModelList.size();
    }

    class FeedsViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView imgProfile;

        TextView mUserId, mUsername, mUserTweet;

        FeedsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.img_profile);
            mUserId = itemView.findViewById(R.id.txt_screen_name);
            mUsername = itemView.findViewById(R.id.txt_user_name);
            mUserTweet = itemView.findViewById(R.id.txt_text);
        }
    }
}
