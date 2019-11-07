
package kit.com.twitterlogin.modals;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserMention {

    @SerializedName("screen_name")
    @Expose
    private String screenName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Double id;
    @SerializedName("id_str")
    @Expose
    private String idStr;
    @SerializedName("indices")
    @Expose
    private List<Double> indices = null;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public List<Double> getIndices() {
        return indices;
    }

    public void setIndices(List<Double> indices) {
        this.indices = indices;
    }

}
