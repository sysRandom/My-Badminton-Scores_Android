package sg.xs_tech.mybadmintonscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

public class FriendsList extends AppCompatActivity {

    private ListView mFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        mFriendList = (ListView) findViewById(R.id.friends_list);

        Intent intent = getIntent();
        ArrayList<Friend> friends = intent.getParcelableArrayListExtra("fbFriends");

        FriendAdapter adapter = new FriendAdapter(this, friends);
        mFriendList.setAdapter(adapter);
        mFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend selected = (Friend) parent.getItemAtPosition(position);
                Log.i(this.toString(), String.format(Locale.getDefault(), "ID: %s, Name: %s", selected.getId(), selected.getFname()));
            }
        });
    }
}
