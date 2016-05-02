package sg.xs_tech.mybadmintonscores;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Friend implements Parcelable {
    private String id = "";
    private String fname = "";
    private String email = "";
    private String profile_picture_url = "";
    private Bitmap profile_picture = null;

    public Friend(String fname) {
        this.fname = fname;
    }
    public Friend(String id, String fname) {
        this.id = id;
        this.fname = fname;
//        retrieveProfileBitmapURL();
    }
    protected Friend(Parcel in) {
        id = in.readString();
        fname = in.readString();
        email = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(fname);
        dest.writeString(email);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
    protected String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getProfile_picture_url() {
        return profile_picture_url;
    }
    public void getProfile_picture(ImageView imageView) {
        retrieveProfileBitmapURL(imageView);
    }
    private void downloadProfileBitmap(ImageView imageView) throws ExecutionException, InterruptedException {
        if (profile_picture_url.isEmpty()) return;
        if (profile_picture != null) return;
        Log.i(this.toString(), "Downloading Profile picture Bitmap");
        profile_picture = new DownloadProfilePictureTask().execute(profile_picture_url).get();
        imageView.setImageBitmap(profile_picture);
    }
    private void retrieveProfileBitmapURL(final ImageView imageView) {
        if (id.isEmpty()) return;
        Bundle param = new Bundle();
        param.putBoolean("redirect", false);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + id + "/picture",
                param, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject mData = response.getJSONObject().getJSONObject("data");
                            if (mData.has("url")) {
                                try {
                                    Log.i(this.toString(), "Profile URL for " + fname + ": " + mData.getString("url"));
                                    profile_picture_url = mData.getString("url");
                                    downloadProfileBitmap(imageView);
                                } catch (ExecutionException|InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }
}
