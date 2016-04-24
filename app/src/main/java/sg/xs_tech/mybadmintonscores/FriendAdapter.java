package sg.xs_tech.mybadmintonscores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter<Friend> {
    private ImageView ivProfilePic;
    private TextView tvName;

    public FriendAdapter(Context context, ArrayList<Friend> friends) {
        super(context, 0, friends);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            final Friend selected = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_friend_detail, parent, false);
            }
            ivProfilePic = (ImageView) convertView.findViewById(R.id.profile_picture);
            tvName = (TextView) convertView.findViewById(R.id.player_name);
            tvName.setText(selected.getFname());
            if (selected.getProfile_picture_url().isEmpty()) {
                Bundle param = new Bundle();
                param.putBoolean("redirect", false);
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + selected.getId() + "/picture",
                        param, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
//                                                Log.i(this.toString(),"Profile Picture request result: " + response.toString());
                                try {
                                    JSONObject mData = response.getJSONObject().getJSONObject("data");
//                                                    Log.i(this.toString(), "Data: " + mData.toString());
                                    if (mData.has("url")) {
                                        Log.i(this.toString(),"Profile URL: " + mData.getString("url"));
                                        selected.setProfile_picture_url(mData.getString("url"));
                                    }
                                    ivProfilePic.setImageBitmap(getFacebookProfilePicture(selected.getProfile_picture_url()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).executeAsync();
            }
            else {
                ivProfilePic.setImageBitmap(getFacebookProfilePicture(selected.getProfile_picture_url()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertView; // super.getView(position, convertView, parent);
    }

    public static Bitmap getFacebookProfilePicture(String url) throws IOException {
        // Put to AsyncTask
        Log.i("FacebookProfilePicture","Profile URL: " + url);
        Bitmap picture;
        InputStream in = (InputStream) new URL(url).getContent();
        picture = BitmapFactory.decodeStream(in);
        in.close();
        return picture;
    }
}
