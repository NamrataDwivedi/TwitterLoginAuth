
package kit.com.twitterlogin.modals;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Description_ {

    @SerializedName("urls")
    @Expose
    private List<Url______> urls = null;

    public List<Url______> getUrls() {
        return urls;
    }

    public void setUrls(List<Url______> urls) {
        this.urls = urls;
    }

}
