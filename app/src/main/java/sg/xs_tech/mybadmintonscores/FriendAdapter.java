package sg.xs_tech.mybadmintonscores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter<Friend> {
    private ImageView ivProfilePic;
    private TextView tvName;

    public FriendAdapter(Context context, ArrayList<Friend> friends) {
        super(context, 0, friends);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Friend selected = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_friend_detail, parent, false);
        }
        ivProfilePic = (ImageView) convertView.findViewById(R.id.profile_picture);
        tvName = (TextView) convertView.findViewById(R.id.player_name);
        tvName.setText(selected.getFname());

        selected.getProfile_picture(ivProfilePic);
        return convertView;
    }
}
